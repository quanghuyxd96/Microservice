package com.example.demo.aop;

import com.example.demo.response.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect
{
    private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);
    private org.slf4j.Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    //AOP expression for which methods shall be intercepted
    @Around("execution(* com.example.demo.controller.AuthRestController.login(..)) and args(userName,password)")
    public ResponseEntity<Token> profileAllMethods(ProceedingJoinPoint proceedingJoinPoint, String userName, String password) throws Throwable
    {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        List<String> strings = Arrays.stream(methodSignature.getParameterNames()).collect(Collectors.toList());

        System.out.println(strings.get(0));
        System.out.println(userName);
        System.out.println("CHữ kí: " +methodSignature);

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();

        //Measure method execution time
        stopWatch.start();
//        Object result = proceedingJoinPoint.proceed();
        ResponseEntity<Token> token = (ResponseEntity<Token>) proceedingJoinPoint.proceed();
        stopWatch.stop();
        System.out.println(token.getBody().getToken());

        //Log method execution time
        LOGGER.info("Execution time of " + className + "." + methodName + " : " + stopWatch.getTotalTimeMillis() + " ms");

        return token;
    }

    @Before(value = "execution(* com.example.demo.service.AuthService.generateTokenByUserName(..))")
    public void beforeCheckDeliveryItemUndelivery(JoinPoint joinPoint) {
        LOGGER.info("Before method:" + joinPoint.getSignature());
    }
}