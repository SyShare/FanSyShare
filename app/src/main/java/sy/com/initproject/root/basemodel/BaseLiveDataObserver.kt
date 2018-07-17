package com.baseproject.architecture

import android.arch.lifecycle.MutableLiveData
import viewmodel.BaseObserver

open class BaseLiveDataObserver<T>(toastError: Boolean = false, liveData: MutableLiveData<T>?) : BaseObserver<T>(toastError) {

    var mLiveData: MutableLiveData<T>? = liveData

    override fun onSuccess(resp: T) {
        mLiveData?.value = resp
    }
}