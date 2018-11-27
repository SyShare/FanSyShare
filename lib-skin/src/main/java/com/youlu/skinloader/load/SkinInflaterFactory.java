package com.youlu.skinloader.load;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.youlu.skinloader.attr.AttrFactoryKt;
import com.youlu.skinloader.attr.concrete.DynamicAttr;
import com.youlu.skinloader.attr.concrete.SkinAttr;
import com.youlu.skinloader.config.SkinConfig;
import com.youlu.skinloader.entity.SkinItem;
import com.youlu.skinloader.util.L;
import com.youlu.skinloader.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:19
 * <p>
 * 自定义的InflaterFactory，用来代替默认的InflaterFactory
 * 参考链接：http://willowtreeapps.com/blog/app-development-how-to-get-the-right-layoutinflater/
 */
public class SkinInflaterFactory implements LayoutInflater.Factory2 {

    private static String TAG = "SkinInflaterFactory";
    /**
     * 存储那些有皮肤更改需求的View及其对应的属性的集合
     */
    private List<SkinItem> mSkinItems = new ArrayList<>();

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        boolean isSkinEnable = attrs.getAttributeBooleanValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false);
        if (!isSkinEnable) {
            return null;//返回空就使用默认的InflaterFactory
        }
        View view = createView(context, name, attrs);
        if (view == null) {//没有找到这个View
            return null;
        }
        parseSkinAttr(context, attrs, view);
        return view;
    }

    //SDK >=11
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView(null, name, context, attrs);
    }

    /**
     * 通过name去实例化一个View
     *
     * @param context
     * @param name    要被实例化View的全名.
     * @param attrs   View在布局文件中的XML的属性
     * @return View
     */
    private View createView(Context context, String name, AttributeSet attrs) {
        Log.i(TAG, "createView:" + name);
        View view = null;
        try {
            if (-1 == name.indexOf('.')) {
                if ("View".equals(name)) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                }
            } else {
                view = LayoutInflater.from(context).createView(name, null, attrs);
            }

            L.i(TAG, "about to create " + name);

        } catch (Exception e) {
            L.e(TAG, "error while create 【" + name + "】 : " + e.getMessage());
            view = null;
        }
        return view;
    }

    /**
     * 搜集可更换皮肤的属性
     *
     * @param context
     * @param attrs
     * @param view
     */
    private void parseSkinAttr(Context context, AttributeSet attrs, View view) {
        //存储View可更换皮肤属性的集合
        List<SkinAttr> viewAttrs = new ArrayList<SkinAttr>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if (!AttrFactoryKt.isSupportedAttr(attrName)) {
                continue;
            }
            //也就是引用类型，形如@color/red
            if (attrValue.startsWith("@")) {
                try {
                    //资源的id
                    int id = Integer.parseInt(attrValue.substring(1));
                    //入口名，例如text_color_selector
                    String entryName = context.getResources().getResourceEntryName(id);
                    //类型名，例如color、background
                    String typeName = context.getResources().getResourceTypeName(id);
                    SkinAttr mSkinAttr = AttrFactoryKt.get(attrName, id, entryName, typeName);
                    L.i("parseSkinAttr", "view:" + view.getClass().getSimpleName());
                    L.i("parseSkinAttr", "attrName:" + attrName + " | attrValue:" + attrValue);
                    L.i("parseSkinAttr", "id:" + id);
                    L.i("parseSkinAttr", "entryName:" + entryName);
                    L.i("parseSkinAttr", "typeName:" + typeName);
                    if (mSkinAttr != null) {
                        viewAttrs.add(mSkinAttr);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!ListUtils.isEmpty(viewAttrs)) {
            SkinItem skinItem = new SkinItem();
            skinItem.view = view;
            skinItem.attrs = viewAttrs;
            addSkinView(skinItem);
            //如果当前皮肤来自于外部
            if (SkinManager.getInstance().isExternalSkin()) {
                skinItem.apply();
            }
        }
    }

    /**
     * 应用皮肤
     */
    public void applySkin() {
        if (ListUtils.isEmpty(mSkinItems)) {
            return;
        }

        for (SkinItem si : mSkinItems) {
            if (si.view == null) {
                continue;
            }
            si.apply();
        }
    }

    /**
     * 清除有皮肤更改需求的View及其对应的属性的集合
     */
    public void clean() {
        if (ListUtils.isEmpty(mSkinItems)) {
            return;
        }
        for (SkinItem si : mSkinItems) {
            if (si.view == null) {
                continue;
            }
            si.clean();
        }
    }

    private void addSkinView(SkinItem item) {
        mSkinItems.add(item);
    }

    /**
     * 动态添加那些有皮肤更改需求的View，及其对应的属性
     *
     * @param context        上下文
     * @param view           可变view
     * @param attrName       属性名
     * @param attrValueResId 属性资源id
     */
    public void dynamicAddSkinEnableView(Context context, View view, String attrName, int attrValueResId) {
        String entryName = context.getResources().getResourceEntryName(attrValueResId);
        String typeName = context.getResources().getResourceTypeName(attrValueResId);
        SkinAttr mSkinAttr = AttrFactoryKt.get(attrName, attrValueResId, entryName, typeName);
        SkinItem skinItem = new SkinItem();
        skinItem.view = view;
        List<SkinAttr> viewAttrs = new ArrayList<SkinAttr>();
        viewAttrs.add(mSkinAttr);
        skinItem.attrs = viewAttrs;
        skinItem.apply();
        addSkinView(skinItem);
    }

    /**
     * 动态添加那些有皮肤更改需求的View，及其对应的属性集合
     *
     * @param context
     * @param view
     * @param pDAttrs
     */
    public void dynamicAddSkinEnableView(Context context, View view, List<DynamicAttr> pDAttrs) {
        List<SkinAttr> viewAttrs = new ArrayList<SkinAttr>();
        SkinItem skinItem = new SkinItem();
        skinItem.view = view;

        for (DynamicAttr dAttr : pDAttrs) {
            int id = dAttr.refResId;
            String entryName = context.getResources().getResourceEntryName(id);
            String typeName = context.getResources().getResourceTypeName(id);
            SkinAttr mSkinAttr = AttrFactoryKt.get(dAttr.attrName, id, entryName, typeName);
            viewAttrs.add(mSkinAttr);
        }

        skinItem.attrs = viewAttrs;
        skinItem.apply();
        addSkinView(skinItem);
    }


}
