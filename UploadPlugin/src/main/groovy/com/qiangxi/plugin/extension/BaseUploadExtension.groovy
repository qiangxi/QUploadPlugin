package com.qiangxi.plugin.extension

import com.qiangxi.plugin.utils.CheckUtil
import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * Create by Ray(任强强) on 2018/6/16.
 * 用于上传信息的配置
 */
class BaseUploadExtension {
    Project project

    /**
     * 要上传的文件所在路径 ，不可空,否则抛出异常</br>
     * 目前只能使用绝对路径
     */
    String fileDir

    /**
     * 上传成功后，一些文件的存档路径，如蒲公英返回的二维码图片等，可空，空时使用默认存档路径
     */
    String archiveDir

    /**
     * 上传出现异常时，异常日志的存档路径，用于错误分析，可空 ，空时使用默认存档路径
     */
    String exceptionDir

    /**
     * 所依赖的task，可空，非空时，在执行upload之前，必须要先执行dependsTask， 如'build'
     */
    String dependsTask

    /**
     * 过滤条件，用于过滤出要上传的文件，可空，空时上传所有文件，如'*.apk','*.war','*.jar'等
     */
    String[] filter

    BaseUploadExtension(Project project) {
        this.project = project
    }

    void checkParams() {
        if (CheckUtil.isEmpty(fileDir)) {
            throw new GradleException("fileDir must not be null or empty")
        }
    }

    @Override
    String toString() {
        """| fileDir =  ${fileDir}
           | archiveDir = ${archiveDir}
           | exceptionDir = ${exceptionDir}
           | dependsTask = ${dependsTask}
           | filter = ${filter}
        """.stripMargin()
    }
}