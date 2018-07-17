package com.pince.ut;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yanjibin on 8/4/16.
 */
public class DigestUtil {
    private DigestUtil() { /*DISABLE*/ }

    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex;
        }
        return ret;
    }

    public static String getMD5(String val) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return val;
        }
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        return bytes2HexString(m);
    }

}
