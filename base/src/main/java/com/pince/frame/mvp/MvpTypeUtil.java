package com.pince.frame.mvp;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by athou on 2017/7/10.
 */

class MvpTypeUtil {

    public static Class findParamsTypeClass(Class cls) {
        if (cls.equals(FinalMvpActivity.class)) {
            return null;
        }
        if (cls.equals(FinalMvpFragment.class)) {
            return null;
        }
        if (cls.equals(Object.class)) {
            return null;
        }
        Class findClass = getMethodClass(cls);
        if (findClass != null) {
            return findClass;
        }
        return findParamsTypeClass(cls.getSuperclass());
    }

    public static Class getMethodClass(Class cls) {
        Type typeOri = cls.getGenericSuperclass();
        // if Type is T
        if (typeOri instanceof ParameterizedType) {
            Type[] parentypes = ((ParameterizedType) typeOri).getActualTypeArguments();
            for (Type childtype : parentypes) {
                if (childtype instanceof Class && FinalMvpPresenter.class.isAssignableFrom((Class<?>) childtype)) {
                    if (childtype.equals(FinalMvpPresenter.class)) {
                        return null;
                    }
                    return (Class) childtype;
                }
            }
        }
        return null;
    }

//
//    private Class findParamsTypeClass(Class cls) {
//        if (cls.equals(FinalMvpActivity.class)) {
//            return null;
//        }
//        Class findClass = getMethodClass(cls);
//        if (findClass != null) {
//            return findClass;
//        }
//        return findParamsTypeClass(cls.getSuperclass());
//    }
//
//    private Class getMethodClass(Class cls) {
//        Type typeOri = cls.getGenericSuperclass();
//        // if Type is T
//        if (typeOri instanceof ParameterizedType) {
//            Type[] parentypes = ((ParameterizedType) typeOri).getActualTypeArguments();
//            for (Type childtype : parentypes) {
//                if (childtype instanceof Class && FinalMvpPresenter.class.isAssignableFrom((Class<?>) childtype)) {
//                    return (Class) childtype;
//                }
//            }
//        }
//        return null;
//    }
}
