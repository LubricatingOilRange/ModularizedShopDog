package com.library.core.util;

//  Created by ruibing.han on 2018/3/27.

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileUtil {
    public static final String CACHE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "ShopDog";

    //屏蔽new 创建和反射创建
    private FileUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 检查是否已挂载SD卡镜像（是否存在SD卡）
     */
    public static boolean isMountedSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            LogUtil.e("SDCARD is not MOUNTED !");
            return false;
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     */
    public static Boolean isExist(String filePath) {
        Boolean flag = false;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                flag = true;
            }
        } catch (Exception e) {
            LogUtil.e("判断文件失败-->" + e.getMessage());
        }

        return flag;
    }

    /*
     * 创建目录
     *
     * @param dirName 文件夹名称
     * @return
     */
    public static File createFileDir(Context context, String dirName) {
        String filePath;
        // 如SD卡已存在，则存储；反之存在data目录下
        if (isMountedSDCard()) {
            // SD卡路径
            filePath = Environment.getExternalStorageDirectory() + File.separator + dirName;
        } else {
            filePath = context.getCacheDir().getPath() + File.separator + dirName;
        }
        File destDir = new File(filePath);
        if (!destDir.exists()) {
            boolean isCreate = destDir.mkdirs();
            LogUtil.i("FileUtils", dirName + " 文件夹创建结果:" + (isCreate ? "成功" : "失败"));
        }
        return destDir;
    }

    /*
     * 删除文件或文件夹（若为文件夹，则递归删除子目录和文件） -- 如果文件比较多，最好开线程进行删除
     *
     * @param file ()
     * @param delThisPath true代表删除参数指定file，false代表保留参数指定file
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        //若当前为文件夹
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null && subFiles.length > 0) {
                // 删除子目录和文件
                for (File subFile : subFiles) {
                    deleteFile(subFile);
                }
            } else {
                boolean delete = file.delete();
                LogUtil.i("FileUtils", file.getName() + " 文件夹删除" + (delete ? "成功" : "失败"));
            }
        } else if (file.isFile()) {
            boolean delete = file.delete();
            LogUtil.i("FileUtils", file.getName() + " 文件删除" + (delete ? "成功" : "失败"));
        }
    }

    /*
     * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
     *
     * @param file
     * @return
     */
    private static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        size += getFileSize(subFile);
                    }
                }
            } else {
                size += file.length();
            }
        }
        return size;
    }

    //------------------------------------------------------读取文件-----------------------------------------------

    /**
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param filePath 文件路径
     */
    public static String readFileByLines(String filePath) throws IOException {
        return readFileByLines(filePath, System.getProperty("file.encoding"));
    }

    /**
     * @param filePath 文件路径
     * @param encoding 写文件编码
     */
    private static String readFileByLines(String filePath, String encoding) throws IOException {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(reader);
        }
        return sb.toString();
    }

    /**
     * 读取指定目录文件的文件内容
     *
     * @param fileName 文件名称
     */
    @SuppressWarnings("resource")
    public static String readFile(String fileName) throws IOException {

        FileInputStream inStream = new FileInputStream(fileName);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.flush();
        closeIO(inStream, outStream);

        byte[] data = outStream.toByteArray();


        return new String(data);
    }

    /**
     * 读取raw目录的文件内容
     *
     * @param context   内容上下文
     * @param rawFileId raw文件名id (R.raw.xxx)
     */
    public static String readRawFile(Context context, int rawFileId) {
        String result = "";
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(rawFileId);
            int len = is.available();
            byte[] buffer = new byte[len];
            int read = is.read(buffer);
            if (read == -1) {
                result = new String(buffer, "UTF-8");
            } else {
                LogUtil.i("FileUtils", "未读全Raw下的文件");
            }
        } catch (Exception e) {
            LogUtil.i("FileUtils", "读取Raw下的文件失败，原因" + e.getMessage());
        } finally {
            closeIO(is);
        }
        return result;
    }

    /**
     * 读取assets目录的文件内容
     *
     * @param context  内容上下文
     * @param fileName 文件名称，包含扩展名(joke.txt   joke/joke.text   joke.png)
     * @return 返回utf-8 String
     */
    public static String readAssetsFile(Context context, String fileName) {
        String result = "";
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(fileName);
            int len = is.available();
            byte[] buffer = new byte[len];
            if (is.read(buffer) == -1) {
                result = new String(buffer, "UTF-8");
            } else {
                LogUtil.i("FileUtils", "未读全Assets目录下文件" + fileName);
            }
        } catch (Exception e) {
            LogUtil.i("FileUtils", "读取Assets下的文件失败，原因" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeIO(is);
        }
        return result;
    }

    //------------------------------------------------------写入文件-----------------------------------------------

    /**
     * 写入文件
     *
     * @param inputStream 下载文件的字节流对象
     * @param filePath    文件的存放路径 (带文件名称)
     * @return isWrite 是否写入成功
     */
    public static boolean write(InputStream inputStream, String filePath) {
        boolean isWrite = false;
        OutputStream outputStream = null;
        // 在指定目录创建一个空文件并获取文件对象
        File mFile = new File(filePath);
        try {
            outputStream = new FileOutputStream(mFile);
            byte buffer[] = new byte[4 * 1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            isWrite = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(inputStream, outputStream);
        }
        return isWrite;
    }

    /**
     * 指定编码将内容写入目标文件
     *
     * @param target   目标文件
     * @param content  文件内容
     * @param encoding 写入文件编码
     * @return isWrite 是否写入成功
     */
    public static boolean write(File target, String content, String encoding) {
        boolean isWrite = false;
        BufferedWriter writer = null;
        try {
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(target, false), encoding));
                writer.write(content);
                isWrite = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            closeIO(writer);
        }
        return isWrite;
    }

    /**
     * 指定目录保存图片
     *
     * @param filePath 文件路径+文件名
     * @param bitmap   文件内容
     * @return isSave 是否保存成功
     */
    public static boolean saveAsPNG(Bitmap bitmap, String filePath) {
        boolean isSave = false;
        FileOutputStream fos = null;
        File file = new File(filePath);
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            isSave = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(fos);
        }
        return isSave;
    }


    /*
     * 快速复制
     *
     * @param in
     * @param out
     */
    public static void copyFileFast(File in, File out) {
        FileChannel fileIn = null;
        FileChannel fileOut = null;
        try {
            fileIn = new FileInputStream(in).getChannel();
            fileOut = new FileOutputStream(out).getChannel();
            fileIn.transferTo(0, fileIn.size(), fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * 获取文件夹下所有文件
     *
     * @param path
     * @return
     */
    public static ArrayList<File> getFilesArray(String path) {
        File file = new File(path);
        File files[] = file.listFiles();
        ArrayList<File> listFile = new ArrayList<File>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    listFile.add(files[i]);
                }
                if (files[i].isDirectory()) {
                    listFile.addAll(getFilesArray(files[i].toString()));
                }
            }
        }
        return listFile;
    }

    /*
     * 分享文件
     *
     * @param context
     * @param title
     * @param filePath
     */
    public static void shareFile(Context context, String title, String filePath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = Uri.parse("file://" + filePath);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }

    /*
     * 对os 进行压缩后  保存到文件中
     * @param is
     * @param os
     */
    public static void zip(InputStream is, OutputStream os) {
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(os);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                gzip.write(buf, 0, len);
                gzip.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(is, gzip);
        }
    }

    /*
     * 解压某个文件 到 os中
     *
     * @param is
     * @param os
     */
    public static void unZip(InputStream is, OutputStream os) {
        GZIPInputStream gzip = null;
        try {
            gzip = new GZIPInputStream(is);
            byte[] buf = new byte[1024];
            int len;
            while ((len = gzip.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(gzip, os);
        }
    }

    /*
     * 打开图片
     *
     * @param mContext
     * @param imagePath
     */
    public static void openImage(Context mContext, String imagePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(imagePath));
        intent.setDataAndType(uri, "image/*");
        mContext.startActivity(intent);
    }

    /*
     * 打开视频
     *
     * @param mContext
     * @param videoPath
     */
    public static void openVideo(Context mContext, String videoPath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(videoPath));
        intent.setDataAndType(uri, "video/*");
        mContext.startActivity(intent);
    }

    /*
     * 打开URL
     *
     * @param mContext
     * @param url
     */
    public static void openURL(Context mContext, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }

    /*
     * 系统下载文件
     *
     * @param context
     * @param fileurl
     */
    public static void downloadFile(Context context, String fileurl) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileurl));
        request.setDestinationInExternalPublicDir("/Download/", fileurl.substring(fileurl.lastIndexOf("/") + 1));
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }

    //关流
    public static void closeIO(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                    closeable = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
