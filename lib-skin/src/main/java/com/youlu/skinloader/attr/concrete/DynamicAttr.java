package com.youlu.skinloader.attr.concrete;

import com.youlu.skinloader.attr.AttrFactoryKt;

/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:10:21
 */
public class DynamicAttr {
    /**
     * attr name , defined from {@link AttrFactoryKt} :<br>
     * should be
     * <li> AttrFactory.BACKGROUND
     * <li> AttrFactory.TEXT_COLOR <br>
     * ...and so on
     */
    public String attrName;

    /**
     * resource id from default context , eg: "R.drawable.app_bg"
     */
    public int refResId;

    public DynamicAttr(String attrName, int refResId) {
        this.attrName = attrName;
        this.refResId = refResId;
    }
}
