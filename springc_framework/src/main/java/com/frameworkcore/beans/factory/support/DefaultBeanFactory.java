package com.frameworkcore.beans.factory.support;

import com.frameworkcore.beans.factory.BeanFactory;
import com.frameworkcore.beans.factory.SingletonBeanRegistry;
import com.frameworkcore.beans.factory.config.BeanDefinition;
import com.frameworkcore.beans.factory.config.BeanPostProcessor;
import com.frameworkcore.exception.BeansException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// BeanFactory的实现, 作为ApplicationContext的成员变量

/**
 * 调用链条 getBean() 是入口，其调用链为：getBean()->doGetBean()获取Bean如果不存在则创建->doCreateBean()->
 * createBeanInstance()创建 Bean 的实例->populateBean()Bean属性的自动装配。
 */
public class DefaultBeanFactory extends SingletonBeanRegistry implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(64);

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    public void addBeanProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public void preInstantiateSingleton() {
        this.beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            getBean(beanName);
        });
    }

    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName);
    }

    private <T> T doGetBean(String beanName) {
        Object bean;
        Object sharedInstance = getSingletonObject(beanName, true);
        if (sharedInstance != null) {
            bean = sharedInstance;
        } else {
            BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
            if (beanDefinition == null) {
                throw new BeansException("can not find the definition of bean '" + beanName + "'");
            }
            bean = getSingleton(beanName, () -> {
                try {
                    return doCreateBean(beanName, beanDefinition);
                } catch (Exception ex) {
                    removeSingleton(beanName);
                    throw ex;
                }
            });
        }
        return (T) bean;
    }

    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = createBeanInstance(beanName, beanDefinition);
        boolean earlySingletonExposure = isSingletonCurrentlyInCreation(beanName);
        if (earlySingletonExposure) {
            addSingletonFactory(beanName, () -> bean);
        }
        Object exposedObj = bean;
        populateBean(beanName, beanDefinition, bean);
        if (earlySingletonExposure) {
            Object earlySingletonReference = getSingletonObject(beanName, false);
            if (earlySingletonReference != null) {
                exposedObj = earlySingletonReference;
            }
        }
        return exposedObj;
    }

    private Object createBeanInstance(String beanName, BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?> constructorInUse;
        if (beanClass.isInterface()) {
            throw new BeansException("Specified class '" + beanName + "' is an interface");
        }
        try {
            constructorInUse = beanClass.getDeclaredConstructor((Class<?>[]) null);
            return constructorInUse.newInstance();
        } catch (Exception ex) {
            throw new BeansException("'" + beanName + "',No default constructor found", ex);
        }
    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, Object beanInstance) {
        Field[] beanFields = beanDefinition.getClass().getDeclaredFields();
        try {
            for (Field beanField : beanFields) {
                if (beanField.getAnnotation(Resource.class) == null) {
                    continue;
                }
                if (!containsBean(beanField.getName())) {
                    throw new BeansException("'@Resource' for field '" + beanField.getClass().getName() + "' can not find");
                }
                beanField.setAccessible(true);
                beanField.set(beanInstance, getBean(beanField.getName()));
            }
        } catch (IllegalAccessException e) {
            throw new BeansException("populateBean '" + beanName + "' error", e);
        }
    }

    private boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public <T> T getBean(Class<T> requireType) {
        return null;
    }

    @Override
    public boolean containsBean(String beanName) {
        return this.containSingletong(beanName) || this.containsBean(beanName);
    }
}
