package com.example.demo.utils.report;


import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.Payment;
import com.lowagie.text.Phrase;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ExcelGeneratorReport {

    private List<Payment> payments;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelGeneratorReport(List<Payment> payments) {
        this.payments = payments;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader(LocalDate startDate, LocalDate endDate) {
        if (startDate.equals(endDate)) {
            sheet = workbook.createSheet("REPORT IN DATE " + startDate);
        } else {
            sheet = workbook.createSheet("REPORT FROM " + startDate + " TO " + endDate);
        }
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Store User", style);
        createCell(row, 1, "Order ID", style);
        createCell(row, 2, "Money Paid", style);
        createCell(row, 3, "Money Unpaid", style);
        createCell(row, 4, "Accumulated Money", style);
        createCell(row, 5, "Total Money", style);
        createCell(row, 6, "Payment Date", style);
        createCell(row, 7, "Status", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        double totalMoney = 0;
        for (Payment payment : payments) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, payment.getStoreUser(), style);
            createCell(row, columnCount++, payment.getOrderId(), style);
            createCell(row, columnCount++, payment.getMoneyPaid(), style);
            createCell(row, columnCount++, payment.getMoneyUnpaid(), style);
            createCell(row, columnCount++, payment.getAccumulatedMoney(), style);
            createCell(row, columnCount++, payment.getTotalMoney(), style);
            createCell(row, columnCount++, String.valueOf(payment.getPaymentDate()), style);
            createCell(row, columnCount++, payment.getStatus(), style);
            totalMoney += payment.getMoneyPaid();
        }
        Row row = sheet.createRow(rowCount++);
        createCell(row, 6, "Total Price", style);
        createCell(row, 7, totalMoney, style);
    }

    public void generate(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException {
        writeHeader(startDate, endDate);
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
