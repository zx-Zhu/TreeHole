package com.zxzhu.show.units.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * Created by zxzhu on 2017/8/2.
 */

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment : Fragment() {

    private var mView: View? = null
    var inflater: LayoutInflater? = null
        private set
    var viewGroup: ViewGroup? = null
        private set
    private var binding: ViewDataBinding? = null

    //获取当前fragment的view的方法
    protected abstract val resourceId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(resourceId, container, false)
        binding = DataBindingUtil.inflate(inflater, resourceId, container, false)
        this.inflater = inflater
        this.viewGroup = container
        initData()
        return binding!!.root
    }

    //做数据或其他初始化的方法
    protected abstract fun initData()

    fun toast(str: String) {
        Toast.makeText(activity, str, Toast.LENGTH_SHORT).show()
    }

    //获取对应ViewDataBinding
    fun <T : ViewDataBinding> getDataBinding(): T? {
        return binding as T?
    }

    //View快捷绑定id的方法
    fun <T : View> `$`(id: Int): T {
        return mView!!.findViewById<View>(id) as T
    }
}
