package com.github.onsync.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
@Log4j2
public class LoggingAspect {

    @Pointcut("execution(* com.github.onsync..*(..))")
    public void allComponent() {}

    @Pointcut("@annotation(LogExecutionTime)")
    public void logExecutionTime() {}

    @Around("logExecutionTime()")
    public void aroundExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        if (log.isInfoEnabled()) {
            log.atInfo().log(joinPoint.getSignature() + " : " + executionTime + "ms");
        }
    }

    @Before("allComponent()")
    public void beforeExecute(JoinPoint joinPoint) {
        if (log.isInfoEnabled()) {
            log.atInfo().log(joinPoint.getTarget());
        }
        if (log.isDebugEnabled()) {
            log.atDebug().log(joinPoint.getArgs());
            log.atDebug().log(joinPoint.getKind());
            log.atDebug().log(joinPoint.getSignature());
            log.atDebug().log(joinPoint.getStaticPart());
            log.atDebug().log(joinPoint.getSourceLocation());
            log.atDebug().log(joinPoint.toShortString());
            log.atDebug().log(joinPoint.toLongString());
        }
    }

    @After("allComponent()")
    public void afterExecute(JoinPoint joinPoint) {
        // Returning & Throwing 상관없는 별도 로직 추가
    }

    @AfterReturning(pointcut = "allComponent()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        if (log.isDebugEnabled()) {
            log.atDebug().log(returnValue.toString());
        }
    }

    @AfterThrowing(pointcut = "allComponent()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        if (log.isErrorEnabled()) {
            log.atError().log(exception);
        }
    }
}
