create table organization
(
  id         int auto_increment
  comment '主键'
    primary key,
  createDate datetime     null
  comment '创建时间',
  createBy   int          null
  comment '创建人',
  modifyDate datetime     null
  comment '修改时间',
  modifyBy   int          null
  comment '修改人',
  name       varchar(200) null
  comment '组织名',
  code       varchar(200) null
  comment '组织编码',
  org_type   int          null
  comment '组织类型(1:公司；2：部门；3：工作组;)',
  pId        int          null
  comment '父级id',
  sort       varchar(50)  null
  comment '排序码',
  constraint id_UNIQUE
  unique (id)
)
  comment '组织架构';

