package smart.industry.utils.enums;

public enum CodeEnum {
    Ok(100, "处理成功"),
    RequestFormatError(1001, "公共请求参数为空."),
    ClientTimeError(1002, "时间无效."),
    UserInValid(1003, "无效的用户密码."),
    SignatureNotExist(1004, "签名鉴权失败."),
    ChannelError(1005,"渠道有误."),
    ServerError(2001, "服务端系统异常."),
    BizParamError(3001, "数据参数有误."),
    ServerBizError(4001, "业务前置校验失败."),
    ServerDataError(4002, "服务端预置数据有误."),
    NeedInitMoneySumError(4003, "需要初始化余额实况表.");

    private int key;

    private String value;

    CodeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}