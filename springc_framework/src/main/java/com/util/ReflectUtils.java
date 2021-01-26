package com.util;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ReflectUtils {
    private static final Reflections reflections = new Reflections("");

    public static Set<Class<?>> getAllClass(Class<? extends Annotation> clazz) {
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(clazz);
        return classSet;
    }
}
