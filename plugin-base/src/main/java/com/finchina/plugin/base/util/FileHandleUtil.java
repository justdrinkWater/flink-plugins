package com.finchina.plugin.base.util;

import cn.hutool.core.io.IoUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: renjianfei
 * @Date: 2020/6/1 14:24
 * @Description:
 */
public class FileHandleUtil {

    private FileHandleUtil(){

    }

    // 遍历查找所有的文件装到集合里面去
    public static List<File> getFileList(String strPath,List<File> filelist) {

        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                //是文件夹的话就是要递归再深入查找文件
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath(),filelist); // 获取文件绝对路径
                } else {
                    //如果是文件，直接添加到集合
                    filelist.add(files[i]);
                }
            }
        }
        return filelist;
    }

    protected static final Map<String,String> fileTypes = new HashMap<>();
    static
    {
        fileTypes.put("d0cf11e0", "doc");    //xls、wps、vsd
        fileTypes.put("504b0304", "docx");   //xlsx、zip、jar
        fileTypes.put("44656C69766572792D64", "eml");
        fileTypes.put("ffd8ffe000104a464946", "jpg");
        fileTypes.put("89504e470d0a1a0a0000", "png");
        fileTypes.put("47494638396126026f01", "gif");
        fileTypes.put("49492a00227105008037", "tif");
        fileTypes.put("424d228c010000000000", "bmp");
        fileTypes.put("424d8240090000000000", "bmp");
        fileTypes.put("424d8e1b030000000000", "bmp");
        fileTypes.put("41433130313500000000", "dwg");
        fileTypes.put("3C21444F4", "html");
        fileTypes.put("3C68746D6C", "html");
        fileTypes.put("68746D6C3E", "html");
        fileTypes.put("3c21646f637479706520", "htm");
        fileTypes.put("48544d4c207b0d0a0942", "css");
        fileTypes.put("696b2e71623d696b2e71", "js");
        fileTypes.put("7b5c727466315c61", "rtf");
        fileTypes.put("38425053000100000000", "psd");
        fileTypes.put("46726f6d3a203d3f6762", "eml");
        fileTypes.put("5374616E64617264204A", "mdb");
        fileTypes.put("252150532D41646F6265", "ps");
        fileTypes.put("255044462d312e350d0a", "pdf");
        fileTypes.put("2e524d46000000120001", "rmvb");
        fileTypes.put("464c5601050000000900", "flv");
        fileTypes.put("00000020667479706d70", "mp4");
        fileTypes.put("49443303000000002176", "mp3");
        fileTypes.put("000001ba210001000180", "mpg");
        fileTypes.put("3026b2758e66cf11a6d9", "wmv");
        fileTypes.put("52494646e27807005741", "wav");
        fileTypes.put("52494646d07d60074156", "avi");
        fileTypes.put("4d546864000000060001", "mid");
        fileTypes.put("526172211a0700cf9073", "rar");
        fileTypes.put("235468697320636f6e66", "ini");
        fileTypes.put("4d5a9000030000000400", "exe");
        fileTypes.put("3c25402070616765206c", "jsp");
        fileTypes.put("4d616e69666573742d56", "mf");
        fileTypes.put("EFBBBF3C3F786D6C", "xml");
        fileTypes.put("3c3f786d6c2076657273", "xml");
        fileTypes.put("494e5345525420494e54", "sql");
        fileTypes.put("7061636b616765207765", "java");
        fileTypes.put("406563686f206f66660d", "bat");
        fileTypes.put("1f8b0800000000000000", "gz");
        fileTypes.put("6c6f67346a2e726f6f74", "properties");
        fileTypes.put("cafebabe0000002e0041", "class");
        fileTypes.put("49545346030000006000", "chm");
        fileTypes.put("04000000010000001300", "mxp");
        fileTypes.put("6431303a637265617465", "torrent");
    }


    /// <summary>
    /// 获取文件后缀名
    /// </summary>
    /// <param name="b"></param>
    /// <returns></returns>
    public static String getFileTypeByStream(byte[] b)  {

        String start = bytesToHexString(b);

        String filetype ="UNKNOWN";
        if(StringUtils.isNotEmpty(start)){
            for (Map.Entry<String,String> node : fileTypes.entrySet()) {
                if(start.startsWith(node.getKey())){
                    filetype=node.getValue();
                }
            }
        }


        return filetype;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    public static String saveFileToLocal(String dirPath, String relativePath, InputStream fileInputStream) {

        String filePath = dirPath + relativePath;

        File f = new File(filePath);
        File parentFile = f.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)
        ) {
            IoUtil.copy(fileInputStream, bufferedOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return filePath;
    }


    public static String saveFileToLocal(String filePath,  InputStream fileInputStream) {
        File f = new File(filePath);
        File parentFile = f.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)
        ) {
            IoUtil.copy(fileInputStream, bufferedOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return filePath;
    }
}
