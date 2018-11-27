package com.youlu.skinloader.attr

import android.util.Log
import com.youlu.skinloader.attr.abstractAttr.ConcreteFactory
import com.youlu.skinloader.attr.concrete.*
import java.util.*

/**
 * Author by Administrator , Date on 2018/11/21.
 * PS: Not easy to write code, please indicate.
 * 变为可配置
 */

class AttrFactoryKt {

    companion object {
        private const val TAG = "AttrFactory"

        const val BACKGROUND = "background"
        const val TEXT_COLOR = "textColor"
        const val TAB_INDICATOR_COLOR = "tabIndicatorColor"
        const val CONTENT_SCRIM_COLOR = "contentScrimColor"
        const val BACKGROUND_TINTLIST = "backgroundTint"
        const val NAVIGATION_VIEW_MENU = "navigationViewMenu"

        private val map = HashMap<String, Class<out SkinAttr>>()

        init {
            map[BACKGROUND] = BackgroundAttr::class.java
            map[TEXT_COLOR] = TextColorAttr::class.java
            map[TAB_INDICATOR_COLOR] = TabLayoutAttr::class.java
            map[CONTENT_SCRIM_COLOR] = CollapsingToolbarLayoutAttr::class.java
            map[BACKGROUND_TINTLIST] = FabButtonAttr::class.java
            map[NAVIGATION_VIEW_MENU] = NavigationViewAttr::class.java
        }

        @JvmStatic
         fun get(attrName: String, attrValueRefId: Int, attrValueRefName: String, typeName: String): SkinAttr? {
            Log.i(TAG, "attrName:$attrName")

            val tClass = map[attrName]
            val concreteFactory = ConcreteFactory()
            val mSkinAttr: SkinAttr? = concreteFactory.createSkinAttr(tClass)

            mSkinAttr?.attrName = attrName
            mSkinAttr?.attrValueRefId = attrValueRefId
            mSkinAttr?.attrValueRefName = attrValueRefName
            mSkinAttr?.attrValueTypeName = typeName
            return mSkinAttr
        }


        /**
         * 检测属性是否被支持
         *
         * @param attrName
         * @return true : supported <br></br>
         * false: not supported
         */
        @JvmStatic
        fun isSupportedAttr(attrName: String): Boolean {
            if (BACKGROUND == attrName) {
                return true
            }
            if (TEXT_COLOR == attrName) {
                return true
            }
            if (TAB_INDICATOR_COLOR == attrName) {
                return true
            }
            if (CONTENT_SCRIM_COLOR == attrName) {
                return true
            }
            return if (BACKGROUND_TINTLIST == attrName) {
                true
            } else NAVIGATION_VIEW_MENU == attrName
        }
    }





}