package smart.industry.train.biz.entity;

import lombok.Data;
import smart.industry.train.biz.entity.base.BaseEntity;

import java.util.Date;
@Data
public class SysUpfiles extends BaseEntity {

    private Date createDate;

    private Date modifyDate;

    private String fileName;

    private String filePath;

    private String relativePath;

    private String suffix;

    private Long fileSize;

    private String projPath;

    private String projFile;
}