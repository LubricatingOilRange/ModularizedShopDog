package com.library.core.util;

//  Created by ruibing.han on 2018/3/27.

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SPUtil {

    //屏蔽new 创建和反射创建
    private SPUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    //-------------------------------------------file_key------------------------------
    private static final String CONFIG = "config";

    //-------------------------------------------name_key------------------------------
    public static final String SP_RECORD_TIME = "record_time";//前台或后台进程切换的时间保存
    public static final String SP_USER_COMMAND = "userCommand";//用户的保存

    /*
     * 引用数据类型的保存(对象)
     *
     * @param t
     */
    public static <T> void putObject(Context context, String key, T t) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, GSonUtil.defaultGSon().toJson(t));
        edit.apply();
    }

    /**
     * 引用数据类型的用户（对象）
     */
    public static <T> T getObject(Context context, String key, Class<T> clazz) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        String objectString = sp.getString(key, null);
        return GSonUtil.defaultGSon().fromJson(objectString, clazz);
    }

    /**
     * 基本数据类型的数据的保存到SharedPreferences
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(context, editor);
    }

    /**
     * 基本数据类型数据，从SharedPreferences获取
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            return sp.getString(key, defaultObject.toString());
        }
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(context, editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(context, editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }


    /*
     * 保存图片到SharedPreferences
     *
     * @param mContext
     * @param imageView
     */
    public static void putImage(Context mContext, String key, ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        // 将Bitmap压缩成字节数组输出流
        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byStream);
        // 利用Base64将我们的字节数组输出流转换成String
        byte[] byteArray = byStream.toByteArray();
        String imgString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        // 将String保存shareUtils
        put(mContext, key, imgString);
    }

    /*
     * 从SharedPreferences读取图片
     *
     * @param mContext
     * @param imageView
     */
    public static Bitmap getImage(Context mContext, String key, ImageView imageView) {
        String imgString = (String) SPUtil.get(mContext, key, "");
        if (!imgString.equals("")) {
            // 利用Base64将我们string转换
            byte[] byteArray = Base64.decode(imgString, Base64.DEFAULT);
            ByteArrayInputStream byStream = new ByteArrayInputStream(byteArray);
            // 生成bitmap
            return BitmapFactory.decodeStream(byStream);
        }
        return null;
    }

    /*
     * 由于editor.commit方法是同步的，并且我们很多时候的commit操作都是UI线程中，毕竟是IO操作，所以尽可能采用异步操作；
     * 所以我们使用apply进行替代，apply异步的进行写入
     *
     * @author geek_xz
     * @version 1.0
     * @time 2015-8-26 上午10:04:06
     *
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            Class clz = SharedPreferences.Editor.class;
            try {
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                LogUtil.e(e.getMessage());
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(Context context, SharedPreferences.Editor editor) {
            if (sApplyMethod != null) {
                try {
                    sApplyMethod.invoke(editor);
                } catch (IllegalAccessException e) {
                    LogUtil.e(e.getMessage());
                } catch (InvocationTargetException e) {
                    LogUtil.e(e.getMessage());
                }
                return;
            }
            editor.commit();
        }
    }
}
