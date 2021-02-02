package com.frameworkcore.beans.processor;

import com.frameworkcore.aop.AopProxy;
import com.frameworkcore.beans.factory.config.BeanPostProcessor;

// 用于在ioc容器中植入AOP, 通过动态代理返回代理对象
public class DefaultAdvisorProxyCreator implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Object proxyBean = new AopProxy().getProxy(bean.getClass());
        return proxyBean == null ? bean : proxyBean;
    }
}
