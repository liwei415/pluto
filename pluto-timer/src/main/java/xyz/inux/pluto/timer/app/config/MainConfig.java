package xyz.inux.pluto.timer.app.config;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;

import xyz.inux.pluto.domain.config.RedisCacheConfig;
import xyz.inux.pluto.domain.config.DatasourceConfig;

@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@Import(value={
//        PerformFramework.class,
//        DataSourceConfiguration.class,
        DatasourceConfig.class,
        RedisCacheConfig.class,
//        WebMvcConfiguration.class
})
@ComponentScan(
        value = {"xyz.inux.pluto.timer"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)
        })
public class MainConfig {

}
