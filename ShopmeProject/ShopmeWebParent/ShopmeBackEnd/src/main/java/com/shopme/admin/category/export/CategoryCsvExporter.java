package com.shopme.admin.category.export;

import com.shopme.admin.utils.AbstractExporterUtil;
import com.shopme.common.entity.Category;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporterUtil {

    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException{

        // Tipo de archivo y extension
        super.setResponseHeader(response, "text/csv", ".csv", "categories_");
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        // El nombre que tendran las columnas dentro del archivo csv
        String[] csvHeader = {"Category ID", "Category Name"};

        // Como se van a poblar los datos
        String[] fieldMapping = {"id", "name"};

        csvWriter.writeHeader(csvHeader);

        for (Category category : listCategories){
            category.setName(category.getName().replace("--", "  "));
            csvWriter.write(category, fieldMapping);
        }

        csvWriter.close();
    }
}
