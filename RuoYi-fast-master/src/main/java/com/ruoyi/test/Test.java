package com.ruoyi.test;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.documents.TextSelection;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.general.find.PdfTextFind;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static com.spire.xls.OleObjectType.WordDocument;

public class Test {
    public static void main(String[] args) throws IOException {

        String url="D:/ruoyi/uploadPath/upload/2022/05/20/1.docx";
        //System.out.println(url.substring(url.lastIndexOf('.')));
        wordSave(url);
        /*Path path = new File("C:\\Users\\Admin\\Desktop\\4.docx").toPath();
        String type = Files.probeContentType(path);
        System.out.println(type);*/

    }


    public static final HashMap<String, String> mFileTypes = new HashMap<String, String>();

    static {
        // images
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E47", "png");
        mFileTypes.put("47494638", "gif");
        mFileTypes.put("49492A00", "tif");
        mFileTypes.put("424D", "bmp");
        /*CAD*/
        mFileTypes.put("41433130", "dwg");
        mFileTypes.put("38425053", "psd");
        /* 日记本 */
        mFileTypes.put("7B5C727466", "rtf");
        mFileTypes.put("3C3F786D6C", "xml");
        mFileTypes.put("68746D6C3E", "html");
        // 邮件
        mFileTypes.put("44656C69766572792D646174653A", "eml");
        mFileTypes.put("D0CF11E0", "doc");
        //excel2003版本文件
        mFileTypes.put("D0CF11E0", "xls");
        mFileTypes.put("5374616E64617264204A", "mdb");
        mFileTypes.put("252150532D41646F6265", "ps");
        mFileTypes.put("255044462D312E", "pdf");
        mFileTypes.put("504B0304", "docx");
        //excel2007以上版本文件
        mFileTypes.put("504B0304", "xlsx");
        mFileTypes.put("52617221", "rar");
        mFileTypes.put("57415645", "wav");
        mFileTypes.put("41564920", "avi");
        mFileTypes.put("2E524D46", "rm");
        mFileTypes.put("000001BA", "mpg");
        mFileTypes.put("000001B3", "mpg");
        mFileTypes.put("6D6F6F76", "mov");
        mFileTypes.put("3026B2758E66CF11", "asf");
        mFileTypes.put("4D546864", "mid");
        mFileTypes.put("1F8B08", "gz");
    }

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


    /**
     * 根据地址获得数据的输入流
     * @param strUrl 网络连接地址
     * @return url的输入流
     */
    public static InputStream getInputStreamByUrl(String strUrl){
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(),output);
            return  new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try{
                if (conn != null) {
                    conn.disconnect();
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    /**
     * <p>Title:getFileHeaderByFileInputStream </p>
     * <p>Description: 根据文件流获取文件头信息</p>
     *
     * @param is 文件流
     * @return 十六进制文件头信息
     */
    private static String getFileHeaderByFileInputStream(InputStream is) {
        String value = null;
        try {
            byte[] b = new byte[4];
            /*
             * int read() 从此输入流中读取一个数据字节。int read(byte[] b) 从此输入流中将最多 b.length
             * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
             * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
             */
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }

    /**
     * <p>Title:bytesToHexString </p>
     * <p>Description: 将要读取文件头信息的文件的byte数组转换成string类型表示 </p>
     *
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头信息
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        //System.out.println(builder.toString());
        return builder.toString();
    }

}
