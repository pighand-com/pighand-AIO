package com.pighand.aio;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.pighand.aio.common.base.ApplicationIdAware;
import com.pighand.aio.common.enums.CacheConfigEnum;
import com.pighand.aio.common.interceptor.AuthorizationInterceptor;
import com.pighand.aio.common.listener.InsertListener;
import com.pighand.framework.spring.PighandFrameworkConfig;
import com.pighand.framework.spring.api.jacksonSerializer.JacksonSerializer;
import com.pighand.framework.spring.api.springdoc.analysis.SpringDocParameter;
import com.pighand.framework.spring.api.springdoc.analysis.SpringDocProperty;
import com.pighand.framework.spring.base.DomainTimeStampAware;
import com.pighand.framework.spring.converter.StringToEnumConverterFactory;
import com.pighand.framework.spring.http.exchange.HttpExchangeRegister;
import com.pighand.framework.spring.listener.DBInsertListener;
import com.pighand.framework.spring.listener.DBUpdateListener;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * spring-boot:run
 *
 * @author wangshuli
 */
@EnableAsync
@SpringBootApplication
@MapperScan("com.pighand.aio.mapper")
@EnableConfigurationProperties({PighandFrameworkConfig.class})
@Import({HttpExchangeRegister.class, JacksonSerializer.class})
public class Application {

    public static void main(String[] args) {
        // 主键生成器
        FlexGlobalConfig.KeyConfig keyConfig = new FlexGlobalConfig.KeyConfig();
        keyConfig.setKeyType(KeyType.Generator);
        keyConfig.setValue(KeyGenerators.flexId);
        keyConfig.setBefore(true);
        FlexGlobalConfig.getDefaultConfig().setKeyConfig(keyConfig);

        // 逻辑删除字段
        FlexGlobalConfig.getDefaultConfig().setLogicDeleteColumn("deleted");

        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PropertyCustomizer propertyCustomizer() {
        return (schema, annotatedType) -> SpringDocProperty.analysis(schema, annotatedType);
    }

    @Bean
    public ParameterCustomizer propertyCustomizers() {
        return (parameterModel, methodParameter) -> SpringDocParameter.analysis(parameterModel, methodParameter);
    }

    @Configuration
    @EnableWebMvc
    public class CorsConfig implements WebMvcConfigurer {

        private static final Logger logger = LoggerFactory.getLogger("mybatis-flex-sql");
        @Resource
        private AuthorizationInterceptor authorizationInterceptor;

        @Value("${access-control-allow.mapping}")
        private String AccessControlAllowMapping;

        @Value("${access-control-allow.origin}")
        private String AccessControlAllowOrigin;

        @Value("${access-control-allow.methods}")
        private String AccessControlAllowMethods;

        @Value("${access-control-allow.headers}")
        private String AccessControlAllowHeaders;

        public CorsConfig() {
            //开启审计功能
            AuditManager.setAuditEnable(true);

            //设置 SQL 审计收集器
            AuditManager.setMessageCollector(
                auditMessage -> logger.info("{},{}ms", auditMessage.getFullSql(), auditMessage.getElapsedTime()));
        }

        /**
         * @param registry
         */
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping(AccessControlAllowMapping).allowedOrigins(AccessControlAllowOrigin)
                .allowedMethods(AccessControlAllowMethods).allowedHeaders(AccessControlAllowHeaders)
                .allowCredentials(false).maxAge(3600);
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(authorizationInterceptor);
        }

    }

    @Configuration
    public class WebMvcConfig implements WebMvcConfigurer {
        /**
         * 枚举类的转换器工厂 addConverterFactory
         */
        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addConverterFactory(new StringToEnumConverterFactory());
        }
    }

    /**
     * 缓存配置
     */
    @EnableCaching
    @Configuration
    public class CacheConfig {
        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager simpleCacheManager = new SimpleCacheManager();

            ArrayList<CaffeineCache> caffeineCaches = new ArrayList<>();
            for (CacheConfigEnum cacheConfig : CacheConfigEnum.values()) {
                Caffeine caffeine = Caffeine.newBuilder().recordStats();
                caffeine.expireAfterWrite(cacheConfig.getTtl(), TimeUnit.SECONDS);
                if (cacheConfig.getMaxSize() != null) {
                    caffeine.maximumSize(cacheConfig.getMaxSize());
                }

                caffeineCaches.add(new CaffeineCache(cacheConfig.getName(), caffeine.build()));
            }
            simpleCacheManager.setCaches(caffeineCaches);

            return simpleCacheManager;
        }
    }

    /**
     * mybatis-flex 配置
     */
    @Configuration
    public class MyBatisFlexConfiguration {

        public MyBatisFlexConfiguration() {

            InsertListener AIOInsertListener = new InsertListener();
            DBInsertListener PHInsertListener = new DBInsertListener();
            DBUpdateListener PHUpdateListener = new DBUpdateListener();

            FlexGlobalConfig config = FlexGlobalConfig.getDefaultConfig();

            config.registerInsertListener(AIOInsertListener, ApplicationIdAware.class);
            config.registerInsertListener(PHInsertListener, DomainTimeStampAware.class);
            config.registerUpdateListener(PHUpdateListener, DomainTimeStampAware.class);
        }
    }
}
