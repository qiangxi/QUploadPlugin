package com.qiangxi.plugin.task

import com.qiangxi.plugin.exception.UploadException
import com.qiangxi.plugin.extension.YouPaiUploadExtension
import com.qiangxi.plugin.utils.CheckUtil
import com.qiangxi.plugin.utils.FileUtil
import com.upyun.FormUploader
import com.upyun.Params

/**
 * Create by Ray(任强强) on 2018/6/16.
 * 用于上传到又拍云
 */
class YouPaiUploadTask extends BaseUploadTask {

    private static final String TAG = "YouPaiUploadTask"

    YouPaiUploadTask() {
        group = "QUploadPlugin"
    }

    @Override
    void setupDependenciesIfNeeded() {
        final def dependsTask = project.extensions.QUpload.upyun.dependsTask
        if (dependsTask != null) {
            def task = project.tasks.findByName(dependsTask)
            if (task != null && task.enabled) dependsOn task
        }
    }

    @Override
    void upload() {

        //ensure params
        YouPaiUploadExtension extension = project.extensions.QUpload.upyun
        extension.checkParams()

        //upload
        def uploader = new FormUploader(extension.bucketName, extension.operatorName, extension.operatorPwd)
        final def paramMap = new HashMap<String, Object>()
        paramMap.put(Params.SAVE_KEY, extension.savePath)
        def sourceDir = new File(extension.fileDir)
        def files = FileUtil.parseFileWithFile(sourceDir, extension.filter)
        if (files == null || files.size() == 0) return
        project.logger.error("${TAG}:uploading...")
        files.each {

            def result = uploader.upload(paramMap, it)
            if (result.succeed) {
                project.logger.error("${TAG}:uploadFile->${it.name} upload success...")
            } else {
                //save exception to local file .
                def fileDir = CheckUtil.isEmpty(extension.exceptionDir) ? getExceptionsDir() : extension.exceptionDir
                project.logger.error("${TAG}:YoupaiException=${result.toString()}")
                FileUtil.stringToFile(fileDir, "YoupaiException", result.toString(), FileUtil.TXT_SUFFIX)

                // throw new Exception to notify developer
                throw new UploadException("上传又拍云异常，请查看${fileDir}中YoupaiException开头的日志文件")
            }
        }
    }
}