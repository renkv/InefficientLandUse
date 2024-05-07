package com.land.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.util.Arrays;

public class FileCopyUtil {
    /**
     * 拷贝指定文件到目标文件夹
     * @param isRecursion 是否遍历子文件夹
     * @param source 源文件夹
     * @param target 目标文件夹
     * @param mateSuffixs 匹配的文件后缀,如果为空，只要不是文件夹都复制
     */
    public static void copyFile(boolean isRecursion,String source,String target,String...mateSuffixs) throws Exception {
        File sourceFile;
        if (!(sourceFile = file(source)).isDirectory()){
            throw new FileSystemException("source not directory");
        }
        File targetFile = file(target);
        //文件夹如果不存在则创建，存在则判断是否是目录
        if (targetFile.exists()){
            if (!targetFile.isDirectory()){
                throw new FileSystemException("target not directory");
            }
        }else{
            targetFile.mkdirs();
        }
        if (isRecursion){
            copyFilesRecursion(sourceFile,target,mateSuffixs);
        }else {
            copyFiles(sourceFile,target,mateSuffixs);
        }
    }

    /**
     * 创建一个File
     * @param path 路径
     */
    public static File file(String path){
        return new File(path);
    }

    /**
     * 递归查询文件
     * @param sourceDir 源文件
     * @param destDir 目标文件夹
     * @param names 匹配的后缀名
     */
    public static void copyFilesRecursion(File sourceDir,String destDir,String...names) throws IOException {
        // 遍历源文件夹下的所有文件和文件夹
        for (File file : sourceDir.listFiles()) {
            if (file.isDirectory()) { // 如果是文件夹，递归调用 copyImages 函数
                copyFilesRecursion(file, destDir,names);
            } else if (isMateFile(file,names)) { // 如果是图片文件，拷贝到目标文件夹中
                copy(file,destDir);
            }
        }
    }

    /**
     * 非递归查询文件
     * @param sourceDir 源文件
     * @param destDir 目标文件夹
     * @param names 匹配的后缀名
     */
    public static void copyFiles(File sourceDir,String destDir,String...names) throws IOException {
        // 遍历源文件夹下的所有文件和文件夹
        for (File file : sourceDir.listFiles()) {
            if (file.isFile() && isMateFile(sourceDir,names)) {
                copy(file,destDir);
            }
        }
    }

    /**
     * 拷贝文件到目标文件夹
     */
    public static void copy(File sourceFile, String destinationFolder) throws IOException {
        String destinationFilePath = destinationFolder + "/" + getFileSimpleName(sourceFile);
        File file = new File(destinationFilePath);
        if (file.exists()){
            file.delete();
        }
        Files.copy(sourceFile.toPath(), file.toPath());
    }

    /**
     * 判断文件名是否匹配
     * @param file 文件
     * @param names 需要匹配的后缀 png、doc、ppt
     * @return 如果没有填，则是文件就复制
     */
    public static boolean isMateFile(File file,String...names) {
        if (names == null || names.length == 0){
            return true;
        }
        return Arrays.asList(names).contains(getFileType(file).toLowerCase());
    }

    /**
     * 获取文件的后缀
     * @param file 文件  Z:\jiayi\postpartum\images\doduo.zip
     * @return zip
     */
    public static String getFileType(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * 获取文件名
     * @param file  Z:\jiayi\postpartum\images\doduo.zip
     * @return doudou.zip
     */
    public static String getFileSimpleName(File file){
        return  file.getName().replaceAll("[\\\\/]", "");
    }
}



