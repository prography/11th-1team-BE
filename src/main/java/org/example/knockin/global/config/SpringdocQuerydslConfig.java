package org.example.knockin.global.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocQuerydslConfig implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String buggyBeanName = "queryDslQuerydslPredicateOperationCustomizer";
        if (registry.containsBeanDefinition(buggyBeanName)) {
            registry.removeBeanDefinition(buggyBeanName);
        }
    }
}