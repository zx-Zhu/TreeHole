package com.zxzhu.show

import android.app.Application
import com.avos.avoscloud.AVOSCloud
import com.taobao.sophix.PatchStatus
import com.taobao.sophix.SophixManager
import com.zxzhu.show.units.Apis

/**
 * Created by zxzhu
 * 2018/3/15.
 * enjoy it !!
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AVOSCloud.initialize(this, Apis.LeanCloudId, Apis.LeanCloudKey)
        AVOSCloud.setDebugLogEnabled(true)
        initSophix()
    }

    private fun initSophix() {
        SophixManager.getInstance().setContext(this)
                .setAppVersion("1.0")
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub { _, code, _, _ ->
                    // 补丁加载回调通知
                    when (code) {
                        PatchStatus.CODE_LOAD_SUCCESS -> {
                            // 表明补丁加载成功
                        }
                        PatchStatus.CODE_LOAD_RELAUNCH -> {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                            //这里慎重选择强制关闭 ，容易产生闪退的错觉
                        }
                        else -> {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }.initialize()
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch()
    }
}

