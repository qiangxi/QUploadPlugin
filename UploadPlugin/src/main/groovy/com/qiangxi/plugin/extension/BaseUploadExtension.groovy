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
     * 要上传的文件所在路径 ，不可空
     */
    String fileDir

    /**
     * 当所依赖的task执行完毕时，是否自动执行上传操作
     */
    boolean autoUpload

    /**
     * 上传成功后邮件通知，可空，空时不进行邮件发送
     */
    Iterable<String> email

    /**
     * 上传成功后，返回的url存档路径，可空，空时不存档
     */
    String urlDir

    /**
     * 上传出现异常时，异常日志的存储路径，用于错误分析，可空 ，空时不存档
     */
    String exceptionDir

    /**
     * 所依赖的task，只有当autoUpload为false时才可空，否则抛出异常
     */
    Iterable<String> depends

    /**
     * 正则表达式，用于过滤出要上传的文件，可空，空时上传所有文件
     */
    String filter

    BaseUploadExtension(Project project) {
        this.project = project
    }

    void checkParams() {
        if (CheckUtil.isEmpty(fileDir)) {
            throw new GradleException("fileDir must not be null or empty")
        }

        if (autoUpload && depends == null) {
            throw new GradleException("your autoUpload properties was set to true ,but depends properties was set nothing ,(if autoUpload is true ,depends must not be null )")
        }
    }

    @Override
    String toString() {
        """| fileDir =  ${fileDir}
           | autoUpload = ${autoUpload}
           | email = ${email}
           | urlDir = ${urlDir}
           | exceptionDir = ${exceptionDir}
           | depends = ${depends}
           | filter = ${filter}
        """.stripMargin()
    }
}