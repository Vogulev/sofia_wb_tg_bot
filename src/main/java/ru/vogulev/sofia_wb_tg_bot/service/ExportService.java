package ru.vogulev.sofia_wb_tg_bot.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.vogulev.sofia_wb_tg_bot.repository.WbUserRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExportService {
    public static final int COLUMN_SIZE = 6;
    private final WbUserRepository wbUserRepository;

    public ByteArrayInputStream exportUserData() {
        var wbUsers = wbUserRepository.findAll();
        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Пользователи");
            var row = sheet.createRow(0);
            row.createCell(0).setCellValue("Имя");
            row.createCell(1).setCellValue("Телефон");
            row.createCell(2).setCellValue("Информация");
            row.createCell(3).setCellValue("Статус");
            row.createCell(4).setCellValue("Время последнего изменения статуса");
            row.createCell(5).setCellValue("Логин");
            for (int i = 0; i < wbUsers.size(); i++) {
                var dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(wbUsers.get(i).getName());
                dataRow.createCell(1).setCellValue(wbUsers.get(i).getPhone());
                dataRow.createCell(2).setCellValue(wbUsers.get(i).getAbout());
                dataRow.createCell(3).setCellValue(wbUsers.get(i).getState().name());

                var dateCell = dataRow.createCell(4);
                var dataCellStyle = getDataCellStyle(workbook);
                dateCell.setCellStyle(dataCellStyle);
                dateCell.setCellValue(wbUsers.get(i).getStateUpdate());

                dataRow.createCell(5).setCellValue(wbUsers.get(i).getTgUserName());
            }
            for (int i = 0; i < COLUMN_SIZE; i++) {
                sheet.autoSizeColumn(i);
            }
            try (var out = new ByteArrayOutputStream()) {
                workbook.write(out);
                try (var in = new ByteArrayInputStream(out.toByteArray())) {
                    return in;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static XSSFCellStyle getDataCellStyle(XSSFWorkbook workbook) {
        var createHelper = workbook.getCreationHelper();
        var format = createHelper.createDataFormat().getFormat("dd.mm.yyyy h:mm");
        var cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(format);
        return cellStyle;
    }
}
