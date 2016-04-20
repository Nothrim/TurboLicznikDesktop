package pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class OutputPdfDocument {
    public OutputPdfDocument(Map<String,List<Long>> data,String filename) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        int currentX = 0;
        int currentY = 0;
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(font, 14);
            for(Map.Entry<String,List<Long>>entry:data.entrySet()) {
                contentStream.addRect(currentX, currentY, 85, 40);
                currentX += 60;
                for(Long l:entry.getValue()) {
                    contentStream.addRect(currentX, currentY, 110, 40);
                    currentX+=110;
                }
                currentX=0;
                currentY+=40;
            }
            contentStream.addRect(currentX,currentY,100,40);
            contentStream.close();
            document.save(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
