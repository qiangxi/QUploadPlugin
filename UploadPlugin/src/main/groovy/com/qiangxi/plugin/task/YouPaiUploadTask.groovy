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
        final def dependsTask = project.extensions.QUpload.youpai.dependsTask
        if (dependsTask != null) {
            def task = project.tasks.findByName(dependsTask)
            if (task != null && task.enabled) dependsOn task
        }
    }

    @Override
    void upload() {

    }
}