package com.shopme.admin.user.export;

import com.shopme.admin.utils.AbstractExporterUtil;
import com.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporterUtil {

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {

        // Tipo de archivo y extension
        super.setResponseHeader(response, "text/csv", ".csv", "users_");
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        // El nombre que tendran las columnas dentro del archivo csv
        String[] csvHeader = {"User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};

        // Como se van a poblar los datos
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};

        csvWriter.writeHeader(csvHeader);

        for(User user: listUsers){
            csvWriter.write(user, fieldMapping);
        }

        csvWriter.close();

    }

}
