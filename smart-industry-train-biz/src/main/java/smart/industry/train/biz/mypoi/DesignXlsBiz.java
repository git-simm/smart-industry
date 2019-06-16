package smart.industry.train.biz.mypoi;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import smart.industry.train.biz.dao.DesignExcelAttrBiz;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.entity.DesignExcelAttr;
import smart.industry.train.biz.entity.DesignExcelList;
import smart.industry.train.biz.entity.SysUpfiles;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean resolve(final SysUpfiles file) {
        String filePath = file.getFilePath();
        Integer fileId = file.getId();
        HashMap<String, Integer> colMap = new HashMap<>();
        File f = new File(filePath);
        if (!f.exists()) return true;
        read(filePath,fileId,colMap,null);
        //designExcelListBiz.batchAdd(list);
        return true;
    }

    /**
     * 读取所有的数据
     * @param filepath
     * @param fileId
     * @param colMap
     * @return
     */
    private void read(String filepath,Integer fileId,HashMap<String, Integer> colMap,ExcelTypeEnum excelTypeEnum) {
        List<DesignExcelList> sheetContent = new ArrayList<>();
        if(excelTypeEnum ==null){
            if(isExcel2003(filepath)){
                excelTypeEnum = ExcelTypeEnum.XLS;
            }else{
                excelTypeEnum = ExcelTypeEnum.XLSX;
            }
        }
        try (InputStream is = new FileInputStream(filepath)) {
            InputStream inputStream = new BufferedInputStream(is);
            ExcelReader excelReader = new ExcelReader(inputStream,excelTypeEnum, null,
                    new AnalysisEventListener<List<String>>() {
                        @Override
                        public void invoke(List<String> strings, AnalysisContext analysisContext) {
                            if(strings != null && !StringUtils.isEmpty(strings.get(0))){
                                if(analysisContext.getCurrentRowNum()==0){
                                    //解析标题
                                    resolveHeader(strings,fileId,colMap);
                                }else {
                                    DesignExcelList item = resolveRow(strings);
                                    item.setFileId(fileId);
                                    sheetContent.add(item);
                                    if(sheetContent.size()>=200){
                                        designExcelListBiz.batchAdd(sheetContent);
                                        sheetContent.clear();
                                    }
                                }
                            }
                        }
                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {

                        }
                    });
            excelReader.read();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("excel解析异常",e);
            if(excelTypeEnum == ExcelTypeEnum.XLS){
                read(filepath,fileId,colMap,ExcelTypeEnum.XLSX);
            }
        }
        if(sheetContent.size() >0 ){
            designExcelListBiz.batchAdd(sheetContent);
        }
        //return sheetContent;
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
     * 解析表头数据
     *
     * @param row
     * @param fileId
     * @param colMap
     */
    private void resolveHeader(List<String> row,Integer fileId, HashMap<String, Integer> colMap) {
        for (int flag = 0; flag < row.size(); flag++) {
            DesignExcelAttr excelAttr = new DesignExcelAttr();
            excelAttr.setAttrName(row.get(flag));
            excelAttr.setColName("col" + (flag + 1));
            excelAttr.setFileId(fileId);
            designExcelAttrBiz.add(excelAttr);
            colMap.put(excelAttr.getColName(), excelAttr.getId());
        }
    }

    /**
     * 解析行数据
     *
     * @param row
     */
    private DesignExcelList resolveRow(Row row) throws Exception {
        HashMap<String,Field> fieldMap = new HashMap<>();
        DesignExcelList excelItem = new DesignExcelList();
        for (int flag = 0; flag < row.getLastCellNum(); flag++) {
            if(flag>=70) break; //字段解析上限为70
            String colName = "col" + (flag + 1);
            String colVal = getValue(row.getCell(flag)).toString();
            Field field = null;
            field = getField(fieldMap, excelItem, colName);
            field.set(excelItem, colVal);
        }
        return excelItem;
    }

    /**
     * 解析行数据
     * @param row
     * @return
     * @throws Exception
     */
    private DesignExcelList resolveRow(List<String> row) {
        HashMap<String,Field> fieldMap = new HashMap<>();
        DesignExcelList excelItem = new DesignExcelList();
        for (int flag = 0; flag < row.size(); flag++) {
            if(flag>=70) break; //字段解析上限为70
            String colName = "col" + (flag + 1);
            String colVal = row.get(flag);
            Field field = null;
            try {
                field = getField(fieldMap, excelItem, colName);
                field.set(excelItem, colVal);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return excelItem;
    }

    /**
     * 获取字段值
     * @param fieldMap
     * @param excelItem
     * @param colName
     * @return
     * @throws NoSuchFieldException
     */
    private Field getField(HashMap<String, Field> fieldMap, DesignExcelList excelItem, String colName) throws NoSuchFieldException {
        Field field;
        if(fieldMap.containsKey(colName)){
            field = fieldMap.get(colName);
        }else{
            field = excelItem.getClass().getDeclaredField(colName);
            field.setAccessible(true);
            fieldMap.put(colName,field);
        }
        return field;
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
