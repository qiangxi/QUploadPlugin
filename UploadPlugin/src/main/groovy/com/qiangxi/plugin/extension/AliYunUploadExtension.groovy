package com.qiangxi.plugin.extension

import org.gradle.api.Project

/**
 * Create by Ray(任强强) on 2018/6/16.
 * 阿里云上传信息的配置
 */
class AliYunUploadExtension extends  BaseUploadExtension {

    AliYunUploadExtension(Project project) {
        super(project)
    }
}