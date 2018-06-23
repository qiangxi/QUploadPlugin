package com.qiangxi.plugin.utils

import java.text.SimpleDateFormat

/**
 * Create by Ray(任强强) on 2018/6/16.
 */
class DateUtil {

    static String format(long timeMills) {
        return new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(timeMills)
    }

}