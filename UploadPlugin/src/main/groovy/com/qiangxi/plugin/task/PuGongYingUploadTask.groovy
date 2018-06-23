package com.qiangxi.plugin.task
/**
 * Create by Ray(任强强) on 2018/6/16.
 * 用于上传到蒲公英
 */
class PuGongYingUploadTask extends BaseUploadTask {
    PuGongYingUploadTask() {
        group = "QUploadPlugin"
    }

    @Override
    void setupDependenciesIfNeeded() {
        final def dependsTasks = project.extensions.QUpload.pugongying.depends
        final def autoUpload = project.extensions.QUpload.pugongying.autoUpload
        if (autoUpload && dependsTasks != null) {
            this.dependsOn dependsTasks
        }
    }

    @Override
    void upload() {

    }
}