package smart.industry.train.biz.mypoi;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smart.industry.train.biz.dao.DesignExcelAttrBiz;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.entity.DesignExcelAttr;
import smart.industry.train.biz.entity.DesignExcelList;
import smart.industry.train.biz.entity.SysUpfiles;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 设计文件的解析中心
 */
@Service
public class DesignXlsBiz {
    private final Logger logger = LoggerFactory.getLogger(DesignXlsBiz.class);
    /**
     * 设计清单属性列表
     */
    @Autowired
    private DesignExcelAttrBiz designExcelAttrBiz;
    /**
     * 设计清单列表
     */
    @Autowired
    private DesignExcelListBiz designExcelListBiz;

    /**
     * 解析excel清单
     *
     * @param file
     * @return
     * @throws Exception
     */
    //@Transactional(rollbackFor = Exception.class)
    public boolean resolve(final SysUpfiles file) throws Exception {
        String filePath = file.getFilePath();
        Integer fileId = file.getId();
        HashMap<String, Integer> colMap = new HashMap<>();
        File f = new File(filePath);
        if (!f.exists()) return true;
        FileInputStream in = new FileInputStream(f);
        Workbook workbook = null;
        try {
            workbook = StreamingReader.builder()
                    .rowCacheSize(100)  //缓存到内存中的行数，默认是10
                    .bufferSize(4096)  //读取资源时，缓存到内存的字节大小，默认是1024
                    .open(in);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            Sheet sheet = workbook.getSheetAt(0);
            List<DesignExcelList> list = new ArrayList<>();
            Row row = sheet.getRow(0);
            //解析标题
            resolveHeader(row,fileId, colMap);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                DesignExcelList item = resolveRow(sheet.getRow(i));
                item.setFileId(fileId);
                //解析内容
                list.add(item);
            }
            designExcelListBiz.batchAdd(list);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("excel解析失败",ex);
            return false;
        }finally {
            if(workbook!=null){
                workbook.close();
            }
            if(in !=null){
                in.close();
            }
        }
        //return InvokeResult.success(true);
    }

    /**
     * 解析表头数据
     *
     * @param row
     * @param fileId
     * @param colMap
     */
    private void resolveHeader(Row row,Integer fileId, HashMap<String, Integer> colMap) {
        for (int flag = 0; flag < row.getLastCellNum(); flag++) {
            DesignExcelAttr excelAttr = new DesignExcelAttr();
            excelAttr.setAttrName(getValue(row.getCell(flag)).toString());
            excelAttr.setColName("col" + (flag + 1));
            excelAttr.setFileId(fileId);
            designExcelAttrBiz.add(excelAttr);
            colMap.put(excelAttr.getColName(), excelAttr.getId());
        }
    }

    /**
     * 字段信息缓冲
     */
    public HashMap<String,Field> fieldMap = new HashMap<>();
    /**
     * 解析行数据
     *
     * @param row
     */
    private DesignExcelList resolveRow(Row row) throws Exception {
        DesignExcelList excelItem = new DesignExcelList();
        for (int flag = 0; flag < row.getLastCellNum(); flag++) {
            if(flag>=70) break; //字段解析上限为70
            String colName = "col" + (flag + 1);
            String colVal = getValue(row.getCell(flag)).toString();
            Field field = null;
            if(fieldMap.containsKey(colName)){
                field = fieldMap.get(colName);
            }else{
                field = excelItem.getClass().getDeclaredField(colName);
                field.setAccessible(true);
                fieldMap.put(colName,field);
            }
            field.set(excelItem, colVal);
        }
        return excelItem;
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    private Object getValue(Cell cell) {
        if (cell == null) return "";
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
        if(result==null)return "";
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
