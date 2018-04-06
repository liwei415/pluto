package xyz.inux.pluto.domain.config;

//import com.qianfan123.sailing.falcon.core.common.multds.DataSourceConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages={"xyz.inux.pluto.domain.dao"},
            sqlSessionFactoryRef="sessionFactory")
@EnableTransactionManagement
//@ConditionalOnMissingBean({DataSourceConfiguration.class})
@AutoConfigureOrder(-100)
public class DatasourceConfig {
	protected static Logger logger = LoggerFactory.getLogger("DAO.LOG");

	@Value("${mybatis.config_location}")
	private String mybatisConfig;
	@Value("${mybatis.mapper_locations}")
	private String mybatisMapper;
	@Value("${spring.datasource.type}")
	private Class<? extends DataSource> datasourceType;

	@Bean(name = "dataSource", destroyMethod = "close")
	@Primary
	@ConfigurationProperties(prefix = "datasource.master")
	public DataSource dataSource(){
	    return DataSourceBuilder.create().type(datasourceType).build();
	}

	@Bean(name = "sessionFactory")
	@ConfigurationProperties(prefix = "bean/mybatis")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
	    SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();

	    /**
	     * 注意！利用系统自带功能无法正常加载这两个部分配置文件,需要再次手动加载
	     */
	    PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
	    /** 设置mybatis configuration 扫描路径 */
	    sqlSessionFactory.setConfigLocation(new ClassPathResource(mybatisConfig));
	    /** 添加mapper 扫描路径 */
	    sqlSessionFactory.setMapperLocations(pathMatchingResourcePatternResolver.getResources(mybatisMapper));

	    sqlSessionFactory.setDataSource(dataSource);

	    return sqlSessionFactory.getObject();
	}

	@Bean(name = "transactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
	    return new DataSourceTransactionManager(dataSource);
    }

	@Bean(name = "transactionTemplate")
	public TransactionTemplate transactionTemplate(@Qualifier("transactionManager") DataSourceTransactionManager transactionManager) throws Exception {
		DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		defaultTransactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
		defaultTransactionDefinition.setTimeout(60); // 秒钟

	    return new TransactionTemplate(transactionManager, defaultTransactionDefinition);
	}
}
