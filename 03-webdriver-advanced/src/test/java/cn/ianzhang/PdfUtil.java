package cn.ianzhang;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 例程3.20  PDF工具类
 */
public class PdfUtil {
  public static void generatePdfFileWithPictures(File pdfFile, Map<String, File> picMap) throws IOException {
    Document document = new Document();
    PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
    try {
      document.open();
      addImage(document, picMap);
    } finally {
      if (document != null) document.close();
      if (pdfWriter != null) pdfWriter.close();
    }
  }

  public static void addImage(Document document, Map<String, File> picMap) throws IOException {
    for (Map.Entry<String, File> entry : picMap.entrySet()) {
      String paragraph = entry.getKey();
      File picFile = entry.getValue();
      document.add(new Paragraph(paragraph));
      Image image = Image.getInstance(picFile.getAbsolutePath());
      image.scaleAbsolute(500, 300);
      document.add(image);
    }
  }
}
