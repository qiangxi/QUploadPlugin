package com.qiangxi.plugin.extension

import org.gradle.api.Project

/**
 * Create by Ray(任强强) on 2018/6/16.
 * 蒲公英上传信息的配置
 */
class PuGongYingUploadExtension extends BaseUploadExtension{
    /**
     * 可能需要，具体看看七牛上传时需要什么 ，不可空
     */
    String appKey

    PuGongYingUploadExtension(Project project) {
        super(project)
    }
}