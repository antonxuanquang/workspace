drop database bigdata_db; 
create database bigdata_db character set utf8 default character set utf8 collate utf8_general_ci default collate utf8_general_ci;

use bigdata_db;

create table t_report
(
    reportId   bigint   comment "报表ID",
    reportName   varchar(255)   comment "报表名称",
    xAxis   varchar(255)   comment "x轴说明",
    yAxis   varchar(255)   comment "y轴说明",
    conditions   varchar(255)  null comment "条件分组, 只用于列表报表, 多个用;隔开",
    type   tinyint   comment "报表类型:1-单值, 2-数值, 3-列表",
    countType   tinyint   comment "统计类型:1-日统计, 2-月统计",
    createTime   bigint   comment "创建时间",
    creater   bigint   comment "创建人",
    constraint t_report_pk primary key (reportId)
) engine=myisam comment "报表实体";

create table t_execute
(
    executeId   bigint   comment "报表ID",
    reportId   bigint   comment "报表名称",
    executeTime   bigint   comment "执行时间",
    result   varchar(255)   comment "执行结果json",
    constraint t_execute_pk primary key (executeId)
) engine=myisam comment "执行实体";