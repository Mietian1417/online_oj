package com.example.demo.component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-03-07
 * Time: 18:40
 */

// 这个类封装了读取和写入 文件的操作
public class FileUtil {
    public static String readFile(String file) {
        StringBuilder result = new StringBuilder();
        try (FileReader fileReader = new FileReader(file)) {
            while(true){
                int ch = fileReader.read();
                if(ch == -1){
                    break;
                }
                result.append((char)ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void writeFile(String file, String content){
        try(FileWriter fileWriter = new FileWriter(file)){
            fileWriter.write(content);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        FileUtil.writeFile("./a.txt", "hello world!");
//        System.out.println(FileUtil.readFile("./a.txt"));
//    }
}
