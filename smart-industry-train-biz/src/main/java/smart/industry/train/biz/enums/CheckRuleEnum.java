package smart.industry.train.biz.enums;

import smart.industry.train.biz.dao.check.strategies.*;

/**
 * 检查规则枚举
 * @author manman.si
 */
public enum CheckRuleEnum {
    //线号缺失
    LOST_WIRE(1,"线号缺失",LostWireCheckStrategy.class,1),
    //车型缺失
    LOST_PLANT(2,"车型缺失",LostPlantCheckStrategy.class,2),
    //电压等级缺失
    LOST_WIRE_GRADE(3,"电压等级缺失",LostWireGradeCheckStrategy.class,3),
    //屏蔽纤芯缺失
    LOST_CORE_NUMBER(4,"屏蔽纤芯缺失",LostCoreNumberCheckStrategy.class,4),
    //电缆型号缺失
    LOST_CABLE_TYPE(5,"电缆型号缺失",LostCableTypeCheckStrategy.class,5),
    //始末端位置缺失
    LOST_LOCATION(6,"始末端位置缺失",LostLocationCheckStrategy.class,6),
    //始末端名称缺失
    LOST_LOCATION_NAME(7,"始末端名称缺失",LostLocationNameCheckStrategy.class,7),
    //线号重复
    REPEAT_WIRE(8,"线号重复",RepeatWireCheckStrategy.class,8),
    //整车车型错误
    ERROR_PLANT(9,"整车车型错误",ErrorPlantCheckStrategy.class,9),
    //始末端名称的位置唯一
    ONLY_LOCATION_NAME(10,"始末端名称的位置唯一",OnlyLocationNameCheckStrategy.class,10),
    //始末端名称错误
    ERROR_LOCATION_NAME(11,"始末端名称错误",ErrorLocationNameCheckStrategy.class,11),
    //短接片PIN错误
    ERROR_SHORT_PIN(12,"短接片PIN错误",ErrorShortPinCheckStrategy.class,12),
    //屏蔽纤芯错误
    HIDE_CORE_NUMBER(13,"屏蔽纤芯错误",HideCodeNumberCheckStrategy.class,13),
    //始末端连接件型号错误
    ERROR_LOCATION_CONNECT(14,"始末端连接件型号错误",ErrorLocationConnectCheckStrategy.class,14),
    //端子排过插
    OVERLOAD_ENDPOINT(15,"端子排过插",OverloadEndpointCheckStrategy.class,15),
    //连接器过插
    OVERLOAD_CONNECTOR(16,"连接器过插",OverloadConnectorCheckStrategy.class,16),
    //电缆线芯在端子排上过远
    FAR_ENDPOINT(17,"电缆线芯在端子排上过远",FarEndpointCheckStrategy.class,17),
    //始末点位缺失
    LOST_ENDPOINT(18,"始末点位缺失",LostEndpointCheckStrategy.class,18),
    //始末点位缺失
    LOST_ENDPLANT(19,"始末端车型缺失",LostEndPlantCheckStrategy.class,19);
    private int value;
    private int sortNo;
    private String name;
    private Class<? extends CheckStrategy> strategy;
    CheckRuleEnum(int value, String name,Class strategy,int sortNo) {
        this.name = name;
        this.value = value;
        this.sortNo = sortNo;
        this.strategy = strategy;
    }
    public int getValue(){
        return this.value;
    }
    public int getSortNo(){
        return this.sortNo;
    }
    public String getName(){
        return this.name;
    }
    public Class getStrategyClazz(){
        return this.strategy;
    }
}
