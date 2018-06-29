package com.qiangxi.plugin.task

import com.google.gson.Gson
import com.qiangxi.plugin.exception.UploadException
import com.qiangxi.plugin.extension.PuGongYingUploadExtension
import com.qiangxi.plugin.utils.CheckUtil
import com.qiangxi.plugin.utils.FileUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

import java.util.concurrent.TimeUnit

/**
 * Create by Ray(任强强) on 2018/6/16.
 * 用于上传到蒲公英
 */
class PuGongYingUploadTask extends BaseUploadTask {
    private static final String TAG = "PuGongYingUploadTask"

    PuGongYingUploadTask() {
        group = "QUploadPlugin"
    }

    @Override
    void setupDependenciesIfNeeded() {
        final def dependsTask = project.extensions.QUpload.pugongying.dependsTask
        if (dependsTask != null) {
            def task = project.tasks.findByName(dependsTask)
            if (task != null && task.enabled) dependsOn task
        }
    }

    @Override
    void upload() {
        //ensure params
        PuGongYingUploadExtension config = project.extensions.QUpload.pugongying
        config.checkParams()

        //upload
        def sourceDir = new File(config.fileDir)
        def files = FileUtil.parseFileWithFile(sourceDir, config.filter)
        if (files == null || files.size() == 0) return

        def builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("_api_key", config.apiKey)
                .addFormDataPart("buildInstallType", "${config.installType}")
                .addFormDataPart("buildPassword", config.password)
                .addFormDataPart("buildUpdateDescription", config.updateDescription)
                .addFormDataPart("buildName", config.buildName)

        files.each {
            builder.addFormDataPart("file", it.name, RequestBody.create(MediaType.parse("*/*"), it))

            def call = generatePGYService().upload(builder.build())
            def response = call.execute()
            if (response.successful) {

                def json = new Gson().toJson(response.body())
                //save exception to local file .
                def fileDir = CheckUtil.isEmpty(config.archiveDir) ? getArchivesDir() : config.archiveDir
                FileUtil.stringToFile(fileDir, "PuGongYingArchive", json)
                project.logger.error("${TAG}:uploadFile->${it.name} upload success,返回内容请查看${fileDir}中PuGongYingArchive开头的日志文件")
            } else {

                //save exception to local file .
                def fileDir = CheckUtil.isEmpty(config.exceptionDir) ? getExceptionsDir() : config.exceptionDir
                def errorMsg = response.toString()
                project.logger.error("${TAG}:PuGongYingException=${errorMsg}")
                FileUtil.stringToFile(fileDir, "PuGongYingException", errorMsg)

                // throw new Exception to notify developer
                throw new UploadException("上传蒲公英异常，请查看${fileDir}中PuGongYingException开头的日志文件")
            }
        }
    }

    static PGYService generatePGYService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.pgyer.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build()
        return retrofit.create(PGYService.class)
    }

    static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
        builder.connectTimeout(600, TimeUnit.SECONDS)
        builder.writeTimeout(600, TimeUnit.SECONDS)
        builder.readTimeout(600, TimeUnit.SECONDS)
        return builder.build()
    }

}

interface PGYService {
    @POST("apiv2/app/upload")
    Call<PGYResponse> upload(@Body RequestBody body)
}

class PGYResponse {
    String buildKey
    String buildType
    int buildIsFirst
    int buildIsLastest
    int buildFileSize
    int appFileSize
    String buildName
    String buildVersion
    String buildVersionNo
    int buildBuildVersion
    String buildIdentifier
    String buildIcon
    String buildDescription
    String buildUpdateDescription
    String buildScreenShots
    String buildShortcutUrl
    String buildQRCodeURL
    String buildCreated
    String buildUpdated
}