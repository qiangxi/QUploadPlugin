package com.qiangxi.plugin.extension

import com.qiangxi.plugin.utils.CheckUtil
import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * Create by Ray(任强强) on 2018/6/16.
 * 又拍云上传信息的配置
 */
class YouPaiUploadExtension extends BaseUploadExtension {

    /**
     * 要上传到又拍云哪个bucket下，必填
     */
    String bucketName

    /**
     * 上传的文件要保存到又拍云的哪个路径下，必填，格式请参考又拍云官方文档</br>
     * 如：“/uploads/{year}{mon}{day}/{random32}{.suffix}”
     */
    String savePath

    /**
     * 操作员名称，必填
     */
    String operatorName

    /**
     * 操作员密码，必填
     */
    String operatorPwd

    YouPaiUploadExtension(Project project) {
        super(project)
    }

    @Override
    void checkParams() {
        super.checkParams()
        if (CheckUtil.isEmpty(bucketName)) {
            throw new GradleException("bucketName must not be null or empty")
        }
        if (CheckUtil.isEmpty(savePath)) {
            throw new GradleException("savePath must not be null or empty")
        }
        if (CheckUtil.isEmpty(operatorName)) {
            throw new GradleException("operatorName must not be null or empty")
        }
        if (CheckUtil.isEmpty(operatorPwd)) {
            throw new GradleException("operatorPwd must not be null or empty")
        }
    }
}