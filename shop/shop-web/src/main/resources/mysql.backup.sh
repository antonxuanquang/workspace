#!/bin/sh

day=$(date -d '1 day ago' +%Y%m%d); 
echo "mysql开始备份${day}数据..."
echo "当前时间是:"
date

mysqldump -hlocalhost -uroot -pxuxu shop_db > /root/backup/${day}.sql
# mysqldump -hlocalhost -uroot -pxuxu shop_db > /home/sean/Desktop/backup.sql
# mysqldump -hlocalhost -uroot -pxuxu shop_db t_good > /home/sean/Desktop/t_good.sql

echo "mysql备份结束"
echo "当前时间是:"
date