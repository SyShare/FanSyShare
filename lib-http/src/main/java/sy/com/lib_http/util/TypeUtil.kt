package com.pince.network.util

import java.lang.reflect.ParameterizedType

object TypeUtil {


    fun findParamsTypeClass(cls: Class<*>): Class<*>? {
        val findClass = getMethodClass(cls)
        return findClass ?: findParamsTypeClass(cls.superclass)
    }

    fun getMethodClass(cls: Class<*>): Class<*>? {
        val typeOri = cls.genericSuperclass
        // if Type is T
        if (typeOri is ParameterizedType) {
            val parentypes = typeOri.actualTypeArguments
            for (childtype in parentypes) {
                if (childtype is Class<*> ) {
                    return childtype
                }
            }
        }
        return null
    }
}