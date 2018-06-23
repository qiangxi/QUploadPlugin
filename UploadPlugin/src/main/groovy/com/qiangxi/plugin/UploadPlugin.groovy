package com.qiangxi.plugin

import com.qiangxi.plugin.extension.AliYunUploadExtension
import com.qiangxi.plugin.extension.PuGongYingUploadExtension
import com.qiangxi.plugin.extension.QiNiuUploadExtension
import com.qiangxi.plugin.extension.WrapperExtension
import com.qiangxi.plugin.extension.YouPaiUploadExtension
import com.qiangxi.plugin.task.AliYunUploadTask
import com.qiangxi.plugin.task.PuGongYingUploadTask
import com.qiangxi.plugin.task.QiNiuUploadTask
import com.qiangxi.plugin.task.YouPaiUploadTask
import org.gradle.api.Plugin
import org.gradle.api.Project
/**
 * Create by Ray(任强强) on 2018/6/16.
 * - create extension
 * - create tasks
 */
class UploadPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        //create extension

        project.extensions.create("QUpload", WrapperExtension)
        project.QUpload.extensions.create("aliyun", AliYunUploadExtension, project)
        project.QUpload.extensions.create("pugongying", PuGongYingUploadExtension, project)
        project.QUpload.extensions.create("qiniu", QiNiuUploadExtension, project)
        project.QUpload.extensions.create("youpai", YouPaiUploadExtension, project)

        project.afterEvaluate {

            //check QUpload plugin status
            def upload = project.QUpload
            if (!upload.uploadEnabled) {
                project.logger.error("QUpload is disabled.")
                return
            }

            //create tasks

            project.tasks.create("AliYunUploadTask",AliYunUploadTask)
            project.tasks.create("PuGongYingUploadTask",PuGongYingUploadTask)
            project.tasks.create("QiNiuUploadTask",QiNiuUploadTask)
            project.tasks.create("YouPaiUploadTask",YouPaiUploadTask)

        }
    }
}
