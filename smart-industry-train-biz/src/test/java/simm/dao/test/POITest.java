package simm.dao.test;

import org.junit.Assert;
import org.junit.Test;
import smart.industry.train.biz.mypoi.DesignXlsBiz;

/**
 * poi测试
 */
public class POITest {
    @Test
    public void resolveTest(){
        DesignXlsBiz designXlsBiz = new DesignXlsBiz();
        try {
            //designXlsBiz.resolve("E:\\工作区\\01 本地项目\\罗总项目\\上海13号线图纸及清单\\DL-MP-SML13-V1.17.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(true);
    }
}
