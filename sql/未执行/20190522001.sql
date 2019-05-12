-- auto-generated definition
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
  remark     varchar(500) null
  comment '备注',
  constraint id_UNIQUE
  unique (id)
)
  comment '组织架构';


-- auto-generated definition
create table design_solution_partion
(
  id            int auto_increment
  comment '主键'
    primary key,
  createDate    datetime      null
  comment '创建时间',
  createBy      int           null
  comment '创建人',
  modifyDate    datetime      null
  comment '修改时间',
  modifyBy      int           null
  comment '修改人',
  name          varchar(200)  null
  comment '测试方案名',
  solutionId       int           null
  comment '关联方案分类',
  remark        varchar(1000) null
  comment '测试方案描述'
)
  comment '测试方案';

-- auto-generated definition
create table design_solution_partion_list
(
  id            int auto_increment
  comment '主键'
    primary key,
  createDate    datetime      null
  comment '创建时间',
  createBy      int           null
  comment '创建人',
  modifyDate    datetime      null
  comment '修改时间',
  modifyBy      int           null
  comment '修改人',
  partionId       int           null
  comment '关联方案ID',
  solutionFileId  int   null
  comment '方案文件ID'
)
  comment '测试方案列表';

-- 用户表添加字段 ------
alter table user add column `org_id` int null comment '组织ID' ;

-- 初始化 部门表中的基础数据 ------
insert into organization(id,name,code,org_type,pid,sort,remark)
values
(1,'组织架构中心', 'root', 0, null, '0', null),
(2,'测试公司', 'CSGS', 0, 1, '1', null),
(3,'测试一部', 'CSGS', 1, 2, '1', null),
(4,'测试一组', 'CSGS', 2, 3, '1', null)

-- 用户部门初始化 ----------------
update user set org_id = 1;