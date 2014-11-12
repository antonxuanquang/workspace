#!/bin/sh
# vi /etc/crontab

# mysql 每天0点执行
0 0 * * * root /root/timer/mysql.backup.sh 1 > /root/timer/log

# 重建商品索引, 每小时执行一次
0 5-23 * * * root curl http://localhost:8080/shop-web/api/v1/BuildIndexAction?password=97igo\&indexType=1

# 重建搜索词索引, 每天凌晨1点执行
0 1 * * * root curl http://localhost:8080/shop-web/api/v1/BuildIndexAction?password=97igo\&indexType=2

# 重新计算搜索热词, 每天凌晨2点执行
0 2 * * * root curl http://localhost:8080/shop-web/api/v1/UpdateHotwordAction?password=97igo

# 计算商品分类, 每天凌晨3点执行
0 3 * * * root curl http://localhost:8080/shop-web/api/v1/UpdateCategoryAction?password=97igo&forceUpdate=0
