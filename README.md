# QUploadPlugin
QUploadPlugin 是一个gradle插件，适用于所有以gradle管理的项目中，如Android项目、Java web项目等。

### QUploadPlugin包含的功能
- 上传指定文件到七牛云，文件可以为任意格式，如图片、json文件、apk文件、war文件、jar文件等
- 上传指定文件到蒲公英，由于蒲公英是为移动端的apk或ipa做分发的，貌似仅支持apk或ipa文件
- 上传指定文件到又拍云，文件可以为任意格式，如图片、json文件、apk文件、war文件、jar文件等
- 上传指定文件到阿里云，文件可以为任意格式，如图片、json文件、apk文件、war文件、jar文件等

### 使用场景

#### Java Web服务war包或jar包存档
每次后端升级服务时，一般都会存档每次的war包或jar包，以便于当某次服务出现问题，可以回滚代码；
使用`QUploadPlugin`可以方便的把war包或jar包上传到阿里云或七牛云等CDN服务中

#### 移动端apk文件或ipa文件的存档
以本公司的Android为例，之前每次发布版本更新时，都需要手动的把生成的30多个渠道的apk文件上传到七牛云，
以便于用户下载更新，这个过程极为缓慢且容易出错，借助于`QUploadPlugin`，可以极为方便的把30多个渠道包
上传到七牛云，你只需要在命令行执行一个命令或在AndroidStudio中双击命令即可稳定快速上传，大大节省了开发人员
或运营同事的时间。

#### 与Jenkins等CI服务完美集成
在使用Jenkins或Travis等CI服务时，很多时候需要把构建出来的apk文件、war包等上传到CDN服务商中存档，以便于
其他同事或用户下载，之前可能会使用Python脚本，高级一点可能会自己写几个task满足需求，但有了`QUploadPlugin`
之后，使用Jenkins等CI服务更加方便，简单到你只需要执行一条命令就可以上传到七牛云，阿里云等CDN服务商中。

#### 其他任何有上传的需求
你可以把任何你想上传的文件通过`QUploadPlugin`上传到CDN提供商中，比如图片，json文件，Android apk 的混淆mapping文件等任何文件。

### 如何使用

#### 第一步： 在根项目级别的build.gradle文件中添加如下配置
```groovy
buildscript {
    repositories {
      ...
        jcenter()
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
比如在android项目的app级别的子项目中加入如下配置（每种上传方式的具体配置一会有详细说明）：
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
    upyun {
     ...
    }
}


dependencies {
   ...
}

```
经过以上两步配置，就可以愉快在项目中使用QUploadPlugin插件了。

### 执行上传操作
可以通过在命令行中使用命令上传，对于IDEA用户也可以在IDE面板的右侧`Gradle->app->quploadplugin中双击执行命令`
##### 命令行
- gradlew AliYunUploadTask
- gradlew PuGongYingUploadTask
- gradlew QiNiuUploadTask
- gradlew YouPaiUploadTask

同时执行多种方式上传
- gradlew YouPaiUploadTask AliYunUploadTask PuGongYingUploadTask YouPaiUploadTask

#####  IDEA控制面板双击执行
![IDEA右侧控制面板](https://github.com/qiangxi/QUploadPlugin/blob/master/img/IDEA.png?raw=true)

### 上传功能的配置
##### 所有上传方式都有的配置：

属性名|类型|是否必填|作用
---|---|---|---
fileDir|String|是|要上传的文件所在路径 ，不可空,否则抛出异常，目前只支持绝对路径
archiveDir|String|否|上传成功后，一些文件的存档路径，如蒲公英返回的json数据等，可空，空时使用默认存档路径,目前只支持绝对路径
exceptionDir|String|否|上传出现异常时，异常日志的存档路径，用于错误分析，可空 ，空时使用默认存档路径，目前只支持绝对路径
dependsTask|String|否|所依赖的task，可空，非空时，在执行upload之前，必须要先执行dependsTask， 如'build'
filter|String数组|否|过滤条件，用于过滤出要上传的文件，如'*.apk','*.war','*.jar'等，可空，空时上传所有文件

#####  七牛上传方式的专有配置
属性名|类型|是否必填|作用
---|---|---|---
accessKey|String|是|七牛accessKey,必传
secretKey|String|是|七牛secretKey,必传
bucket|String|是|上传到七牛哪个bucket下,必传
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
属性名|类型|是否必填|作用
---|---|---|---
apiKey|String|是|蒲公英apiKey，必填
installType|int|否|应用安装方式，值为(1,2,3)。1：公开，2：密码安装，3：邀请安装，默认为1公开。非必填
password|String|否|设置App安装密码，如果不想设置密码，请传空字符串，或不传。非必填
updateDescription|String|否|版本更新描述，请传空字符串，或不传。非必填
buildName|String|否|蒲公英buildName，应用名称,非必填
- 示例
```groovy
QUpload {

   pugongying {
       apiKey "xxxxxx"
       installType 1
       password "xxxxxx"
       buildName "xxxxxx"
       updateDescription "xxxxxx"
       fileDir buildDir.absolutePath + "${File.separator}outputs${File.separator}apk${File.separator}release"
       dependsTask 'assembleRelease'
       filter = ["*.apk"]
   }
}

```
##### 阿里云上传方式的专有配置
属性名|类型|是否必填|作用
---|---|---|---
accessKeyId|String|是|阿里云accessKeyId，必填
accessKeySecret|String|是|阿里云accessKeySecret，必填
bucketName|String|是|要上传到阿里云哪个bucket下，必填
endpoint|String|否|上传到阿里云哪个endpoint，非必填，不填时，使用默认值
savePath|String|否|上传的文件要保存到阿里云的哪个文件夹下，如“dir1/dir2/dir3/”，非必填，不填则默认上传到bucket根路径下
- 示例
```groovy
QUpload {

   aliyun {
       accessKeyId "xxxxxx"
       accessKeySecret "xxxxxxx"
       bucketName "xxxxxx"
       savePath "dir1/dir2/"
       endpoint "oss-cn-beijing.aliyuncs.com"
       fileDir buildDir.absolutePath + "${File.separator}outputs${File.separator}apk${File.separator}release"
       dependsTask 'assembleRelease'
       filter = ["*.apk", "*.json"]
   }
}

```

##### 又拍云上传方式的专有配置
属性名|类型|是否必填|作用
---|---|---|---
operatorName|String|是|操作员名称，必填
operatorPwd|String|是|操作员密码，必填
bucketName|String|是|要上传到又拍云哪个bucket下，必填
savePath|String|是|上传的文件要保存到又拍云的哪个路径下，必填， 如：“/uploads/{year}{mon}{day}/{random32}{.suffix}”，格式请参考又拍云官方文档

- 示例
```groovy
QUpload {

   upyun {
       operatorName "xxxxx"
       operatorPwd "xxxxx"
       bucketName "xxxxxx"
       savePath "/uploads/{year}{mon}{day}/{random32}{.suffix}"
       fileDir buildDir.absolutePath + "${File.separator}outputs${File.separator}apk${File.separator}release"
       dependsTask 'assembleRelease'
       filter = ["*.apk", "*.json"]
   }
}

```