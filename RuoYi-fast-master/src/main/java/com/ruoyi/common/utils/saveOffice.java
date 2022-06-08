package com.ruoyi.common.utils;

import com.spire.doc.Document;
import com.spire.doc.documents.TextSelection;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.general.find.PdfTextFind;

import java.awt.*;

public class saveOffice {
    public static String getText(){
        PdfDocument pdf = new PdfDocument();
        pdf.loadFromFile("C:\\Users\\Admin\\Desktop\\个人作业\\个人简历-张智.pdf");
        PdfTextFind[] result = null;
        //遍历文档页面
        StringBuilder sb = new StringBuilder();
        for (PdfPageBase page : (Iterable<PdfPageBase>) pdf.getPages()) {
            sb.append(page.extractText(false));

        }
        pdf.close();
        return sb.toString();
    }
    public static void pdfSave(String url){
        PdfDocument pdf = new PdfDocument();
        pdf.loadFromFile(url);
        PdfTextFind[] result = null;
        //遍历文档页面
        for (PdfPageBase page : (Iterable<PdfPageBase>) pdf.getPages()) {
            //查找文档中所有的"添加"字符串
            result = page.findText("主要").getFinds();
            for (PdfTextFind find : result) {
                //高亮显示查找结果
                find.applyHighLight(Color.yellow);
            }

        }
        pdf.saveToFile(url);
        pdf.close();
    }
    public static void wordSave(String url){
        Document doc = new Document();

        doc.loadFromFile(url);
        TextSelection[] text = doc.findAllString("财务", false, false);

        for (TextSelection t:text
        ) {
            t.getAsOneRange().getCharacterFormat().setHighlightColor(Color.yellow);
        }

        doc.saveToFile(url);
        doc.close();
        System.out.println("word保存成功");
    }
}
