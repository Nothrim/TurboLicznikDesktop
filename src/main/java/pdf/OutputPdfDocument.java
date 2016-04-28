package pdf;

import core.Controller;
import gui.Record;
import gui.Time;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class OutputPdfDocument {
    static final int PAGE_TOP=775;
    public OutputPdfDocument(VBox data, String filename) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        int currentX = 0;
        int currentY = PAGE_TOP;
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(font, 8);
        contentStream.setNonStrokingColor(80,80,80);
        contentStream.addRect(0,0,PAGE_TOP,PAGE_TOP+35);
        contentStream.fill();
            contentStream.setStrokingColor(0,0,0);
            for(Node n:data.getChildren()) {
                if(n instanceof Record) {
                    contentStream.addRect(currentX, currentY, 15, 20);
                    contentStream.setNonStrokingColor(0, 114, 160);
                    contentStream.fill();
//                    contentStream.addRect(currentX - 1, currentY - 1, 15 + 1, 20 + 1);
//                    contentStream.stroke();
                    currentX += 15;
                    for (TextField l : ((Record) n).fields) {
                        contentStream.addRect(currentX, currentY, 45, 20);
                        contentStream.setNonStrokingColor(6, 88, 120);
                        contentStream.fill();
//                        contentStream.addRect(currentX - 1, currentY - 1, 45 + 1, 20 + 1);
//                        contentStream.setStrokingColor(0, 0, 0);
//                        contentStream.stroke();
                        currentX += 55;
                    }
                    currentX = 0;
                    currentY -= 20;
                }
            }
            contentStream.addRect(currentX+1,currentY-30,35,20);
            contentStream.fill();
//            contentStream.addRect(currentX , currentY - 31, 35, 20 + 1);
//            contentStream.stroke();
            contentStream.addRect(currentX+30+35,currentY-30,55,20);
            contentStream.fill();
//            contentStream.addRect(currentX + 31+35, currentY - 31, 55, 20 + 1);
//            contentStream.stroke();
            contentStream.beginText();
            contentStream.setNonStrokingColor(240,240,240);
            currentX=5;
            currentY=PAGE_TOP;
            contentStream.moveTextPositionByAmount(3,PAGE_TOP+5);
            for(Node n:data.getChildren()) {
                if(n instanceof Record) {
                    contentStream.drawString(((Record) n).tDate.getText().substring(0, 2));
                    contentStream.moveTextPositionByAmount(15, 0);
                    currentX += 15;
                    for (TextField l : ((Record) n).fields) {
                        contentStream.drawString(new Time(Long.parseLong(l.getText())).toPdfRow());
                        contentStream.moveTextPositionByAmount(55, 0);
                        currentX += 55;
                    }
                    contentStream.moveTextPositionByAmount(-currentX+5, -20);
                    currentX = 5;
                    currentY -= 20;
                }}

            contentStream.moveTextPositionByAmount(0,-30);
            contentStream.drawString("RAZEM:");
            contentStream.moveTextPositionByAmount(65,0);
            contentStream.drawString(new Time(Controller.pointer.getSum()).toPdfRow());
            contentStream.endText();
            contentStream.close();
            document.save(filename);
    }
}
