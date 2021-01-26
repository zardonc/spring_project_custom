package com.frameworkcore.beans.factory.config;

import com.frameworkcore.annotation.Component;
import com.frameworkcore.beans.factory.support.DefaultBeanFactory;
import com.util.ReflectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class ComponentReader {
    public void readBeanDefinition(DefaultBeanFactory beanFactory) {
        Set<Class<?>> componentSet = ReflectUtils.getAllClass(Component.class);
        componentSet.forEach((componentClass) -> {
            BeanDefinition beanDefinition = new BeanDefinition();
            String beanName = componentClass.getAnnotation(Component.class).value();
            if (StringUtils.isEmpty(beanName)) {
                beanName = componentClass.getSimpleName();
            }
            beanDefinition.setBeanClass(componentClass);
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
        });
    }
}
