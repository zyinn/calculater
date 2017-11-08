package com.sumscope.optimus.calculator;

import com.sumscope.optimus.calculator.planalysis.DataBaseConfiguration;
import com.sumscope.optimus.commons.cachemanagement.CacheMeAnnAOPProcess;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/** 
 *  
 * MyBatis配置
 *  
 */  
@Configuration
@AutoConfigureAfter({ DataBaseConfiguration.class })
@ComponentScan
@MapperScan(value={"com.sumscope.optimus.calculator.shared.dao.mapper","com.sumscope.optimus.calculator.planalysis.dao.mapper"})
public class MybatisConfiguration{  

    @Resource(name="dataSource")  
    private DataSource dataSource;

    @Bean
    public CacheMeAnnAOPProcess cacheMeAnnAOPProcess(){
        return new CacheMeAnnAOPProcess();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplage() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setFailFast(true);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:/com.sumscope.optimus.calculator.*/*.xml"));
        return new SqlSessionTemplate(sessionFactory.getObject());
    }
}  