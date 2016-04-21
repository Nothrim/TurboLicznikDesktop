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
import java.util.List;
import java.util.Map;

public class OutputPdfDocument {
    static final int PAGE_TOP=740;
    public OutputPdfDocument(VBox data, String filename) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        int currentX = 0;
        int currentY = PAGE_TOP;
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(font, 14);
            contentStream.setStrokingColor(0,0,0);
            for(Node n:data.getChildren()) {
                if(n instanceof Record) {
                    contentStream.addRect(currentX, currentY, 30, 40);
                    contentStream.setNonStrokingColor(190, 245, 190);
                    contentStream.fill();
                    contentStream.addRect(currentX - 1, currentY - 1, 30 + 1, 40 + 1);
                    contentStream.stroke();
                    currentX += 40;
                    for (TextField l : ((Record) n).fields) {
                        contentStream.addRect(currentX, currentY, 100, 40);
                        contentStream.setNonStrokingColor(220, 255, 220);
                        contentStream.fill();
                        contentStream.addRect(currentX - 1, currentY - 1, 100 + 1, 40 + 1);
                        contentStream.setStrokingColor(0, 0, 0);
                        contentStream.stroke();
                        currentX += 110;
                    }
                    currentX = 0;
                    currentY -= 40;
                }
            }
            contentStream.addRect(currentX+1,currentY-30,100,40);
            contentStream.fill();
            contentStream.addRect(currentX , currentY - 31, 100, 40 + 1);
            contentStream.stroke();
            contentStream.addRect(currentX+30+55,currentY-30,100,40);
            contentStream.fill();
            contentStream.addRect(currentX + 31+55, currentY - 31, 100, 40 + 1);
            contentStream.stroke();
            contentStream.beginText();
            contentStream.setNonStrokingColor(0,0,0);
            currentX=5;
            currentY=PAGE_TOP+15;
            contentStream.moveTextPositionByAmount(5,PAGE_TOP+15);
            for(Node n:data.getChildren()) {
                if(n instanceof Record) {
                    contentStream.drawString(((Record) n).tDate.getText().substring(0, 2));
                    contentStream.moveTextPositionByAmount(40, 0);
                    currentX += 40;
                    for (TextField l : ((Record) n).fields) {
                        contentStream.drawString(new Time(Long.parseLong(l.getText())).toPdfRow());
                        contentStream.moveTextPositionByAmount(110, 0);
                        currentX += 110;
                    }
                    contentStream.moveTextPositionByAmount(-currentX + 5, -40);
                    currentX = 10;
                    currentY -= 40;
                }}

            contentStream.moveTextPositionByAmount(10,-30);
            contentStream.drawString("RAZEM:");
            contentStream.moveTextPositionByAmount(30+55,0);
            contentStream.drawString(new Time(Controller.pointer.getSum()).toPdfRow());
            contentStream.endText();
            contentStream.close();
            document.save(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
