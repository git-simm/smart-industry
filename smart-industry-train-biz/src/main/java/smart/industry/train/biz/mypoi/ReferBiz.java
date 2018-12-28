package smart.industry.train.biz.mypoi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smart.industry.train.biz.dao.DesignExcelAttrBiz;
import smart.industry.train.biz.dao.DesignExcelListBiz;
import smart.industry.train.biz.dao.DesignSolutionListBiz;
import smart.industry.train.biz.dao.SysUpfilesBiz;
import smart.industry.train.biz.entity.DesignExcelAttr;
import smart.industry.train.biz.entity.DesignExcelList;
import smart.industry.train.biz.entity.DesignSolutionList;
import smart.industry.train.biz.entity.SysUpfiles;

import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 设计文件的解析中心
 */
@Service
public class ReferBiz {
    private final Logger logger = LoggerFactory.getLogger(ReferBiz.class);
    @Autowired
    private DesignSolutionListBiz designSolutionListBiz;
    @Autowired
    private SysUpfilesBiz sysUpfilesBiz;
    /**
     * 解析excel清单
     *
     * @param detail
     * @param file
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void resolve(DesignSolutionList detail, final SysUpfiles file) throws Exception {
        String filePath = file.getFilePath();
        Integer fileId = file.getId();
        Map<String, Integer> map = new HashMap<String, Integer>();
        /* 读取数据 */
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),
                    "UTF-8"));
            //获取所有的文件，归类进行处理。拿到所有的对应文件，准备为其赋值
            List<DesignSolutionList> solutionFiles = designSolutionListBiz.getAllListBySolution(detail.getSolutionId());
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                int i = lineTxt.indexOf("|");
                int j = lineTxt.indexOf("|||");
                String fileName = lineTxt.substring(0,i);
                String path = lineTxt.substring(0,j);
                updateFileProjPath(solutionFiles,fileName,path);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        //designExcelListBiz.batchAdd(list);
        //return InvokeResult.success(true);
    }

    /**
     * 找到相应的文件更新项目路径
     * @param list
     * @param name
     * @param path
     */
    private void updateFileProjPath(List<DesignSolutionList> list, String name, String path){
        Optional<DesignSolutionList> temp = list.stream().filter(a->a.getName().startsWith(name)).findFirst();
        if(!temp.isPresent()) return;
        DesignSolutionList solu = temp.get();
        SysUpfiles file =  sysUpfilesBiz.selectByPrimaryKey(solu.getFileId());
        file.setProjPath(path);
        sysUpfilesBiz.update(file);
    }
}
