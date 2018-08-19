package smart.industry.train.biz.mypoi;

import com.alibaba.fastjson.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 设计文件的解析中心
 */
@Service
public class DesignXlsBiz {
    public JSONArray resolve(final String filePath) throws Exception {
        JSONArray infos = new JSONArray();
        try {
            InputStream in = new FileInputStream(new File(filePath));
            Workbook workbook;
            try {
                workbook = new HSSFWorkbook(in);
            } catch (Exception ex) {
                workbook = new XSSFWorkbook(in);
            }
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row tempRow = sheet.getRow(i);
                //int minCell = tempRow.getFirstCellNum();
                //int maxCell = tempRow.getLastCellNum();
                for (int flag = 0; flag < tempRow.getLastCellNum(); flag++) {
                    System.out.print(getValue(tempRow.getCell(flag)) + "\t");
                }
                System.out.println();
            }
            return infos;
            //return InvokeResult.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            //return InvokeResult.failure(500, "impPriceRecord:历史数据导入错误" + e.getMessage());
        }
        return infos;
    }

    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    private Object getValue(Cell cell) {
        if (cell == null) return null;
        Object result = null;
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());
                break;
            case NUMERIC: {
                if (cell.getCellStyle().getDataFormat() == 22) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value);
                }
            }
            break;
            default:
                result = String.valueOf(cell.getStringCellValue());
                break;
        }
        return result;
    }

    /**
     * @描述：是否是2003的excel，返回true是2003
     * @返回值：boolean
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * @描述：是否是2007的excel，返回true是2007
     * @返回值：boolean
     */

    public static boolean isExcel2007(String filePath) {
        // ?i 的意思是匹配时不区分大小写
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

}
