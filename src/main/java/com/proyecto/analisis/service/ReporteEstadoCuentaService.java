package com.proyecto.analisis.service;

import com.proyecto.analisis.dto.EstadoCuentaDTO;
import com.proyecto.analisis.dto.MovimientoEstadoCuentaDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class ReporteEstadoCuentaService {

    public void generarPdf(EstadoCuentaDTO dto, OutputStream outputStream) throws IOException {
        try {
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);

            document.add(new Paragraph("Estado de Cuenta", titleFont));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Nombre del Cliente: " + dto.getNombreCliente(), normalFont));
            document.add(new Paragraph("Número de Cuenta: " + dto.getNumeroCuenta(), normalFont));
            document.add(new Paragraph("Fecha de Emisión: " + java.time.LocalDate.now(), normalFont));
            document.add(new Paragraph("Periodo: " + dto.getPeriodo(), normalFont));

            document.add(new Paragraph("\nDetalle de Movimientos", boldFont));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            String[] headers = {"Fecha Movimiento", "Tipo de Movimiento", "Descripción", "Cargo (Q)", "Abono (Q)", "Saldo Acumulado (Q)"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, boldFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            for (MovimientoEstadoCuentaDTO mov : dto.getMovimientos()) {
                table.addCell(new Phrase(mov.getFechaMovimiento().toString(), normalFont));
                table.addCell(new Phrase(mov.getTipoMovimiento(), normalFont));
                table.addCell(new Phrase(mov.getDescripcion(), normalFont));
                table.addCell(new Phrase(mov.getCargo().toString(), normalFont));
                table.addCell(new Phrase(mov.getAbono().toString(), normalFont));
                table.addCell(new Phrase(mov.getSaldoAcumulado().toString(), normalFont));
            }

            document.add(table);

            document.add(new Paragraph("\nTotales", boldFont));
            document.add(new Paragraph("Total Cargos: Q " + dto.getTotalCargos(), normalFont));
            document.add(new Paragraph("Total Abonos: Q " + dto.getTotalAbonos(), normalFont));
            document.add(new Paragraph("Saldo Final: Q " + dto.getSaldoFinal(), normalFont));

            document.close();

        } catch (DocumentException e) {
            throw new IOException("Error al generar el PDF", e);
        }
    }

    public void generarExcel(EstadoCuentaDTO dto, OutputStream outputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Estado de Cuenta");
            int rowIdx = 0;

            Row title = sheet.createRow(rowIdx++);
            title.createCell(0).setCellValue("Estado de Cuenta");

            sheet.createRow(rowIdx++).createCell(0).setCellValue("Nombre del Cliente: " + dto.getNombreCliente());
            sheet.createRow(rowIdx++).createCell(0).setCellValue("Número de Cuenta: " + dto.getNumeroCuenta());
            sheet.createRow(rowIdx++).createCell(0).setCellValue("Periodo: " + dto.getPeriodo());
            rowIdx++;

            Row header = sheet.createRow(rowIdx++);
            String[] headers = {"Fecha Movimiento", "Tipo de Movimiento", "Descripción", "Cargo (Q)", "Abono (Q)", "Saldo Acumulado (Q)"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);

            for (MovimientoEstadoCuentaDTO mov : dto.getMovimientos()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(mov.getFechaMovimiento().toString());
                row.createCell(1).setCellValue(mov.getTipoMovimiento());
                row.createCell(2).setCellValue(mov.getDescripcion());
                row.createCell(3).setCellValue(mov.getCargo().doubleValue());
                row.createCell(4).setCellValue(mov.getAbono().doubleValue());
                row.createCell(5).setCellValue(mov.getSaldoAcumulado().doubleValue());
            }

            rowIdx++;
            sheet.createRow(rowIdx++).createCell(0).setCellValue("Total Cargos: Q " + dto.getTotalCargos());
            sheet.createRow(rowIdx++).createCell(0).setCellValue("Total Abonos: Q " + dto.getTotalAbonos());
            sheet.createRow(rowIdx++).createCell(0).setCellValue("Saldo Final: Q " + dto.getSaldoFinal());

            workbook.write(outputStream);
        }
    }
}
