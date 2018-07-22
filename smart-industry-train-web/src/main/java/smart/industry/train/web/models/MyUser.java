package smart.industry.train.web.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
public class MyUser implements Serializable {
    @Getter @Setter private String name;
    @Getter @Setter private String psw;
}
