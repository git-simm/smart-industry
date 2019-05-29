package simm.dao.test;

import org.junit.Assert;
import org.junit.Test;
import smart.industry.train.biz.entity.User;
import smart.industry.train.biz.mypoi.DesignXlsBiz;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 去重测试
     */
    @Test
    public void distinctTest(){
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setCode("1");
        users.add(user);
        User user1 = new User();
        user1.setCode("1");
        users.add(user1);
        User user2 = new User();
        user2.setCode("2");
        users.add(user2);
        User user3 = new User();
        user3.setCode("2");
        users.add(user3);
        long count = users.stream().map(a->a.getCode()).distinct().count();
        long count2 = users.stream().map(a->a.getCode()).count();
        Assert.assertTrue(count==2);
    }
}
