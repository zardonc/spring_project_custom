package com.frameworkcore.beans;

import com.frameworkcore.beans.factory.BeanFactory;
import com.frameworkcore.beans.factory.config.ComponentReader;
import com.frameworkcore.beans.factory.support.DefaultBeanFactory;
import com.frameworkcore.beans.processor.DefaultAdvisorProxyCreator;

/**
 * 在BeanFactory的基础上构建，实现了 BeanFactory。
 * new ApplicationContext()时，会执行读取所有的 Bean 转化成 BeanDefinition
 * ，并对所有的 BeanDefinition 执行 getBean() 获取所有 Bean 的实例
 * , 存放在 SingletonBeanRegistry 当中
 */
public class ApplicationContext implements BeanFactory {

    private DefaultBeanFactory beanFactory = new DefaultBeanFactory();

    public ApplicationContext() {
        loadBeanDefinitions(beanFactory);
        postProcessBeanFactory(beanFactory);
        finishBeanFactoryInitialization(beanFactory);
    }

    private void loadBeanDefinitions(DefaultBeanFactory defaultBeanFactory) {
        ComponentReader componentReader = new ComponentReader();
        componentReader.readBeanDefinition(defaultBeanFactory);
    }

    private void postProcessBeanFactory(DefaultBeanFactory beanFactory) {
        beanFactory.addBeanProcessor(new DefaultAdvisorProxyCreator());
    }

    public void finishBeanFactoryInitialization(DefaultBeanFactory beanFactory) {
        beanFactory.preInstantiateSingleton();
    }

    @Override
    public Object getBean(String name) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requireType) {
        return getBeanFactory().getBean(requireType);
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    public DefaultBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
