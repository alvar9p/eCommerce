package com.shopme.admin.user.export;

import com.shopme.admin.utils.AbstractExporterUtil;
import com.shopme.common.entity.User;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporterUtil {

    // Representa el libro de trabajo Excel
    private XSSFWorkbook workbook;
    // Representa una hoja dentro del libro de trabajo
    private XSSFSheet sheet;

    // Se inicializa el libro
    public UserExcelExporter(){
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine(){
        // Crea una nueva hoja llamada "Users"
        sheet = workbook.createSheet("Users");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "User Id", cellStyle);
        createCell(row, 1, "E-mail", cellStyle);
        createCell(row, 2, "First Name", cellStyle);
        createCell(row, 3, "Last Name", cellStyle);
        createCell(row, 4, "Roles", cellStyle);
        createCell(row, 5, "Enabled", cellStyle);
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style){
        // Crea una celda en la columna especifica
        XSSFCell cell = row.createCell(columnIndex);

        // Ajusta el tamano de la columna
        sheet.autoSizeColumn(columnIndex);

        // Asigna el valor segun tipo de dato
        if(value instanceof  Integer){
            cell.setCellValue((Integer) value);
        } else if (value instanceof  Boolean) {
            cell.setCellValue((Boolean) true);
        }else {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(style);
    }

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/octet-stream", ".xlsx", "users_");

        // Escribe la cabecera
        writeHeaderLine();
        writeDataLines(listUsers);

        // Obtiene el flujo de salida de la respuesta HTTP
        ServletOutputStream outputStream = response.getOutputStream();
        // Escribe la salida del libro excel
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void writeDataLines(List<User> listUsers){
        // row 0 es el header del documento
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for(User user: listUsers){
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;

            createCell(row, columnIndex++, user.getId(), cellStyle);
            createCell(row, columnIndex++, user.getEmail(), cellStyle);
            createCell(row, columnIndex++, user.getFirstName(), cellStyle);
            createCell(row, columnIndex++, user.getLastName(), cellStyle);
            createCell(row, columnIndex++, user.getRoles().toString(), cellStyle);
            createCell(row, columnIndex++, user.isEnabled(), cellStyle);
        }
    }
}
