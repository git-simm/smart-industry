package smart.industry.train.biz.enums;

/**
 * 任务转换枚举
 */
public enum TaskStateEnum {
    //待转换
    Ready(0),
    //转换中
    Converting(1),
    //转换完成
    Completed(2);
    int value;
    TaskStateEnum(int value) {
       this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
