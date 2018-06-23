package com.qiangxi.plugin.exception

import org.gradle.api.GradleException

/**
 * Create by Ray(任强强) on 2018/6/23.
 * UploadException
 */
class UploadException extends GradleException {
    UploadException() {
    }

    UploadException(String message) {
        super(message)
    }

    UploadException(String message, Throwable cause) {
        super(message, cause)
    }
}