package com.pighand.aio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.StringUtils;

/**
 * 该类用于生成全局唯一的 Bean 名称，通过组合包名和类名的方式，
 * 有效解决了不同包下存在相同类名时的 Bean 命名冲突问题。
 *
 * <p>使用方法：
 * 在 Spring Boot 应用主类上添加注解，并指定 nameGenerator 属性为该类，示例如下：
 * <pre>
 * {@code @SpringBootApplication(nameGenerator = UniqueAnnotationBeanNameGenerator.class)}
 * </pre>
 */
@Slf4j
public class UniqueAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        //如果有设置了value，则用value，如果没有则是用全类名
        if (definition instanceof AnnotatedBeanDefinition) {
            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition)definition);

            if (!StringUtils.hasText(beanName)) {
                // 使用默认类名
                beanName = buildDefaultBeanName(definition, registry);
            }

            // 检查当前名字是否已经注入，如果已经存在则使用包名+类名
            if (registry.containsBeanDefinition(beanName)) {
                log.info("Bean名称 {} 已存在,使用全限定类名 {} 作为Bean名称", beanName, definition.getBeanClassName());
                return definition.getBeanClassName();
            }

            return beanName;
        }

        // 使用默认类名
        return buildDefaultBeanName(definition, registry);
    }
}
