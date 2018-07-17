package com.pince.permission;

/**
 * 权限回调接口
 * Created by cai on 2017/7/10.
 */
public abstract class PermissionCallback {
    /**
     * 权限通过
     */
    public abstract void onGranted();

    /**
     * 权限拒绝
     */
    public void onDenied(String permission, String tips) {
    }
}
