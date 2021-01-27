package com.frameworkcore.beans.factory.config;

/**
 * 用于对于新构造的实例做自定义的修改，比如如何构造、属性值的修改、构造器的选择等等。
 * 典型应用Spring AOP, 最终将代理对象放入Spring容器
 *
 * 流程：
 * ===Spring IOC容器实例化Bean===
 * ===调用BeanPostProcessor的postProcessBeforeInitialization方法===
 * ===调用bean实例的初始化方法(invokeInitMethods())===
 * ===调用BeanPostProcessor的postProcessAfterInitialization方法===
 *
 */

public interface BeanPostProcessor {
    // 初始化前调用
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    // 初始化后调用
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
