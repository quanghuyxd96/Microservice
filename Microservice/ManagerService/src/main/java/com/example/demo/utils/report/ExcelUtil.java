package com.example.demo.utils.report;

import com.example.demo.entity.Payment;
import com.lowagie.text.DocumentException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ExcelUtil {
    public void generateExcel(HttpServletResponse response, List<Payment> payments,
                              LocalDate startDate, LocalDate endDate) throws DocumentException, IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Item_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        ExcelGeneratorReport generator = new ExcelGeneratorReport(payments);
        generator.generate(response,startDate,endDate);
    }
}
