package com.frameworkcore.aop.support;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

// cglib代理对象handler, 生成原对象obj的代理对象proxy
public class DefaultBeanProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Method beforeMethod = AopAspectContainer.getBeforeAdvMethod(method.getName());
        if (beforeMethod != null) {
            beforeMethod.invoke(AopAspectContainer.getAdvAspectInstance(beforeMethod.getDeclaringClass()), args);
        }

        Method afterMethod = AopAspectContainer.getAfterAdvMethod(method.getName());
        if (afterMethod != null) {
            afterMethod.invoke(AopAspectContainer.getAdvAspectInstance(afterMethod.getDeclaringClass()), args);
        }

        // 执行被代理类的方法
        return methodProxy.invokeSuper(o, args);
    }
}
