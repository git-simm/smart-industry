create table design_block_attr
(
  id         int auto_increment
    primary key,
  createDate datetime     null
  comment '创建时间',
  createBy   int          null
  comment '创建人',
  modifyDate datetime     null
  comment '修改时间',
  modifyBy   int          null
  comment '修改人',
  detailId   int          null
  comment '对应的设计方案明细ID',
  attrId     int          null
  comment '对应的属性信息ID',
  blockId    int          null
  comment 'block主键',
  value      varchar(500) null
  comment '属性值',
  attrName   varchar(500) null
  comment '属性名',
  constraint id_UNIQUE
  unique (id)
)
  comment '方案block对应的属性信息';

create table design_class
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
  comment '分类名',
  pId        int          null
  comment '父级id',
  sort       int          null
  comment '排序码(系统自动创建)',
  constraint id_UNIQUE
  unique (id)
)
  comment '方案分类';

create table design_detail_block
(
  id         int auto_increment
    primary key,
  createDate datetime     null
  comment '创建时间',
  createBy   int          null
  comment '创建人',
  modifyDate datetime     null
  comment '修改时间',
  modifyBy   int          null
  comment '修改人',
  detailId   int          null
  comment '对应的设计方案明细ID',
  name       varchar(500) null
  comment '方案明细对应的block信息',
  constraint id_UNIQUE
  unique (id)
)
  comment '方案明细block信息';

create table design_excel_attr
(
  id         int auto_increment
    primary key,
  createDate datetime     null
  comment '创建时间',
  createBy   int          null
  comment '创建人',
  modifyDate datetime     null
  comment '修改时间',
  modifyBy   int          null
  comment '修改人',
  attrName   varchar(500) null
  comment '属性名称',
  colName    varchar(100) null
  comment '系统列名',
  fileId     int          null
  comment '清单文件ID',
  constraint id_UNIQUE
  unique (id)
)
  comment '设计清单属性定义';

create table design_excel_list
(
  id         int auto_increment
    primary key,
  createDate datetime     null
  comment '创建时间',
  createBy   int          null
  comment '创建人',
  modifyDate datetime     null
  comment '修改时间',
  modifyBy   int          null
  comment '修改人',
  fileId     int          null
  comment '文件ID',
  col1       varchar(200) null,
  col2       varchar(200) null,
  col3       varchar(200) null,
  col4       varchar(200) null,
  col5       varchar(200) null,
  col6       varchar(200) null,
  col7       varchar(200) null,
  col8       varchar(200) null,
  col9       varchar(200) null,
  col10      varchar(200) null,
  col11      varchar(200) null,
  col12      varchar(200) null,
  col13      varchar(200) null,
  col14      varchar(200) null,
  col15      varchar(200) null,
  col16      varchar(200) null,
  col17      varchar(200) null,
  col18      varchar(200) null,
  col19      varchar(200) null,
  col20      varchar(200) null,
  col21      varchar(200) null,
  col22      varchar(200) null,
  col23      varchar(200) null,
  col24      varchar(200) null,
  col25      varchar(200) null,
  col26      varchar(200) null,
  col27      varchar(200) null,
  col28      varchar(200) null,
  col29      varchar(200) null,
  col30      varchar(200) null,
  col31      varchar(200) null,
  col32      varchar(200) null,
  col33      varchar(200) null,
  col34      varchar(200) null,
  col35      varchar(200) null,
  col36      varchar(200) null,
  col37      varchar(200) null,
  col38      varchar(200) null,
  col39      varchar(200) null,
  col40      varchar(200) null,
  col41      varchar(200) null,
  col42      varchar(200) null,
  col43      varchar(200) null,
  col44      varchar(200) null,
  col45      varchar(200) null,
  col46      varchar(200) null,
  col47      varchar(200) null,
  col48      varchar(200) null,
  col49      varchar(200) null,
  col50      varchar(200) null,
  col51      varchar(200) null,
  col52      varchar(200) null,
  col53      varchar(200) null,
  col54      varchar(200) null,
  col55      varchar(200) null,
  col56      varchar(200) null,
  col57      varchar(200) null,
  col58      varchar(200) null,
  col59      varchar(200) null,
  col60      varchar(200) null,
  col61      varchar(200) null,
  col62      varchar(200) null,
  col63      varchar(200) null,
  col64      varchar(200) null,
  col65      varchar(200) null,
  col66      varchar(200) null,
  col67      varchar(200) null,
  col68      varchar(200) null,
  col69      varchar(200) null,
  col70      varchar(200) null,
  constraint id_UNIQUE
  unique (id)
)
  comment '设计清单';

create table design_solution
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
  comment '方案名',
  classId       int           null
  comment '方案所属分类',
  billCount     int           null
  comment '清单数',
  standardCount int           null
  comment '基准文件数',
  designCount   int           null
  comment '设计文件数',
  remark        varchar(1000) null
  comment '方案描述'
)
  comment '设计方案';

create table design_solution_list
(
  id         int auto_increment
  comment '主键'
    primary key,
  createDate datetime      null
  comment '创建时间',
  createBy   int           null
  comment '创建人',
  modifyDate date          null
  comment '修改时间',
  modifyBy   int           null
  comment '修改人',
  name       varchar(200)  null
  comment '方案名',
  solutionId int           null
  comment '设计方案ID',
  fileId     int           null
  comment '对应的文件列表ID',
  type       int           not null
  comment '文件类型',
  remark     varchar(1000) null
  comment '方案描述'
)
  comment '设计方案明细列表';

create table param_group
(
  id         int auto_increment
    primary key,
  createDate datetime         null
  comment '创建时间',
  createBy   int              null
  comment '创建人',
  modifyDate datetime         null
  comment '修改时间',
  modifyBy   int              null
  comment '修改人',
  name       varchar(200)     null
  comment '分组名',
  paramType  varchar(50)      null
  comment '类型',
  defaultVal varchar(200)     null
  comment '默认值',
  remark     varchar(500)     null
  comment '说明',
  valid      bit default b'1' null
  comment '是否有效',
  constraint param_group_id_uindex
  unique (id)
)
  comment '参数分组';

create table param_options
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
  groupid    int          null
  comment '所属分组',
  value      varchar(200) null
  comment '值',
  showVal    varchar(200) null
  comment '显示值',
  sort       int          null
  comment '排序码',
  constraint param_options_id_uindex
  unique (id)
)
  comment '参数项';

create table sys_dxfAttr
(
  id         int auto_increment
    primary key,
  createDate datetime     null
  comment '创建时间',
  createBy   int          null
  comment '创建人',
  modifyDate datetime     null
  comment '修改时间',
  modifyBy   int          null
  comment '修改人',
  name       varchar(500) null
  comment '属性名',
  constraint id_UNIQUE
  unique (id)
)
  comment 'dxf属性列表';

create table sys_tasks
(
  id         int auto_increment
    primary key,
  createDate datetime null
  comment '创建时间',
  createBy   int      null
  comment '创建人',
  modifyDate datetime null
  comment '修改时间',
  modifyBy   int      null
  comment '修改人',
  detailId   int      null
  comment '方案子项ID',
  state      int      null
  comment '转换状态(0:待转换;1:转换中;2:转换完成)',
  retries    int      null
  comment '重试次数',
  constraint id_UNIQUE
  unique (id)
)
  comment '系统自动转换任务';

create table sys_upfiles
(
  id           int auto_increment
  comment '主键'
    primary key,
  createDate   datetime     null
  comment '创建时间',
  createBy     int          null
  comment '创建人',
  modifyDate   datetime     null
  comment '修改时间',
  modifyBy     int          null
  comment '修改人',
  suffix       varchar(50)  null
  comment '文件后缀',
  filePath     varchar(200) null
  comment '文件路径',
  relativePath varchar(200) null
  comment '相对路径',
  fileName     varchar(200) null
  comment '文件名',
  fileSize     bigint       null
  comment '文件尺寸',
  projPath     varchar(200) null
  comment '项目路径'
)
  comment '系统上载文件列表';

create table user
(
  id         int auto_increment
    primary key,
  createDate datetime      null
  comment '创建时间',
  createBy   int           null
  comment '创建人',
  modifyDate datetime      null
  comment '修改时间',
  modifyBy   int           null
  comment '修改人',
  name       varchar(50)   null,
  code       varchar(50)   null,
  psw        varchar(50)   null,
  sex        bit           null,
  remark     varchar(2000) null,
  department varchar(200)  null,
  constraint id_UNIQUE
  unique (id)
);

