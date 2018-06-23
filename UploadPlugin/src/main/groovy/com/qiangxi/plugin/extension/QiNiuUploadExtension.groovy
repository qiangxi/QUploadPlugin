package com.qiangxi.plugin.extension

import com.qiangxi.plugin.utils.CheckUtil
import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * Create by Ray(任强强) on 2018/6/16.
 * 七牛云上传信息的配置
 */
class QiNiuUploadExtension extends BaseUploadExtension {
    /**
     * 七牛accessKey,必传
     */
    String accessKey

    /**
     * 七牛secretKey,必传
     */
    String secretKey
    /**
     * 上传到七牛哪个bucket下,必传
     */
    String bucket

    QiNiuUploadExtension(Project project) {
        super(project)
    }

    @Override
    void checkParams() {
        super.checkParams()
        if (CheckUtil.isEmpty(accessKey)) {
            throw new GradleException("accessKey must not be null or empty")
        }
        if (CheckUtil.isEmpty(secretKey)) {
            throw new GradleException("secretKey must not be null or empty")
        }
        if (CheckUtil.isEmpty(bucket)) {
            throw new GradleException("bucket must not be null or empty")
        }

    }
}