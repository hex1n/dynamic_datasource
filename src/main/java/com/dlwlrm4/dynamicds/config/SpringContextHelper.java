package com.dlwlrm4.dynamicds.config;
 
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
 
/**
 * Spring 上下文辅助类
 * @author jelly
 *
 */
@Component
public class SpringContextHelper implements ApplicationContextAware {
       private static ApplicationContext applicationContext; 
        
       @Override 
       public void setApplicationContext(ApplicationContext applicationContext) 
               throws BeansException { 
           SpringContextHelper.applicationContext = applicationContext; 
       } 
          
       public static ApplicationContext getApplicationContext(){ 
           return applicationContext; 
       } 
          
       public static Object getBean(String name){ 
           return applicationContext.getBean(name); 
       } 
        /**
         * 从spring 上下文中获取bean
         * @param name
         * @param requiredClass
         * @return
         */
       public static <T> T getBean(String name, Class<T>  requiredClass){ 
           return applicationContext.getBean(name, requiredClass); 
       } 
       public  static <T> T getBean(Class<T> requiredType){
           return applicationContext.getBean(requiredType);
       }
   
      
}