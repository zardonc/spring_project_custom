package com.frameworkcore.beans.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// 三级缓存代码
public class SingletonBeanRegistry {
    private static final Logger log = LogManager.getLogger("SingletonBeanRegistry.class");

    private static final Object NULL_OBJECT = new Object();
    // 一级缓存，用于存放完全初始化好的 bean，从该缓存中取出的 bean 可以直接使用。
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(64);
    // 二级缓存，用于存放提前曝光的单例对象的cache，原始的 bean 对象（尚未填充属性）。
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<String, Object>(64);
    // 三级缓存
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
    // 用于存放正在被创建的 Bean 对象。
    private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));

    protected Object getSingletonObject(String beanName, boolean allowEarlyReference) {
        // 一级缓存中获取
        Object singletonObject = this.singletonObjects.get(beanName);
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            synchronized (this.singletonObjects) {
                // 尝试二级缓存获取
                singletonObject = this.earlySingletonObjects.get(beanName);
                if (singletonObject == null && allowEarlyReference) {
                    ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                    if (singletonFactory != null) {
                        singletonObject = singletonFactory.getObject();
                        // 升级至为二级缓存
                        this.earlySingletonObjects.put(beanName, singletonObject);
                        this.singletonFactories.remove(beanName);
                    }
                }
            }
        }
        return singletonObject != NULL_OBJECT ? singletonObject : null;
    }

    protected Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjects) {
            Object singletonObject = singletonObjects.get(beanName);
            if (singletonObject == null) {
                this.singletonsCurrentlyInCreation.add(beanName);
                singletonObject = singletonFactory.getObject();
                this.singletonsCurrentlyInCreation.remove(beanName);
                addSingleton(beanName, singletonObject);
            }
            return singletonObject != NULL_OBJECT ? singletonObject : null;
        }
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, (singletonObject != null ? singletonObject : NULL_OBJECT));
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
        }
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(beanName)) {
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
            }
        }
    }

    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.singletonFactories.remove(beanName);
        }
    }

    // 正在创建中
    protected boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    protected boolean containSingletong(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }
}
