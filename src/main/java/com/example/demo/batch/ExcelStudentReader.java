package com.example.demo.batch;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.ClassPathResource;

public class ExcelStudentReader implements ItemReader<ExcelStudent> {
    private Workbook workbook;
    private org.apache.poi.ss.usermodel.Sheet sheet;
    private int currentRow = 1;

    public ExcelStudentReader() throws Exception {

        InputStream inputStream =
                new ClassPathResource("batch/students.xlsx").getInputStream();

        workbook = WorkbookFactory.create(inputStream);
        sheet = workbook.getSheetAt(0);
    }

    @Override 
    public ExcelStudent read() {

        if (currentRow > sheet.getLastRowNum()) {
            return null;
        }

        Row row = sheet.getRow(currentRow++);

        int id = (int) row.getCell(0).getNumericCellValue();
        String name = row.getCell(1).getStringCellValue();
        String email = row.getCell(2).getStringCellValue();

        return new ExcelStudent(id, name, email);
    }
}
