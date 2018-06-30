package com.qiangxi.plugin.task

import com.aliyun.oss.ClientException
import com.aliyun.oss.OSSClient
import com.aliyun.oss.OSSException
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

        def ossClient = new OSSClient(config.endpoint, config.accessKeyId, config.accessKeySecret)

        //mkDir if needed
        if (!CheckUtil.isEmpty(config.savePath)) {
            try {
                ossClient.putObject(config.bucketName, config.savePath, new ByteArrayInputStream(new byte[0]))
            } catch (Exception e) {
                String errorMsg
                if (e instanceof OSSException) {
                    def oSSException = e as OSSException
                    errorMsg = """
                                Message: ${oSSException.message},
                                Error Message: ${oSSException.errorMessage},
                                Error Code: ${oSSException.errorCode},
                                Request ID: ${oSSException.requestId},
                                Host ID: ${oSSException.hostId},
                               """.stripMargin()
                } else if (e instanceof ClientException) {
                    def clientException = e as ClientException
                    errorMsg = """
                                Message: ${clientException.message},
                                Error Message: ${clientException.errorMessage},
                                Error Code: ${clientException.errorCode},
                                Request ID: ${clientException.requestId},
                                Host ID: ${clientException.hostId},
                               """.stripMargin()
                } else {
                    errorMsg = e.toString()
                }
                reportError(errorMsg)
                ossClient.shutdown()
                return
            }
        }
        //upload file
        def sourceDir = new File(config.fileDir)
        def files = FileUtil.parseFileWithFile(sourceDir, config.filter)
        if (files == null || files.size() == 0) return
        project.logger.error("${TAG}:uploading...")
        files.each {
            try {
                if (CheckUtil.isEmpty(config.savePath)) {
                    ossClient.putObject(config.bucketName, it.name, it)
                } else {
                    ossClient.putObject(config.bucketName, config.savePath + it.name, new ByteArrayInputStream(new byte[0]))
                }
                project.logger.error("${TAG}:uploadFile->${it.name} upload success...")
            } catch (Exception e) {
                String errorMsg
                if (e instanceof OSSException) {
                    def oSSException = e as OSSException
                    errorMsg = """
                                Message: ${oSSException.message},
                                Error Message: ${oSSException.errorMessage},
                                Error Code: ${oSSException.errorCode},
                                Request ID: ${oSSException.requestId},
                                Host ID: ${oSSException.hostId},
                               """.stripMargin()
                } else if (e instanceof ClientException) {
                    def clientException = e as ClientException
                    errorMsg = """
                                Message: ${clientException.message},
                                Error Message: ${clientException.errorMessage},
                                Error Code: ${clientException.errorCode},
                                Request ID: ${clientException.requestId},
                                Host ID: ${clientException.hostId},
                               """.stripMargin()
                } else {
                    errorMsg = e.toString()
                }
                reportError(errorMsg)
                ossClient.shutdown()
                return
            }
        }

        ossClient.shutdown()

    }

    private reportError(String errorMsg, AliYunUploadExtension config) {
        //save exception to local file .
        def fileDir = CheckUtil.isEmpty(config.exceptionDir) ? getExceptionsDir() : config.exceptionDir

        project.logger.error("${TAG}:AliyunException=${errorMsg}")
        FileUtil.stringToFile(fileDir, "AliyunException", errorMsg, FileUtil.TXT_SUFFIX)

        // throw new Exception to notify developer
        throw new UploadException("上传阿里云异常，请查看${fileDir}中AliyunException开头的日志文件")
    }

}