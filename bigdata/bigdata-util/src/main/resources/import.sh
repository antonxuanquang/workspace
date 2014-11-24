#!/bin/sh
#JAVA_HOME="/home/sean/workspace/jdk7"

# 程序目录
APP_HOME=$(dirname "$0")
APP_HOME=$(cd "$APP_HOME"; pwd)
APP_HOME=${APP_HOME}"/../"
echo APP_HOME=$APP_HOME

# JVM参数
JAVA_OPTS="-client -Xms128m -Xmx256m"

# 添加lib下所有类库到classpath
for jarfile in ${APP_HOME}/lib/*.jar
do
	CLASSPATH=${CLASSPATH}:${jarfile} 
done

# 添加配置文件目录到classpath
CLASSPATH=${CLASSPATH}:${APP_HOME}/conf

${JAVA_HOME}/bin/java ${JAVA_OPTS} \
	-cp ${CLASSPATH} com.sean.bigdata.util.HDFSImportor \
	-hdfs hdfs://master:8020 \
	-file /tmp/viewlog/count/value_1 \
	-split '\t' \
	-reportId 1 \
	-userKey sean \
	-time 20041101000000 \
	-check true