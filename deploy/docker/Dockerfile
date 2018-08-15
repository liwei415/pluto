FROM ccr.ccs.tencentyun.com/bs-base/bingsheng-java8:1.0

VOLUME /var/qianfan123/marketing-test-log/

COPY pluto-web/target/pluto-release.jar /var/qianfan123/marketing-test/app.jar

ENV RUN_LIB="/var/qianfan123/marketing-test/app.jar"
ENV JAVA_OPTS=" -server -Xmx2048m -Xms1500m -Xmn1024m  -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "

RUN sh -c 'touch $RUN_LIB' \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

ENTRYPOINT [ "sh", "-c", "nohup java $JAVA_OPTS -Dsjdbc.self.id.generator.worker.id=$RANDOM$RANDOM -Djava.security.egd=file:/dev/./urandom -jar $RUN_LIB >/var/qianfan123/marketing-test-log/marketing-test-log.log 2>&1" ]