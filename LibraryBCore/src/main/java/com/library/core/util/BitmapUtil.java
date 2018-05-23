package com.library.core.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.library.core.base.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    private BitmapUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 从本地path中获取bitmap，压缩后保存小图片到本地
     *
     * @param path     当前图片存放的路径
     * @param fileName 文件名
     * @param width    保存图片宽度
     * @param height   保存图片高度
     * @return 返回压缩后图片的存放路径
     */
    public static String saveBitmap(String path, String fileName, int width, int height) {
        String compressedPicPath = "";

        //      ★★★★★★★★★★★★★★重点★★★★★★★★★★★★★
        //★如果不压缩直接从path获取bitmap，这个bitmap会很大，下面再压缩文件到100kb时，会循环很多次，
        // ★而且会因为迟迟达不到100k，options一直在递减为负数，直接报错
        //★ 即使原图不是太大，options不会递减为负数，也会循环多次，UI会卡顿，所以不推荐不经过压缩，直接获取到bitmap
        //Bitmap bitmap = BitmapFactory.decodeFile(path);
        //      ★★★★★★★★★★★★★★重点★★★★★★★★★★★★★
        Bitmap bitmap = decodeSampledBitmapFromPath(path, BaseApplication.SCREEN_WIDTH, BaseApplication.SCREEN_HEIGHT);//缩放图片为屏幕宽高

        ByteArrayOutputStream baoS = new ByteArrayOutputStream();

        // options表示 如果不压缩是100，表示压缩率为0。如果是70，就表示压缩率是70，表示压缩30%;
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baoS);

        while (baoS.toByteArray().length / 1024 > 200) {
            // 循环判断如果压缩后图片是否大于200kb继续压缩

            baoS.reset();//重置

            options -= 10;
            if (options < 11) {//为了防止图片大小一直达不到200kb，options一直在递减，当options<0时，下面的方法会报错
                // 也就是说即使达不到200kb，也就压缩到10了
                bitmap.compress(Bitmap.CompressFormat.PNG, options, baoS);
                break;
            }
            // 这里压缩options%，把压缩后的数据存放到baoS中
            bitmap.compress(Bitmap.CompressFormat.PNG, options, baoS);
        }

        String mDir = Environment.getExternalStorageDirectory() + "/HBJ";
        File file = new File(mDir, fileName + ".png");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            fOut.write(baoS.toByteArray());
            fOut.flush();
            fOut.close();

        } catch (IOException e) {
            compressedPicPath = "";
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                LogUtil.e(e.getMessage());
            }
        }
        return compressedPicPath;
    }

    /*
     * 根据图片要显示的宽和高，对图片进行压缩，避免OOM
     *
     * @param path
     * @param width  要显示的imageView的宽度(px)
     * @param height 要显示的imageView的高度(px)
     */
    private static Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {
        //获取图片的宽和高，并不把他加载到内存当中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, width, height);
        // 使用获取到的inSampleSize再次解析图片(此时options里已经含有压缩比 options.inSampleSize，再次解析会得到压缩后的图片，不会oom了 )
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);

    }

    /*
     * 根据需求的宽和高以及图片实际的宽和高计算SampleSize
     *
     * @param options
     * @param reqWidth  要显示的imageView的宽度
     * @param reqHeight 要显示的imageView的高度
     * @compressExpand 这个值是为了像预览图片这样的需求，他要比所要显示的imageview高宽要大一点，放大才能清晰
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if (width >= reqWidth || height >= reqHeight) {

            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(width * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }

        return inSampleSize;
    }
}
