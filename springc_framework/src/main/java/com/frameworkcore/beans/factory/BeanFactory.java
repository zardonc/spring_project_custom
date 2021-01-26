package com.frameworkcore.beans.factory;

// 基础类型 IoC 容器，提供完整的 IoC 服务支持
// 采取延迟加载 在第一次调用getBean的时候才会初始化
public interface BeanFactory {
    Object getBean(String name);

    <T> T getBean(Class<T> requireType);

    boolean containsBean(String name);
}
