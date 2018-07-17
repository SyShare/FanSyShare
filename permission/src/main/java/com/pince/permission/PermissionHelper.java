package com.pince.permission;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import rx.functions.Action1;

/**
 * 权限申请帮助类
 */
public final class PermissionHelper {
    private RxPermissions rxPermissions = null;

    public PermissionHelper(@NonNull Activity activity) {
        this.rxPermissions = new RxPermissions(activity);
    }

    public void request(@NonNull final String permission, final PermissionCallback callback) {
        rxPermissions.request(permission).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (!granted) {
                    if (callback != null) {
                        callback.onDenied(permission,
                                PermissionConstants.getTipsStr(R.string.permissions_denied_tips));
                    }
                } else {
                    if (callback != null) {
                        callback.onGranted();
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (callback != null) {
                    callback.onDenied(permission,
                            PermissionConstants.getTipsStr(R.string.permissions_denied_tips));
                }
            }
        });
    }

    public void request(@NonNull final String permission, @NonNull final String tips, final PermissionCallback callback) {
        rxPermissions.request(permission).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (!granted) {
                    if (callback != null) {
                        callback.onDenied(permission, tips);
                    }
                } else {
                    if (callback != null) {
                        callback.onGranted();
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (callback != null) {
                    callback.onDenied(permission, tips);
                }
            }
        });
    }

    public void request(@NonNull Map<String, String> permissions, PermissionCallback callback) {
        Set<Map.Entry<String, String>> entrySet = permissions.entrySet();
        requestWithTips(entrySet.iterator(), callback);
    }

    public void requestWithTips(@NonNull final Iterator<Map.Entry<String, String>> iterator
            , final PermissionCallback callback) {
        if (!iterator.hasNext()) {
            if (callback != null) {
                callback.onGranted();
            }
            return;
        }

        final Map.Entry<String, String> permission = iterator.next();
        if (rxPermissions.isGranted(permission.getKey())) {
            requestWithTips(iterator, callback);
            return;
        }
        rxPermissions.request(permission.getKey()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (!granted) {
                    if (callback != null) {
                        callback.onDenied(permission.getKey(), permission.getValue());
                    }
                } else {
                    requestWithTips(iterator, callback);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (callback != null) {
                    callback.onDenied(permission.getKey(), permission.getValue());
                }
            }
        });
    }

    public void request(@NonNull Set<String> permissions, PermissionCallback callback) {
        request(permissions.iterator(), callback);
    }

    public void request(@NonNull final Iterator<String> iterator, final PermissionCallback callback) {
        if (!iterator.hasNext()) {
            if (callback != null) {
                callback.onGranted();
            }
            return;
        }

        final String permission = iterator.next();
        if (rxPermissions.isGranted(permission)) {
            request(iterator, callback);
            return;
        }
        rxPermissions.request(permission).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (!granted) {
                    if (callback != null) {
                        callback.onDenied(permission,
                                PermissionConstants.getTipsStr(R.string.permissions_denied_tips));
                    }
                } else {
                    request(iterator, callback);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (callback != null) {
                    callback.onDenied(permission,
                            PermissionConstants.getTipsStr(R.string.permissions_denied_tips));
                }
            }
        });
    }
}
