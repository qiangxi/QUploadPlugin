package com.qiangxi.plugin.task
/**
 * Create by Ray(任强强) on 2018/6/16.
 * 用于上传到阿里云
 */
class AliYunUploadTask extends BaseUploadTask {
    AliYunUploadTask() {
        group = "QUploadPlugin"
    }

    @Override
    void setupDependenciesIfNeeded() {
        final def dependsTasks = project.extensions.QUpload.aliyun.depends
        final def autoUpload = project.extensions.QUpload.aliyun.autoUpload
        if (autoUpload && dependsTasks != null) {
            this.dependsOn dependsTasks
        }
    }

    @Override
    void upload() {

    }
}