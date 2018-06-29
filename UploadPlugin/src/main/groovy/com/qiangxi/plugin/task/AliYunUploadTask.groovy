package com.qiangxi.plugin.task

import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.PutObjectResult
import com.qiangxi.plugin.exception.UploadException
import com.qiangxi.plugin.extension.AliYunUploadExtension
import com.qiangxi.plugin.utils.CheckUtil
import com.qiangxi.plugin.utils.FileUtil

/**
 * Create by Ray(任强强) on 2018/6/16.
 * 用于上传到阿里云
 */
class AliYunUploadTask extends BaseUploadTask {

    private static final String TAG = "AliYunUploadTask"

    AliYunUploadTask() {
        group = "QUploadPlugin"
    }

    @Override
    void setupDependenciesIfNeeded() {
        final def dependsTask = project.extensions.QUpload.aliyun.dependsTask
        if (dependsTask != null) {
            def task = project.tasks.findByName(dependsTask)
            if (task != null && task.enabled) dependsOn task
        }
    }

    @Override
    void upload() {
        //ensure params
        AliYunUploadExtension config = project.extensions.QUpload.aliyun
        config.checkParams()

        //upload
        def ossClient = new OSSClient(config.endpoint, config.accessKeyId, config.accessKeySecret)

        //mkDir if needed
        if (!CheckUtil.isEmpty(config.savePath)) {
            PutObjectResult result = ossClient.putObject(config.bucketName, config.savePath, new ByteArrayInputStream(new byte[0]))
            if (!result.response.successful) {
                reportError(result)
                return
            }
        }

        def sourceDir = new File(config.fileDir)
        def files = FileUtil.parseFileWithFile(sourceDir, config.filter)
        if (files == null || files.size() == 0) return
        files.each {
            PutObjectResult result
            if (CheckUtil.isEmpty(config.savePath)) {
                result = ossClient.putObject(config.bucketName, it.name, it)
            } else {
                result = ossClient.putObject(config.bucketName, config.savePath + it.name, new ByteArrayInputStream(new byte[0]))
            }
            if (result.response.successful) {
                project.logger.error("${TAG}:uploadFile->${it.name} upload success...")
            } else {
                reportError(result)
            }
        }

        ossClient.shutdown()

    }

    private reportError(PutObjectResult result, AliYunUploadExtension config) {
        //save exception to local file .
        def fileDir = CheckUtil.isEmpty(config.exceptionDir) ? getExceptionsDir() : config.exceptionDir

        def response = result.response
        def errorMsg = """  errorResponseAsString=${response.errorResponseAsString},
                            statusCode=${response.statusCode},
                            uri=${response.uri}
                       """.stripMargin()

        project.logger.error("${TAG}:AliyunException=${errorMsg}")
        FileUtil.stringToFile(fileDir, "AliyunException", errorMsg)

        // throw new Exception to notify developer
        throw new UploadException("上传阿里云异常，请查看${fileDir}中AliyunException开头的日志文件")
    }
}