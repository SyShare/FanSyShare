package sy.com.lib_http.arch

import android.arch.lifecycle.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import java.lang.reflect.ParameterizedType


/**
 * 快捷显示error方法
 */
fun <T : ViewModel> FragmentActivity.getViewModel(modelClass: Class<T>): T {
    return ViewModelProviders.of(this).get(modelClass)
}

fun <T : ViewModel> Fragment.getViewModel(modelClass: Class<T>): T {
    return ViewModelProviders.of(this).get(modelClass)
}

fun <T : ViewModel> View.getViewModel(modelClass: Class<T>): T {
    return ViewModelProviders.of(this.context as FragmentActivity).get(modelClass)
}

fun <T> LiveData<T>.observeInView(view: View, observer: Observer<T>) {
    this.observe(view.context as LifecycleOwner, observer)
}

fun findParamsTypeClass(cls: Class<*>): Class<*>? {
    val findClass = getMethodClass(cls)
    if (cls.superclass != null) {
        return findClass ?: findParamsTypeClass(cls.superclass)
    } else {
        return null
    }
}

fun getMethodClass(cls: Class<*>): Class<*>? {
    val typeOri = cls.genericSuperclass
    // if Type is T
    if (typeOri is ParameterizedType) {
        val parentypes = typeOri.actualTypeArguments
        for (childtype in parentypes) {
            if (childtype is Class<*>) {
                return childtype
            }
        }
    }
    return null
}