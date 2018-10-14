package smart.industry.train.biz.enums;

/**
 * 文件类型枚举
 */
public enum FileTypeEnum {
    Standard(1,"基准"),Design(2,"设计"),Bill(3,"清单");
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
