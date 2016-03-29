package com.tencent.bugly.beta.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

public class MainActivity extends Activity {

    public static final String APP_ID = "900020779"; // TODO 替换成bugly上注册的appid

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

        Beta.autoInit = true;

        /***** Beta高级设置 *****/
        Beta.betaStartDelay = 0; // 初始化延时设为0，立即初始化

        /***** 统一初始化Bugly产品，包含Beta *****/
        Bugly.init(this, APP_ID, true);

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

    @Override
    protected void onResume() {
        super.onResume();
        loadUpgradeInfo();
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

    /**
     * 代替findViewById
     * 
     * @param resId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T $(int resId) {
        return (T) findViewById(resId);
    }
}
