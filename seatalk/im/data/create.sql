drop database im_db;

create database im_db character set utf8 default character set utf8 collate utf8_general_ci default collate utf8_general_ci;
use im_db;

create table t_friend
(
	id				bigint			auto_increment comment "id",
	userId			bigint			not null comment "用户Id",
	friendId		bigint			not null comment "好友Id",
	groupId			bigint			not null comment "分组Id",
	remark			varchar(128) 	null comment "备注",
	constraint t_friend_pk primary key(id)
) comment "好友";

create table t_group
(
	id				bigint			auto_increment comment "id",
	name			varchar(255)	not null comment "分组名称",
	userId			bigint			not null comment "所属用户",
	isDefault		int				not null comment "是不是默认分组",
	constraint t_group_pk primary key(id)
) comment "分组";

create table t_message
(
	id				bigint			auto_increment comment "id",
	senderId		bigint			not null comment "发送者Id",
	receiverId		bigint			not null comment "接受者Id",
	sendTime		bigint			not null comment "发送时间",
	content			varchar(1024)	not null comment "消息内容",
	isRead			int				not null comment "是否已经读取",
	type			int				not null comment "消息类型",
	ownerId			bigint			not null comment"",
	constraint t_message_pk primary key(id)
) comment "消息";

create table t_userinfo
(
	id				bigint			auto_increment comment "id",
	username		varchar(255)	not null comment "用户名",
	password		varchar(255)	not null comment "密码",
	nickname		varchar(255)	not null comment "昵称",
	signature		varchar(1024)	not null comment "个性签名",
	country			int				not null comment "国家",
	sex				int				not null comment "性别",
	age				int				not null comment "年龄",
	tel				varchar(255)	not null comment "电话",
	mail			varchar(255)	not null comment "邮箱",
	language		int				not null comment "语言",
	description		varchar(1024)	not null comment "个人说明",
	head			int				not null comment "头像Id",
	status			int				not null comment "用户状态",
	translator 		varchar(10)		null comment "翻译语言",
	constraint t_userinfo_pk primary key(id)
) comment "用户实体";

create table t_file
(
	id				bigint			auto_increment comment "id",
	filename		varchar(255)	not null comment "原文件名",
	length			bigint			not null comment "文件字节长度",
	path			varchar(255)	not null comment "文件相对路径",
	type			int				not null comment "类型",
	constraint t_file_pk primary key(id)
) comment "文件";

create table t_flock
(
	id				bigint			auto_increment comment "id",
	name			varchar(128)	not null comment "群名称",
	signature		varchar(255)    null comment "群签名",
	description		varchar(512)	null comment "群介绍",
	creater			bigint  		not null comment "创建人",
	createTime		bigint  		not null comment "创建时间",
	constraint t_flock_pk primary key(id)
) comment "群";

create table t_flock_member
(
	id				bigint			auto_increment comment "id",
	userId			bigint			not null comment "用户ID",
	flockId			bigint			not null comment "群ID",
	joinTime		bigint    		null comment "加入时间",
	isAdmin			int				not null comment "是否管理员",
	constraint t_flock_member_pk primary key(id)
) comment "群成员";

create table t_flock_message
(
	id				bigint			auto_increment comment "id",
	senderId		bigint			not null comment "发送者Id",
	flockId			bigint			not null comment "群Id",
	sendTime		bigint			not null comment "发送时间",
	content			varchar(1024)	not null comment "消息内容",
	type			int				not null comment "消息类型",
	constraint t_flock_message_pk primary key(id)
) comment "群消息";

create table t_flock_card
(
	id				bigint			auto_increment comment "id",
	flockId			bigint			not null comment "群Id",
	userId			bigint			not null comment "用户Id",
	name			varchar(32)		null comment "姓名",
	tel				varchar(20)		null comment "电话",
	email			varchar(64)		null comment "邮箱",
	description		varchar(512)	null comment "个人说明",
	constraint t_flock_card_pk primary key(id)
) comment "群名片";

create table t_ad
(
	id				bigint			auto_increment comment "id",
	link			varchar(255)	not null comment "跳转链接",
	imgUrl			varchar(255)	not null comment "跳转链接",
	constraint t_ad_pk primary key(id)
) comment "广告";

create table t_icon
(
	id				bigint			auto_increment comment "id",
	link			varchar(255)	not null comment "跳转链接",
	imgUrl			varchar(255)	not null comment "跳转链接",
	visible			int				not null comment "跳转链接",
	constraint t_icon_pk primary key(id)
) comment "";


insert into t_userinfo values(1,'sean_zwx','xuxu','Sean','It was all meant to be',1,1,22,'15980546806','sean_zwx@163.com',1,'I am Sean',1,0,null);
insert into t_userinfo values(2,'sean_yaya','xuxu','Yaya','It was all meant to be',1,1,22,'15980546806','sean_zwx@163.com',1,'I am Sean',1,0,null);
insert into t_userinfo values(3,'sean_tom','xuxu','Tom','It was all meant to be',1,1,22,'15980546806','sean_zwx@163.com',1,'I am Sean',1,0,null);
insert into t_userinfo values(4,'sean_tom2','xuxu','Tim','It was all meant to be',1,1,22,'15980546806','sean_zwx@163.com',1,'I am Sean',1,0,null);
insert into t_userinfo values(5,'sean_tom3','xuxu','FUk','It was all meant to be',1,1,22,'15980546806','sean_zwx@163.com',1,'I am Sean',1,0,null);
insert into t_userinfo values(6,'sean_tom4','xuxu','Dop','It was all meant to be',1,1,22,'15980546806','sean_zwx@163.com',1,'I am Sean',1,0,null);

insert into t_group values(1,'我的好友',1,1);
insert into t_group values(2,'我的同学',1,0);
insert into t_group values(3,'我的家人',1,0);
insert into t_group values(4,'我的好友',2,1);

insert into t_friend values(1,1,2,1,"");
insert into t_friend values(2,1,3,1,"");
insert into t_friend values(3,1,4,2,"");
insert into t_friend values(4,1,5,2,"");
insert into t_friend values(5,1,6,3,"");
insert into t_friend values(6,2,1,4,"");

insert into t_ad values(1,"http://www.baidu.com", "http://www.baidu.com/img/bdlogo.png");

insert into t_icon values(1,"http://www.baidu.com", "http://cdn-img.easyicon.net/png/5525/552594.png", 1);
insert into t_icon values(2,"http://www.baidu.com", "http://cdn-img.easyicon.net/png/5215/521573.png", 1);