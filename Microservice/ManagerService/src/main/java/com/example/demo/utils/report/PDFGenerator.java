package com.example.demo.utils.report;

import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.Payment;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Getter
@Setter
public class PDFGenerator {

    // List to hold all item
    private List<ItemDTO> items;
    private List<Payment> payments;

    public void generate(HttpServletResponse response) throws DocumentException, IOException {

        // Creating the Object of Document
        Document document = new Document(PageSize.A4);

        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());

        // Opening the created document to modify it
        document.open();

        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);

        // Creating paragraph
        Paragraph paragraph = new Paragraph("List Of items", fontTiltle);

        // Aligning the paragraph in document
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        // Adding the created paragraph in document
        document.add(paragraph);

        // Creating a table of 4 columns
        PdfPTable table = new PdfPTable(4);

        // Setting width of table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3, 3, 3});
        table.setSpacingBefore(5);

        // Create Table Cells for table header
        PdfPCell cell = new PdfPCell();

        // Setting the background color and padding
        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);

        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        // Adding headings in the created table cell/ header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Item Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Price", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);

        // Iterating over the list of items
        for (ItemDTO item : items) {
            // Adding item id
            table.addCell(String.valueOf(item.getId()));
            // Adding item name
            table.addCell(item.getName());
            // Adding item price
            table.addCell(String.valueOf(item.getPrice()));
            //Adding quantity
            table.addCell(String.valueOf(item.getQuantity()));

        }
        // Adding the created table to document
        document.add(table);

        // Closing the document
        document.close();
    }

    public void generateWeeklyReport(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        Paragraph paragraph = new Paragraph("List Of items", fontTiltle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3, 3, 3, 3, 3, 3});
        table.setSpacingBefore(5);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Store User", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Order ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Money Paid", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Money Unpaid", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Payment Date", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Complete", font));
        table.addCell(cell);
        for (Payment payment : getPayments()) {
            table.addCell(String.valueOf(payment.getId()));
            table.addCell(payment.getStoreUser());
            table.addCell(String.valueOf(payment.getAccumulatedMoney()));
            table.addCell(String.valueOf(payment.getMoneyUnpaid()));
            table.addCell(String.valueOf(payment.getPaymentDate()));

        }
        document.add(table);
        document.close();
    }


}
