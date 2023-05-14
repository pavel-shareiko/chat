package by.shareiko.chat.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LoggingAspect {
    @Pointcut("execution(* by.shareiko.chat.service.*.*(..))")
    public void servicePointcut() {}

    @Around("servicePointcut()")
    public Object logServices(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());

        logger.debug("Enter: {} with arguments: {}", signature.getName(), Arrays.toString(joinPoint.getArgs()));
        Object result = joinPoint.proceed();
        logger.debug("Exit: {} with result: {}", signature.getName(), result);
        return result;
    }
}
