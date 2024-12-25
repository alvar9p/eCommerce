package com.shopme.admin.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExporterUtil {

    // Se utiliza para crear el contentType del archivo a descargar
    public void setResponseHeader(HttpServletResponse response, String contentType,
                                  String extension, String prefix) throws IOException {

        // Para el nombre del archivo cada vez que sea descargado
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());
        String fileName = prefix + timestamp + extension;

        response.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }
}
