package smart.industry.train.biz.enums;

/**
 * 检查规则枚举
 */
public enum CheckRuleEnum {
    LOST_WIRE(1,"线号缺失"),
    LOST_PLANT(2,"车型缺失"),
    LOST_WIRE_GRADE(3,"电压等级缺失"),
    LOST_CORE_NUMBER(4,"屏蔽纤芯缺失"),
    LOST_CABLE_TYPE(5,"电缆型号缺失"),
    LOST_LOCATION(6,"始末端位置缺失"),
    LOST_LOCATION_NAME(7,"始末端名称缺失"),
    REPEAT_WIRE(8,"线号重复"),
    ERROR_PLANT(9,"整车车型错误"),
    ONLY_LOCATION_NAME(10,"始末端名称的位置唯一"),
    ERROR_LOCATION_NAME(11,"始末端名称错误"),
    ERROR_SHORT_PIN(12,"短接片PIN错误"),
    HIDE_CORE_NUMBER(13,"屏蔽纤芯错误"),
    ERROR_LOCATION_CONNECT(14,"始末端连接件型号错误"),
    OVERLOAD_ENDPOINT(15,"端子排过插"),
    OVERLOAD_CONNECTOR(16,"连接器过插"),
    FAR_ENDPOINT(16,"电缆线芯在端子排上过远"),
    LOST_ENDPOINT(17,"始末点位缺失");

    private int value;
    private String name;
    CheckRuleEnum(int value, String name) {
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
