package smart.industry.train.biz.entity;

public class User {
    private Integer id;

    private String name;

    private String psw;

    private byte[] sex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public byte[] getSex() {
        return sex;
    }

    public void setSex(byte[] sex) {
        this.sex = sex;
    }
}