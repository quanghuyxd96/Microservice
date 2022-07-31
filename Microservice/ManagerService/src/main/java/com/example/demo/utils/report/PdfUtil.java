package com.example.demo.utils.report;

import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.Payment;
import com.lowagie.text.DocumentException;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class PdfUtil {
    public void generatePdfToReport(HttpServletResponse response, List<Payment> payments,
                                    LocalDate startDate, LocalDate endDate) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        PDFGenerator generator = new PDFGenerator();
        generator.generateReportByDateTime(response, startDate, endDate, payments);
    }

    public void generatePdf(HttpServletResponse response, List<ItemDTO> items) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        PDFGenerator generator = new PDFGenerator();
        generator.setItems(items);
        generator.generate(response);
    }
}
