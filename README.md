# QUploadPlugin
QUploadPlugin 是一个gradle插件，适用于所有以gradle管理的项目中，如Android项目、Java web项目等。

### QUploadPlugin包含的功能
- 上传指定文件到七牛云，文件可以为任意格式，如图片、json文件、apk文件、war文件、jar文件等
- 上传指定文件到蒲公英，由于蒲公英是为移动端的文件做分发的，所以仅支持apk或ipa文件
- 上传指定文件到又拍云，coming soon
- 上传指定文件到阿里云，coming soon

### 如何使用
现在还未发布，coming soon ...
#### 第一步： 在根项目级别的build.gradle文件中添加如下配置
```groovy
buildscript {
    repositories {
      ...
        maven {
            url uri('C:/maven-local/uploadPluginArchive')
        }
       ...
    }
    dependencies {
        ...
        classpath 'com.qiangxi:upload-plugin:1.0.0'
        ...
    }
}

```
#### 第二步：在你需要的子项目中加入如下配置
比如在android项目的app级别的子项目中加入如下配置：
```groovy
apply plugin: 'com.android.application'
apply plugin: 'com.qiangxi.upload'

android {
  ...
}

QUpload {

    qiniu {
     ...
    }
    aliyun {
     ...
    }
    pugongying {
     ...
    }
    youpai {
     ...
    }
}


dependencies {
   ...
}

```
经过以上两步配置，就可以愉快在项目中使用QUploadPlugin插件了。

### 上传功能的配置
##### 所有上传方式的都有的配置：

属性名|是否必填|作用
---|---|---
fileDir|是|要上传的文件所在路径 ，不可空,否则抛出异常，目前只支持绝对路径
archiveDir|否|上传成功后，一些文件的存档路径，如蒲公英返回的二维码图片等，可空，空时使用默认存档路径
exceptionDir|否|上传出现异常时，异常日志的存档路径，用于错误分析，可空 ，空时使用默认存档路径
dependsTask|否|所依赖的task，可空，非空时，在执行upload之前，必须要先执行dependsTask， 如'build'
filter|否|过滤条件，用于过滤出要上传的文件，可空，空时上传所有文件，如'*.apk','*.war','*.jar'等

#####  七牛上传方式的专有配置
属性名|是否必填|作用
---|---|---
accessKey|是|七牛accessKey,必传
secretKey|是|七牛secretKey,必传
bucket|是|上传到七牛哪个bucket下,必传
- 示例
```groovy
QUpload {

    qiniu {
        secretKey "xxxxxx"
        accessKey "xxxxxxxx"
        bucket "xxxx"
        fileDir buildDir.absolutePath + "${File.separator}outputs${File.separator}apk${File.separator}release"
        dependsTask 'build'
        filter = ["*.apk", "*.json"]
    }
}

```

##### 蒲公英上传方式的专有配置
coming soon ...