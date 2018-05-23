package com.library.common.module.http.helper;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import it.sauronsoftware.base64.Base64;

public class En_DecryptionHelper {

    //AES/CBC/NoPadding
    //AES/ECB/PKCS5Padding
    private static final String algorithmStr = "AES/ECB/PKCS5Padding";


    //注意: 这里的password(秘钥必须是16位的)
    private static final String AES_KEY = "xhf6Z6I3JePpm8pgYa5m6w==";

    private static En_DecryptionHelper instance;

    private En_DecryptionHelper() {

    }

    public static En_DecryptionHelper getInstance() {
        if (instance == null) {
            synchronized (En_DecryptionHelper.class) {
                if (instance == null) {
                    instance = new En_DecryptionHelper();
                }
            }
        }
        return instance;
    }

    // 排序-升序（不区分大小写）
    class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String s, String t1) {
            return s.toLowerCase().compareTo(t1.toLowerCase());
        }
    }
    //----------------------------------------------参数加密--------------------------------------------

    private Map<String, String> mSortMap = new TreeMap<>(new MapKeyComparator());
    private List<String> mValueParams = new ArrayList<>();
    private List<String> mKeyParams = new ArrayList<>();

    /*
     * 将键值分开加密
     * @param map
     * @return
     * @throws Exception
     */
    public synchronized Map<String, String> sortMapByKey(Map<String, String> map) throws Exception {
        if (map == null || map.isEmpty()) {
            return null;
        }
        mSortMap.clear();
        mSortMap.putAll(map);//添加排序

        mValueParams.clear();
        mKeyParams.clear();

        for (Map.Entry<String, String> entry : mSortMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            mValueParams.add(entry.getKey());
            mKeyParams.add(value);
            if (key.equals("image")) mSortMap.put(key, value);//图片处理 不加密
            else
                mSortMap.put(key, encode(entry.getValue() == null ? "" : entry.getValue()));//AES加密


        }
        mSortMap.put("sign", TestJoint(mValueParams, mKeyParams));
        return mSortMap;
    }

    /*
     * 加密后重新组合 并重新排序
     * @param key
     * @param value
     * @return
     */
    private Map<String, String> mSingMap = new TreeMap<>(new MapKeyComparator());

    private String TestJoint(List<String> key, List<String> value) {
        mSingMap.clear();

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < key.size(); i++) {
            mSingMap.put(key.get(i), value.get(i) == null ? "" : value.get(i));
        }
        mSingMap.put("seamless_api", "xhf6Z6I3JePpm8pgYa5m6w==");
        for (Map.Entry<String, String> entry : mSingMap.entrySet()) {
            str.append(entry.getKey()).append("=").append(entry.getValue());
            str.append("&");
        }

        String Signature = str.deleteCharAt(str.length() - 1).toString();

        try {
            return new String(Base64.encode(getSHAData(Signature)));//二进制数据编码为BASE64字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private byte[] getSHAData(String result) {
        // 返回值
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(result.getBytes());
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    //---------------------------------------------------数据解密处理------------------------------
    public String decode(@NonNull String content) {
        //解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
        return new String(decrypt(parseHexStr2Byte(content), Str2MD516()));
    }

    /*
     * 将16进制转换为二进制(进行的base64编码)
     *
     * @param hexStr
     * @return
     */
    private @NonNull
    byte[] parseHexStr2Byte(@NonNull String hexStr) {
        try {

            return Base64.decode(hexStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /*
     * 解密处理
     * @param content
     * @param password
     * @return
     */
    @SuppressLint("GetInstance")
    private @NonNull
    byte[] decrypt(@NonNull byte[] content, @NonNull String password) {
        try {
            byte[] keyStr = getKeyByte(password);
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//algorithmStr
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    //---------------------------------------------------数据解密处理------------------------------

    /**
     * 加密
     */
    public String encode(String content) {
        //加密之后的字节数组,转成16进制的字符串形式输出
        //MD5
        return parseByte2HexStr(encrypt(content, Str2MD516()));
    }

    /*
     * 将二进制转换成16进制(进行的base64编码)
     *
     * @param buf
     * @return
     */
    private String parseByte2HexStr(byte buf[]) {
        String password = null;
        try {
            password = new String(Base64.encode(buf));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    @SuppressLint("GetInstance")
    private byte[] encrypt(String content, String password) {
        try {
            byte[] keyStr = getKeyByte(password);
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);//
            return cipher.doFinal(byteContent); //
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getKeyByte(String password) {
        byte[] rByte;
        if (password != null) {
            rByte = password.getBytes();
        } else {
            rByte = new byte[24];
        }
        return rByte;
    }

    String Str2MD516() {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(AES_KEY.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }
}
