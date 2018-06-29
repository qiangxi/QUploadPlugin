package com.qiangxi.plugin.extension

import com.qiangxi.plugin.utils.CheckUtil
import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * Create by Ray(任强强) on 2018/6/16.
 * 蒲公英上传信息的配置
 */
class PuGongYingUploadExtension extends BaseUploadExtension {

    /**
     * 蒲公英apiKey，必填
     */
    String apiKey

    /**
     * 应用安装方式，值为(1,2,3)。1：公开，2：密码安装，3：邀请安装，默认为1公开。非必填
     */
    int installType

    /**
     * 设置App安装密码，如果不想设置密码，请传空字符串，或不传。非必填
     */
    String password

    /**
     * 版本更新描述，请传空字符串，或不传。非必填
     */
    String updateDescription

    /**
     * 蒲公英buildName，应用名称,非必填
     */
    String buildName

    PuGongYingUploadExtension(Project project) {
        super(project)
    }

    @Override
    void checkParams() {
        super.checkParams()
        if (CheckUtil.isEmpty(apiKey)) {
            throw new GradleException("apiKey must not be null or empty")
        }
        if (installType != 1 && installType != 2 && installType != 3) {
            project.logger.error("the installType value should be one of [1,2,3],your value is $installType,so it will be reset to the defalut value of 1")
            installType = 1
        }
    }
}