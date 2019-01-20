package smart.industry.train.biz.enums;

/**
 * 文件类型枚举
 */
public enum FileTypeEnum {
    Standard(1,"原理图"),Design(2,"布线图"),Bill(3,"布线清单");
    int value;
    String name;
    FileTypeEnum(int value,String name) {
        this.name = name;
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
    public String getName(){
        return this.name;
    }
}
