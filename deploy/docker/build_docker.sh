#!/bin/bash

#当前目录
PWD=`pwd`

# 仓库及镜像定义
REPO_DOMAIN="ccr.ccs.tencentyun.com"
DOCKER_NAME="marketing/marketing-test"
DOCKER_FILE="docker/Dockerfile"

# jar包路径
JAR_PATH="$PWD/pluto-web/target/pluto-release.jar"

# 参数验证
if [ !$1 ];then
  echo '环境变量未指定: ./command.sh env';
  exit;
fi

#环境变量
APP_ENV=$1

# 镜像版本
VERSION=`date '+%Y%m%d%H%M%S'`

# maven 打包
mvn clean package -Denv=$APP_ENV -DskipTests=true

# 删除原有本地最新版本镜像
docker rmi "$DOCKER_NAME:latest"
docker images | grep "$REPO_DOMAIN/$DOCKER_NAME" | awk '{print $3}'| xargs docker rmi

#生成docker镜像
docker build -t "$DOCKER_NAME:latest" -f $DOCKER_FILE .

#打docker镜像tag
docker tag "$DOCKER_NAME:latest" "$REPO_DOMAIN/$DOCKER_NAME:$VERSION"

#push到阿里云
docker push "$REPO_DOMAIN/$DOCKER_NAME:$VERSION"