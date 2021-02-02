package com.frameworkcore.aop;

import com.frameworkcore.aop.support.DefaultBeanProxy;
import net.sf.cglib.proxy.Enhancer;

public class AopProxy {
    private DefaultBeanProxy beanProxy = new DefaultBeanProxy();

    // 获取代理类
    public Object getProxy(Class<?> clz) {
        Enhancer enhancer = new Enhancer();
        // 设置哪个类需要代理
        enhancer.setSuperclass(clz);
        enhancer.setCallback(beanProxy);
        return enhancer.create();
    }
}
