drop database shop_db; 
create database shop_db character set utf8 default character set utf8 collate utf8_general_ci default collate utf8_general_ci;

use shop_db;

create table t_good 
(
	goodId                    bigint               auto_increment comment "商品ID",
	goodName                  varchar(255)         not null comment "商品名称",
	price                     float                not null comment "商品价格",
	imageUrl                  varchar(512)         not null comment "商品图片Url",
	goodUrl                   varchar(512)         not null comment "淘宝商品Url",
	channel                   int                  not null comment "商品来源渠道: 1-天天9块九, 2-精品推荐",
	createTime                bigint               not null comment "创建时间",
	status                    int                  not null comment "商品状态: 1-上架, 2-下架",
	categoryId                bigint               not null comment "商品分类",
	
	keyword                   varchar(2048)        not null comment "搜索引擎索引关键字",
	showTimes                 bigint               not null comment "近期展示次数",
	boost                     float                not null comment "搜索排名得分调整参数, 默认为1",
	saleCount                 int                  not null comment "30天售出的件数",
	commissionRate            float                not null comment "佣金比率",
	commissionCount           float                not null comment "30天支出的佣金总数",
	constraint t_good_pk primary key (goodId)
) engine=myisam comment "商品";
create index index_goodName on t_good (goodName);


create table t_good_feedback 
(
	feedbackId               bigint               auto_increment comment "id",
	goodId                   bigint               not null comment "商品ID",
	feedbackTime             bigint               not null comment "创建时间",
	constraint t_good_feedback_pk primary key (feedbackId)
) engine=myisam comment "商品反馈";

create table t_active
(
    activeId                  bigint              auto_increment comment "活动ID",
    activeName                varchar(128)        comment "活动名称",
    startDate                 int                 comment "活动开始日期",
    endDate                   int                 comment "活动结束日期",
    activeUrl                 varchar(512)        comment "淘宝活动Url",
    imageUrl                  varchar(512)        comment "活动图片连接",
    activeChannel             int                 comment "活动渠道: 1-pc, 2-mobile",
    createTime                bigint              comment "创建时间",
    constraint t_active_pk primary key (activeId)
) engine=myisam comment "活动实体";

create table t_hotword 
(
	hotwordId                bigint                auto_increment comment "热词id",
	hotword                  varchar(64)           not null comment "热词",
	rank                     int                   not null comment "排名",
	date                     bigint                not null comment "产生日期",
	constraint t_hotword_pk primary key (hotwordId)
) engine=myisam comment "搜索热词";


create table t_search_log 
(
	searchId                 bigint                auto_increment comment "搜索id",
	searchKey                varchar(64)           not null comment "搜索关键字",
	pageNo                   int                   not null comment "当前页码",
	searchResult             varchar(512)          not null comment "搜索结果, good的ID集合, 用逗号隔开",
	createTime               bigint                not null comment "创建时间",
	constraint t_search_log_pk primary key (searchId)
) engine=myisam comment "搜索日志";

create table t_show_log 
(
	showId                   bigint               auto_increment comment "展示id",
	searchKey                varchar(64)          null comment "搜索关键字",
	goodId                   bigint               not null comment "商品ID",
	createTime               bigint               not null comment "创建时间",
	constraint t_show_log_pk primary key (showId)
) engine=myisam comment "商品详细展示日志, 指点击查看商品详细";

create table t_build_log 
(
	buildId                   bigint               auto_increment comment "id",
	startTime                 bigint               not null comment "开始时间",
	endTime                   bigint               not null comment "结束时间",
	buildResult               int                  not null comment "建立结果",
	type                      int                  not null comment "索引类型:1-商品索引, 2-搜索词索引, 3-计算商品分类",
	constraint t_index_build_pk primary key (buildId)
) engine=myisam comment "索引建立实体";

create table t_order
(
    orderId                  bigint                auto_increment comment "订单ID",
    goodId                   bigint                not null comment "商品ID",
    userId                   bigint                not null comment "用户ID",
    integration              int                   not null comment "积分",
    tel                      varchar(16)           not null comment "手机号码",
    qq                       varchar(16)           not null comment "qq号码",
    address                  varchar(512)          not null comment "address",
    remark                   varchar(512)          not null comment "备注",
    createTime               bigint                not null comment "创建时间",
    status                   int                   not null comment "订单状态, 1-未处理, 2-已经处理, 3-退单",
    constraint t_order_pk primary key (orderId)
) engine=myisam comment "订单实体";

create table t_user
(
    userId                   bigint               auto_increment comment "用户Id",
    username                 varchar(255)         not null comment "用户帐号",
    password                 char(32)             not null comment "用户密码",
    integration              int                  not null comment "积分",
    registTime               bigint               not null comment "注册时间",
    constraint t_user_pk primary key (userId)
) engine=myisam comment "用户信息实体";
create index index_username on t_user (username);