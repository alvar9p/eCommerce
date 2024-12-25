package com.shopme.admin.user.export;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.admin.utils.AbstractExporterUtil;
import com.shopme.common.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends AbstractExporterUtil {

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException{

        // Configura la cabecera de la respuesta HTTP para un archivo PDF
        super.setResponseHeader(response, "application/pdf", ".pdf", "users_");

        // Crea un nuevo documento PDF con tamano A4
        Document document = new Document(PageSize.A4);

        // Obtiene el flujo de salida de la respuesta para escribir el PDF
        PdfWriter.getInstance(document, response.getOutputStream());

        // Abre el documento para agregar contenido
        document.open();

        // Define una fuente para el título del PDF
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        // Crea un párrafo para el título con la fuente definida
        Paragraph paragraph = new Paragraph("List of Users", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        // Agrega el título al documento
        document.add(new Paragraph(paragraph));

        // Crea una tabla con 6 columnas
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f); // Ancho de la tabla en porcentaje
        table.setSpacingBefore(10); // Espacio antes de la tabla
        // Define los anchos relativos de las columnas
        table.setWidths(new float[] {1.2f, 3.5f, 3.0f, 3.0f, 3.0f, 1.5f});

        // Escribe la cabecera de la tabla
        writeTableHeader(table);

        // Escribe los datos de los usuarios en la tabla
        writeTableData(table, listUsers);

        // Agrega la tabla al documento
        document.add(table);

        // Cierra el documento
        document.close();
    }

    private void writeTableHeader(PdfPTable table){

        // Crea una celda para la cabecera
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE); // Color de fondo de la celda
        cell.setPadding(5); // Espaciado interno de la celda

        // Define una fuente para la cabecera
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        // Crea las celdas de cabecera con el texto correspondiente y la fuente definida
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("E-mail", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("First Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Last Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Roles", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Enabled", font));
        table.addCell(cell);
    }

    public void writeTableData(PdfPTable table, List<User> listUsers){
        // Recorre la lista de usuarios y agrega una fila por cada usuario
        for(User user: listUsers){
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getEmail());
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getRoles().toString());
            table.addCell(String.valueOf(user.isEnabled()));
        }
    }
}
