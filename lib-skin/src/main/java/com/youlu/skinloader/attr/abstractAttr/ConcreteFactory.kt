package com.youlu.skinloader.attr.abstractAttr

import com.youlu.skinloader.attr.concrete.SkinAttr

/**
 * Author by Administrator , Date on 2018/11/21.
 * PS: Not easy to write code, please indicate.
 */
class ConcreteFactory : Factoty() {


    override fun <T : SkinAttr?> createSkinAttr(tClass: Class<T>?): T? {
        var product: SkinAttr? = null
        try {

            product = Class.forName(tClass?.name).newInstance() as SkinAttr
        } catch (e: Exception) {

        }
        return product as T
    }
}