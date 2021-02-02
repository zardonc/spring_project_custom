package com.frameworkcore.aop.support;

import com.frameworkcore.aop.annotation.After;
import com.frameworkcore.aop.annotation.Aspect;
import com.frameworkcore.aop.annotation.Before;
import com.frameworkcore.exception.BeansException;
import com.util.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// aspect缓存
public class AopAspectContainer {
    private static Map<Class<?>, Object> advInstanceMap = new HashMap<>();

    private static Map<String, Method> advBeforeMap = new HashMap<>();

    private static Map<String, Method> advAfterMap = new HashMap<>();

    static {
        Set<Class<?>> classSet = ReflectUtils.getAllClass(Aspect.class);
        for (Class<?> aspClz : classSet) {
            try {
                advInstanceMap.put(aspClz, aspClz.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new BeansException("can not create instance for '" + aspClz.getName() + "'", e);
            }
            for (Method method : aspClz.getDeclaredMethods()) {
                if (method.getAnnotation(Before.class) != null) {
                    advBeforeMap.put(method.getAnnotation(Before.class).value(), method);
                }
                if (method.getAnnotation(After.class) != null) {
                    advBeforeMap.put(method.getAnnotation(After.class).value(), method);
                }
            }
        }
    }

    public static Method getBeforeAdvMethod(String methodName) {
        for (Map.Entry<String, Method> entry : advBeforeMap.entrySet()) {
            if (methodName.matches(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static Method getAfterAdvMethod(String methodName) {
        for (Map.Entry<String, Method> entry : advAfterMap.entrySet()) {
            if (methodName.matches(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static Object getAdvAspectInstance(Class<?> clz) {
        return advInstanceMap.get(clz);
    }
}
