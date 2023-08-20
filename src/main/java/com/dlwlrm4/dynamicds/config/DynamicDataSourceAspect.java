package com.dlwlrm4.dynamicds.config;

import com.alibaba.fastjson.JSON;
import com.dlwlrm4.dynamicds.entity.DataSourceEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * @Author hex1n
 * @Date 2023/8/20/12:13
 * @Description
 **/
@Aspect
@Component
@Order(1)
@Slf4j
public class DynamicDataSourceAspect {

    @Pointcut("execution(* com.dlwlrm4.dynamicds.controller.TestController.*(..))")
    public void pointCut() {

    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取要切换的数据源
        Object[] args = point.getArgs();
        Optional<Object> result = Arrays.stream(args).filter(DataSourceEntity.class::isInstance).findFirst();
        log.info("around 切换的数据源:{}", JSON.toJSONString(result));
        if (result.isEmpty()) {
            return point.proceed();
        }
        try {
            DataSourceEntity dataSource = (DataSourceEntity) result.get();
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.getDatabaseName());
            return point.proceed();
        } finally {
            DynamicDataSourceContextHolder.removeDataSourceType();
        }
    }

    private Method getTargetMethod(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }
}
