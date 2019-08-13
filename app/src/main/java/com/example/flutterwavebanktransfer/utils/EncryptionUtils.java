package com.example.flutterwavebanktransfer.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public class EncryptionUtils {
    //The `getEncryptedData` method needs to be called first to clean the secret key, all other methods are used inside it.

    private static String encrypt(String data, String key) throws Exception {
        byte[] keyBytes = key.getBytes(Constants.UTF_8);
        SecretKeySpec skey = new SecretKeySpec(keyBytes,Constants.ALGORITHM);
        Cipher cipher = Cipher.getInstance(Constants.TRANSFORMATION);

        cipher.init(Cipher.ENCRYPT_MODE, skey);
        byte[] plainTextBytes = data.getBytes(Constants.UTF_8);
        byte[] buf = cipher.doFinal(plainTextBytes);
        return Base64.encodeToString(buf, Base64.DEFAULT);

    }

    private static String getMd5(String md5) throws Exception {
        MessageDigest md = MessageDigest.getInstance(Constants.MD5);
        byte[] array = md.digest(md5.getBytes(Charset.defaultCharset()));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static String getEncryptedData(String unEncryptedString, String secret) {
        try {
            // hash the secret
            String md5Hash = getMd5(secret);
            String cleanSecret = secret.replace(Constants.TARGET, "");
            int hashLength = md5Hash.length();
            String encryptionKey = cleanSecret.substring(0, 12).concat(md5Hash.substring(hashLength - 12, hashLength));

            return encrypt(unEncryptedString, encryptionKey);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
