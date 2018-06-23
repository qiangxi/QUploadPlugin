package com.qiangxi.plugin.extension

import org.gradle.api.Project

/**
 * Create by Ray(任强强) on 2018/6/16.
 * 又拍云上传信息的配置
 */
class YouPaiUploadExtension extends BaseUploadExtension{
    /**
     * 可能需要，具体看看七牛上传时需要什么 ，不可空
     */
    String appKey

    YouPaiUploadExtension(Project project) {
        super(project)
    }
}