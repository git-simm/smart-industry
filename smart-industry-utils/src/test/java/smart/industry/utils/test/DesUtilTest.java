package smart.industry.utils.test;

import org.junit.Assert;
import org.junit.Test;
import smart.industry.utils.DesUtil;

public class DesUtilTest {
    @Test
    public void desTest() {
        //待加密内容
        String str = "admins~^&**";
        //密钥长度要是8的倍数
        String password = "9588028820109132570743325311898495880288201091325707433253118984";

        byte[] result = DesUtil.encrypt(str.getBytes(),password);
        System.out.println("加密后："+new String(result));
        //直接将如上内容解密
        try {
            byte[] decryResult = DesUtil.decrypt(result, password);
            System.out.println("解密后："+new String(decryResult));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void testString(){
        String str = "000151|CD|DOR|1|||??????|CD";
        int i = str.indexOf("|");
        int j = str.indexOf("|||");
        String fileName = str.substring(0,i);
        String path = str.substring(0,j);
        System.out.println(fileName);
        System.out.println(path);
        Assert.assertTrue(true);
    }
}
