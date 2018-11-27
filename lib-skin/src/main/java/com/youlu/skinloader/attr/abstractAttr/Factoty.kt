package com.youlu.skinloader.attr.abstractAttr

import com.youlu.skinloader.attr.concrete.SkinAttr

/**
 * Author by Administrator , Date on 2018/11/21.
 * PS: Not easy to write code, please indicate.
 */
abstract class Factoty {

    /**
     * 创建对应的皮肤解析策略
     */
    internal abstract fun <T : SkinAttr?> createSkinAttr(tClass: Class<T>?): T?
}