package com.land.utils;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;

public class DocToPdfUtil {

    public static void main(String[] args) {
        //实例化Document类的对象
        Document doc = new Document();
        //加载Word
        doc.loadFromFile("E:\\tmp\\2024-05-06\\1787366008369860609.docx");
        //保存为PDF格式
        doc.saveToFile("E:\\tmp\\2024-05-06\\result.pdf", FileFormat.PDF);
    }
}
