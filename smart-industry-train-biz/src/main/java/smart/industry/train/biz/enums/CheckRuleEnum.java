package smart.industry.train.biz.enums;

import smart.industry.train.biz.dao.check.strategies.*;

/**
 * 检查规则枚举
 * @author manman.si
 */
public enum CheckRuleEnum {
    //线号缺失
    LOST_WIRE(1,"线号缺失",LostWireCheckStrategy.class),
    //车型缺失
    LOST_PLANT(2,"车型缺失",LostPlantCheckStrategy.class),
    //电压等级缺失
    LOST_WIRE_GRADE(3,"电压等级缺失",LostWireGradeCheckStrategy.class),
    //屏蔽纤芯缺失
    LOST_CORE_NUMBER(4,"屏蔽纤芯缺失",LostCoreNumberCheckStrategy.class),
    //电缆型号缺失
    LOST_CABLE_TYPE(5,"电缆型号缺失",LostCableTypeCheckStrategy.class),
    //始末端位置缺失
    LOST_LOCATION(6,"始末端位置缺失",LostLocationCheckStrategy.class),
    //始末端名称缺失
    LOST_LOCATION_NAME(7,"始末端名称缺失",LostLocationNameCheckStrategy.class),
    //线号重复
    REPEAT_WIRE(8,"线号重复",RepeatWireCheckStrategy.class),
    //整车车型错误
    ERROR_PLANT(9,"整车车型错误",ErrorPlantCheckStrategy.class),
    //始末端名称的位置唯一
    ONLY_LOCATION_NAME(10,"始末端名称的位置唯一",OnlyLocationNameCheckStrategy.class),
    //始末端名称错误
    ERROR_LOCATION_NAME(11,"始末端名称错误",ErrorLocationNameCheckStrategy.class),
    //短接片PIN错误
    ERROR_SHORT_PIN(12,"短接片PIN错误",ErrorShortPinCheckStrategy.class),
    //屏蔽纤芯错误
    HIDE_CORE_NUMBER(13,"屏蔽纤芯错误",HideCodeNumberCheckStrategy.class),
    //始末端连接件型号错误
    ERROR_LOCATION_CONNECT(14,"始末端连接件型号错误",ErrorLocationConnectCheckStrategy.class),
    //端子排过插
    OVERLOAD_ENDPOINT(15,"端子排过插",OverloadEndpointCheckStrategy.class),
    //连接器过插
    OVERLOAD_CONNECTOR(16,"连接器过插",OverloadConnectorCheckStrategy.class),
    //电缆线芯在端子排上过远
    FAR_ENDPOINT(17,"电缆线芯在端子排上过远",FarEndpointCheckStrategy.class),
    //始末点位缺失
    LOST_ENDPOINT(18,"始末点位缺失",LostEndpointCheckStrategy.class);

    private int value;
    private String name;
    private Class<? extends CheckStrategy> strategy;
    CheckRuleEnum(int value, String name,Class strategy) {
        this.name = name;
        this.value = value;
        this.strategy = strategy;
    }
    public int getValue(){
        return this.value;
    }
    public String getName(){
        return this.name;
    }
    public Class getStrategyClazz(){
        return this.strategy;
    }
}
