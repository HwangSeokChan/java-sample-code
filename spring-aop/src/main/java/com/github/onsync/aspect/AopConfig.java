package com.github.onsync.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.github.onsync.*", includeFilters = @ComponentScan.Filter(value = Aspect.class))
public class AopConfig {
}
