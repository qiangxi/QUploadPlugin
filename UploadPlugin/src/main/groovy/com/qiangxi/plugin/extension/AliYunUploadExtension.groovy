package com.qiangxi.plugin.extension

import com.aliyun.oss.internal.OSSConstants
import com.qiangxi.plugin.utils.CheckUtil
import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * Create by Ray(任强强) on 2018/6/16.
 * 阿里云上传信息的配置
 */
class AliYunUploadExtension extends BaseUploadExtension {

    /**
     * 阿里云accessKeyId，必填
     */
    String accessKeyId

    /**
     * 阿里云accessKeySecret，必填
     */
    String accessKeySecret

    /**
     * 要上传到阿里云哪个bucket下，必填
     */
    String bucketName

    /**
     * 上传到阿里云哪个endpoint，非必填，不填时，使用默认值
     */
    String endpoint

    /**
     * 上传的文件要保存到阿里云的哪个路径下，非必填，如“dir1/dir2/dir3/”
     */
    String savePath

    AliYunUploadExtension(Project project) {
        super(project)
    }

    @Override
    void checkParams() {
        super.checkParams()
        if (CheckUtil.isEmpty(accessKeyId)) {
            throw new GradleException("accessKeyId must not be null or empty")
        }
        if (CheckUtil.isEmpty(accessKeySecret)) {
            throw new GradleException("accessKeySecret must not be null or empty")
        }
        if (CheckUtil.isEmpty(bucketName)) {
            throw new GradleException("bucketName must not be null or empty")
        }
        if (CheckUtil.isEmpty(endpoint)) {
            endpoint = OSSConstants.DEFAULT_OSS_ENDPOINT
            project.logger.error("your endpoint is not set value ,so we use default value $endpoint")
        }
    }
}