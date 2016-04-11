/**
 * Copyright (C) 2016 The RDT of Wireless R&D in MIG. All right reversed.
 * <p/>
 * Created by vellhe on 16/4/7.
 */
package com.tencent.bugly.beta.demo;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

public class MainActivity extends Activity {

    Button checkUpgradeBtn;
    Button refreshBtn;
    TextView upgradeInfoTv;
    TextView appInfoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkUpgradeBtn = $(R.id.check_upgrade);
        refreshBtn = $(R.id.refresh_info);
        upgradeInfoTv = $(R.id.upgrade_info);
        appInfoTv = $(R.id.app_info);

        loadAppInfo();

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

    private void loadAppInfo() {
        try {
            StringBuilder info = new StringBuilder();
            PackageInfo packageInfo =
                    this.getPackageManager().getPackageInfo(this.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            info.append("appid: ").append(DemoApplication.APP_ID).append(" ")
                    .append("channel: ").append(DemoApplication.APP_CHANNEL).append(" ")
                    .append("version: ").append(versionName).append(".").append(versionCode);
            appInfoTv.setText(info);
        } catch (Exception e) {
                e.printStackTrace();
        }
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
