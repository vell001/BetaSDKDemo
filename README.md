# BetaSDKDemo
腾讯Bugly内测，升级SDK接入demo

# 升级功能（Android）使用指南

---

一、概述
--
升级功能是专为App的灰度升级而开发的组件，在bugly内测页面配置好App的更新策略，策略指定的老版本App在启动时会自动检测更新并提示升级，为团队的应用分发，灰度内测提供一站式解决方案。

> 
**准备工作** 
>
如果你之前已经在[Bugly][1]或[腾讯移动开放平台][2]注册了应用，并获取到AppID，可以继续使用它。 
如果你尚未注册应用，可以通过QQ登录Bugly网站，点击"用户名"，选择"我的App"，点击[注册新App][3]，填写完应用基本信息完成注册，即可得到Bugly AppID。

二、接入指南
----
 1. 自动导入（推荐）
如果您使用Gradle编译Apk，我们强烈推荐您使用自动接入方式配置库文件（[JCenter仓库）][4]）。
在Module的buid.gradle文件中添加依赖和属性配置：


        android {
          defaultConfig {
            ndk {
              //设置支持的SO库架构
              abiFilters 'armeabi'  //, 'x86', 'armeabi-v7a', 'x86_64', 'arm64-v8a'
            }
          }
        }
        dependencies {
        	//注释掉原有bugly的仓库
            //    compile 'com.tencent.bugly:crashreport:latest.release' //其中latest.release指代最新版本号，也可以指定明确的版本号，例如1.2.9
    		compile 'com.tencent.bugly:crashreport_upgrade:latest.release' // 其中latest.release指代最新版本号，也可以指定明确的版本号，例如1.0.0
        }

![此处输入图片的描述][5]

**后续更新内测 SDK时，只需变更配置脚本中的版本号即可。**

注意：
>
<font color = "red">**内测SDK已经集成crash上报功能，已经集成Bugly的用户需要注释掉原来Bugly的jcenter库;**</font>
>
<font color = "red">**自动集成时会自动包含Bugly SO库，建议在Module的build.gradle文件中使用NDK的“abiFilter”配置，设置支持的SO库架构**。</font>

如果在添加“abiFilter”之后Android Studio出现以下提示：

<font color = "red">NDK integration is deprecated in the current plugin. Consider trying the new experimental plugin.</font>

则在项目根目录的gradle.properties文件中添加：

    android.useDeprecatedNdk=true

 2. 手动导入

如果您不采用上述自动导入方式，也可以手动集成内测SDK。

注意：
>
<font color='red'>**已经接入Bugly SDK的用户需要先删除原Bugly SDK的jar包；**</font>
>
<font color='red'>**android4.1以上的eclipse工程必须把jar包放在libs目录下，否则会出现NoClassDefFoundError错误;**</font>


**下载内测SDK库文件**

 - 下载内测SDK的[Android SDK包][6]；
 - 如果您的工程有Native代码（C/C++）或者集成了其他第三方SO库，建议下载Bugly的[NDK动态库][7]。

 
    Bugly NDK包含多个架构的SO库：
    
    armeabi
    
    armeabi­v7a
    
    arm64­v8a
    
    x86
    
    x86_64
    
    <font color = "red">**在集成Bugly SO库时，请注意只保留支持的架构SO库。**</font>

**Eclipse 工程**

 - 将内测SDK的库文件复制到工程的libs目录下； 
 - Refresh一下工程；
 - 添加工程依赖：鼠标右键点击Bugly的JAR文件，添加到编译路径中

 ![此处输入图片的描述][8]
 
**Android Studio工程**

 - 将内测SDK的库文件复制到工程的libs目录下；
 - 点击Sync，同步配置。

![此处输入图片的描述][9]
  
  
 
三、参数配置
------
接入Bugly后，在AndroidManifest.xml中新添加如下权限：

    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_LOGS" />
    <!--保存资源到SD卡-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

在AndroidManifest.xml中添加以下Activity

    <activity
        android:name="com.tencent.bugly.beta.ui.BetaActivity"
        android:theme="@android:style/Theme.Translucent" />
    
请避免混淆内测SDK，在Proguard混淆文件中增加一行配置：

    -keep public class com.tencent.bugly.**{*;}

四、SDK初始化
--------
[获取APP ID][10]并将以下代码复制到项目Application类onCreate()中，Bugly会自动检测环境并启用默认配置：

    Bugly.init(getApplicationContext(), "注册时申请的APPID", false);
<font color='red'>**提示：已经接入Bugly的用户改用上面的初始化方法,不影响原有的crash上报功能;**</font>

五、测试验证
----
 1. 注册App，获取Appid

 使用前请先根据[Bugly使用文档][11]完善开发者信息与应用注册
 
 2. 上传升级包

 进入内测页面选择注册的APP，点击发布新版本，上传要升级的APP的版本（<font color='red'>**上传APP的versioncode必须不低于外发版本的versiocode，否则用户检测不到更新**</font>）
 
 ![此处输入图片的描述][12]

 3. 配置升级策略

 发布完成后点击升级配置

 ![此处输入图片的描述][13]

 点击新建升级策略

 ![此处输入图片的描述][14]

 使用默认策略配置，点击创建策略

 ![此处输入图片的描述][15]

 策略创建完成后会回到版本编辑页面，点击立即启动，使策略生效;

 ![此处输入图片的描述][16]
 
 4. 测试验证

 完成步骤1中的策略配置，在本地安装配置过内测SDK的低版本APP，启动后（<font color='red'>**请先杀掉进程**</font>）等待一段时间(默认是3s)会弹出如下升级弹窗，表示SDK配置成功

 ![此处输入图片的描述][17]

六、高级配置
------
我们提供Beta类作为Bugly的初始化扩展，通过Beta类可以修改升级的检测时机，界面元素以及自定义的升级行为,可以参考[BetaSdkDemo][18]的相关设置。

    public class DemoApplication extends Application{
        public static final String APP_ID = "900020779"; // TODO 替换成bugly上注册的appid
        @Override
        public void onCreate() {
            super.onCreate();
            
            /***** Beta高级设置 *****/
            /**
             * true表示app启动自动初始化升级模块;
             * false不会自动初始化;
             * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
             * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
             */
            Beta.autoInit = true;
            
            /**
             * true表示初始化时自动检查升级;
             * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
             */
            Beta.autoCheckUpgrade = true;
            
            /**
             * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
             */
            Beta.upgradeCheckPeriod = 60 * 1000;
            
            /**
             * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
             */
            Beta.initDelay = 1 * 1000;
            
            /**
             * 设置通知栏大图标，largeIconId为项目中的图片资源;
             */
            Beta.largeIconId = R.drawable.ic_launcher;
            
            /**
             * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
             */
            Beta.smallIconId = R.drawable.ic_launcher;
            
            /**
             * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
             * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
             */
            Beta.defaultBannerId = R.drawable.ic_launcher;
            
            /**
             * 设置sd卡的Download为更新资源保存目录;
             * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
             */
            Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            
            /**
             * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
             */
            Beta.showInterruptedStrategy = true;
            
            /**
             * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
             * 不设置会默认所有activity都可以显示弹窗;
             */
            Beta.canShowUpgradeActs.add(MainActivity.class);
            
            /***** 统一初始化Bugly产品，包含Beta *****/
            Bugly.init(this, APP_ID, true);
        }
    }

七、接口说明
------

> 更新功能主要API


    /**
    * 手动检查更新策略
    */
    public static synchronized void checkUpgrade()
    
    /**
    * 获取本地已有升级策略（非实时，可用于界面红点展示）
    *
    * @return
    */
    public static synchronized UpgradeInfo getUpgradeInfo()
    

> 示例
 

    public class MainActivity extends Activity {
        Button checkUpgradeBtn;
        Button refreshBtn;
        TextView upgradeInfoTv;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            checkUpgradeBtn = $(R.id.check_upgrade);
            refreshBtn = $(R.id.refresh_info);
            upgradeInfoTv = $(R.id.upgrade_info);
            
            checkUpgradeBtn.setOnClickListener(new OnClickListener() {
            @Override
                public void onClick(View v) {
                
                    /***** 检查更新 *****/
                    Beta.checkUpgrade();
                }
            });
            
            refreshBtn.setOnClickListener(new OnClickListener() {
            
                @Override
                public void onClick(View v) {
                    loadUpgradeInfo();
                }
            });
        }
        
        private void loadUpgradeInfo() {
            if (upgradeInfoTv == null)
                return;
                
            /***** 获取升级信息 *****/
            UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
            
            if (upgradeInfo == null) {
                upgradeInfoTv.setText("无升级信息");
                return;
            }
            StringBuilder info = new StringBuilder();
            info.append("id: ").append(upgradeInfo.id).append("\n");
            info.append("标题: ").append(upgradeInfo.title).append("\n");
            info.append("升级说明: ").append(upgradeInfo.newFeature).append("\n");
            info.append("versionCode: ").append(upgradeInfo.versionCode).append("\n");
            info.append("versionName: ").append(upgradeInfo.versionName).append("\n");
            info.append("发布时间: ").append(upgradeInfo.publishTime).append("\n");
            info.append("安装包Md5: ").append(upgradeInfo.apkMd5).append("\n");
            info.append("安装包下载地址: ").append(upgradeInfo.apkUrl).append("\n");
            info.append("安装包大小: ").append(upgradeInfo.fileSize).append("\n");
            info.append("弹窗间隔（ms）: ").append(upgradeInfo.popInterval).append("\n");
            info.append("弹窗次数: ").append(upgradeInfo.popTimes).append("\n");
            info.append("发布类型（0:测试 1:正式）: ").append(upgradeInfo.publishType).append("\n");
            info.append("弹窗类型（1:建议 2:强制 3:手工）: ").append(upgradeInfo.upgradeType);
            
            upgradeInfoTv.setText(info);
        }
    }
    

> UpgradeInfo内容如下


    public String id = "";//唯一标识
    public String title = "";//升级提示标题
    public String newFeature = "";//升级特性描述
    public long publishTime = 0;//升级发布时间,ms
    public int publishType = 0;//升级类型 0测试 1正式
    public int upgradeType = 1;//升级策略 1建议 2强制 3手工
    public int popTimes = 0;//提醒次数
    public long popInterval = 0;//提醒间隔
    public int versionCode;
    public String versionName = "";
    public String apkMd5;//包md5值
    public String apkUrl;//APK的CDN外网下载地址
    public long fileSize;//APK文件的大小


 


  [1]: http://bugly.qq.com/
  [2]: http://open.qq.com/
  [3]: http://bugly.qq.com/register
  [4]: http://jcenter.bintray.com/com/tencent/bugly/crashreport_upgrade/
  [5]: http://ww4.sinaimg.cn/large/7d7b7518gw1f2stc5eoooj21d80i0tbc.jpg
  [6]: http://bugly.qq.com/whitebook
  [7]: http://bugly.qq.com/whitebook
  [8]: http://ww3.sinaimg.cn/large/7d7b7518gw1f2lv3aykg3j20li0gtwfs.jpg
  [9]: http://ww2.sinaimg.cn/large/7d7b7518gw1f2lv3lxyixj20zk0j2jtp.jpg
  [10]: http://bugly.qq.com/apps
  [11]: http://bugly.qq.com/document
  [12]: http://ww4.sinaimg.cn/mw690/7d7b7518gw1f2g4vakopjj20m60ezmzh.jpg
  [13]: http://ww1.sinaimg.cn/mw690/7d7b7518gw1f2g4vp0sfpj20ot0cpgnh.jpg
  [14]: http://ww4.sinaimg.cn/mw690/7d7b7518gw1f2paj9ypbpj20m40g9my4.jpg
  [15]: http://ww3.sinaimg.cn/mw690/7d7b7518gw1f2paeui8hjj20ks0nlab7.jpg
  [16]: http://ww3.sinaimg.cn/mw690/7d7b7518gw1f2g4z7ux5kj20mw0bjgmm.jpg
  [17]: http://ww2.sinaimg.cn/large/7d7b7518gw1f2g50bv9quj207l0dhq3c.jpg
  [18]: https://github.com/vell001/BetaSDKDemo

