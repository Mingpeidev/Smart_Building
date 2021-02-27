package cn.mao.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext appCtx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // TODO Auto-generated method stub
        appCtx = applicationContext;
    }

    public static Object getBean(String beanName) {

        return appCtx.getBean(beanName);

    }

    public static <T> T getBean(Class<T> clz) {

        return (T) appCtx.getBean(clz);

    }

}
