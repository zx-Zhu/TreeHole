package com.zxzhu.show.units.base

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast

/**
 * Created by zxzhu on 2017/8/2.
 */

abstract class BaseActivity : AppCompatActivity(), IBaseActivity {

    //获取到当前activity的view的方法
    protected abstract val contentViewId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewId)
        val crashHandler = CrashHandler.getInstance()
        crashHandler.init(applicationContext)
        initData()
        initState()
        //        setTranslateToolbar();

    }


    override fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    override fun getActivity(): Activity {
        return this
    }

    open fun initState() {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            //getWindow().setNavigationBarColor(Color.TRANSPARENT);
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 19) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }


    //用于做数据或其他初始化的方法
    protected abstract fun initData()

    //View快捷绑定id的方法
    fun <T : View> `$`(id: Int): T {
        return findViewById<T>(id)
    }
}
