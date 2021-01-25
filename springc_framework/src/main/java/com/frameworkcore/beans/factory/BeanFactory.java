package com.frameworkcore.beans.factory;

public interface BeanFactory {
    Object getBean(String name);
    <T> T getBean(Class<T> requireType);
    boolean containsBean(String name);
}
