package com.qiangxi.plugin.task
/**
 * Create by Ray(任强强) on 2018/6/16.
 * 用于上传到又拍云
 */
class YouPaiUploadTask extends BaseUploadTask {
    YouPaiUploadTask() {
        group = "QUploadPlugin"
    }

    @Override
    void setupDependenciesIfNeeded() {
        final def dependsTasks = project.extensions.QUpload.youpai.depends
        final def autoUpload = project.extensions.QUpload.youpai.autoUpload
        if (autoUpload && dependsTasks != null) {
            this.dependsOn dependsTasks
        }
    }

    @Override
    void upload() {

    }
}