package xyz.inux.pluto.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SampleScheduled {

    private static final Logger logger = LoggerFactory.getLogger(SampleScheduled.class);

    /**
     * cron 定时执行
     * 秒	 	0-59	 	, - * /
     * 分	 	0-59	 	, - * /
     * 小时	 	0-23	 	, - * /
     * 日期	 	1-31	 	, - * ? / L W C
     * 月份	 	1-12 或者 JAN-DEC	 	, - * /
     * 星期	 	1-7 或者 SUN-SAT	 	, - * ? / L C #
     * 年（可选）	 	留空, 1970-2099	 	, - * /
     */
    @Scheduled(cron="*/5 * * * * ?")
    public void executeSample01Task() {
        System.out.println("定时任务1: cron");
    }

    /**
     * fixedDelay 上次任务结束之后多长时间执行
     */
    @Scheduled(fixedDelay = 1000 * 200)
    public void executeSample02Task() {
        System.out.println("定时任务1: fixedDelay");
    }

    /**
     * fixedRate 上次任务开始之后多长时间执行
     */
    @Scheduled(fixedRate = 1000 * 600)
    public void executeSample03Task() {
        System.out.println("定时任务1: fixedRate");
    }

}