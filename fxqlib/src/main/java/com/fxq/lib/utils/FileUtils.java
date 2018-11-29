package com.fxq.lib.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author huiguo
 * @date 2018/9/7
 */
public class FileUtils {

    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static String readFileToString(Context context, String filePath) {
        if (!PackageUtils.hasPermission(context, FileUtils.READ_EXTERNAL_STORAGE)) {
            return "";
        }
        String resutStr = "";
        try {
            resutStr = readFileToString(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resutStr;
    }

    public static String readFileToString(String filePath) throws IOException {
        if (!TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            return "";
        }
        FileInputStream fin = null;
        BufferedReader reader = null;
        try {
            File fl = new File(filePath);
            fin = new FileInputStream(fl);
            reader = new BufferedReader(new InputStreamReader(fin));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return sb.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (fin != null) {
                fin.close();
            }
        }
    }

    public static void saveStringToFile(Context context, String content, String path, String name) {
        if (!PackageUtils.hasPermission(context, WRITE_EXTERNAL_STORAGE)) {
            return;
        }
        try {
            FileUtils.saveStringToFile(content, path, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveStringToFile(String content, String path, String name) throws IOException {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        OutputStream outputStream = null;
        try {
            if (!TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
                return;
            }
            if (!TextUtils.isEmpty(path)) {
                File fileDir = new File(path);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
            }
            File file = new File(path + File.separator + name);
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.flush();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    public static String getFilePath(String filePath) {
        String path = String.valueOf(Environment.getExternalStorageDirectory());
        if (!TextUtils.isEmpty(filePath)) {
            path = Environment.getExternalStorageDirectory() + File.separator + filePath;
        }
        return path;
    }
}
