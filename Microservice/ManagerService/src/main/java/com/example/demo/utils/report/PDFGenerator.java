package com.example.demo.utils.report;

import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.Payment;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PDFGenerator {

    // List to hold all item
    private List<ItemDTO> items;
//    private List<Payment> payments;

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

    public void generateReportByDateTime(HttpServletResponse response,
                                         LocalDate startDate, LocalDate endDate, List<Payment> payments)
            throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        if (startDate.equals(endDate)) {
            Paragraph paragraph = new Paragraph("REPORT IN DATE " + startDate, fontTiltle);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(paragraph);
        } else {
            Paragraph paragraph = new Paragraph("REPORT FROM " + startDate + " TO " + endDate, fontTiltle);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(paragraph);
        }
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3});
        table.setSpacingBefore(5);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.GREEN);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        cell.setPhrase(new Phrase("Store User", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Order ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Money Paid", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Money Unpaid", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Accumulated Money", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Total Money", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Payment Date", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Payment Time", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Status", font));
        table.addCell(cell);
        double totalMoney = 0;
        for (Payment payment : payments) {
            table.addCell(payment.getStoreUser());
            table.addCell(String.valueOf(payment.getOrderId()));
            table.addCell(String.valueOf(payment.getMoneyPaid()));
            table.addCell(String.valueOf(payment.getMoneyUnpaid()));
            table.addCell(String.valueOf(payment.getAccumulatedMoney()));
            table.addCell(String.valueOf(payment.getTotalMoney()));
            table.addCell(String.valueOf(payment.getPaymentDate().toLocalDate()));
            table.addCell(String.valueOf(payment.getPaymentDate().toLocalTime()));
            table.addCell(payment.getStatus());
            totalMoney += payment.getMoneyPaid();
        }
        document.add(table);
        Paragraph paragraph1 = new Paragraph("Total Money: " + String.valueOf(totalMoney), fontTiltle);
        paragraph1.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(paragraph1);
        document.close();
    }

}
