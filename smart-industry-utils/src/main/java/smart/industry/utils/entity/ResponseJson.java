package smart.industry.utils.entity;

import smart.industry.utils.enums.CodeEnum;

import java.util.List;

public class ResponseJson {

    private Integer code;
    private String message;

    private Object data;
    private List<String> errors;

    public ResponseJson() {
        this(CodeEnum.Ok);
    }

    public ResponseJson(CodeEnum codeEnum) {
        this.code = codeEnum.getKey();
        this.message = codeEnum.getValue();
    }

    public ResponseJson(CodeEnum codeEnum, String message)
    {
        this.code = codeEnum.getKey();
        this.message = message;
    }

    public ResponseJson(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseJson(Object data) {
        this();
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}