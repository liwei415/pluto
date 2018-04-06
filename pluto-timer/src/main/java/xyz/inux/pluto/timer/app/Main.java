package xyz.inux.pluto.timer.app;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
//import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = {"xyz.inux.pluto.timer"})
@EnableAsync
@EnableScheduling
//@EnableHystrix
//@EnableEurekaClient
//@EnableFeignClients(basePackages={"com.qianfan123.sailing.xxxxxx.domain.ext"})
public class Main extends SpringBootServletInitializer {
    // war启动入口
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return configureApplication(builder);
    }

    // jar启动入口
    public static void main(String[] args) throws Exception {
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(Main.class).bannerMode(Banner.Mode.CONSOLE).logStartupInfo(true).registerShutdownHook(true).web(true);
    }
}