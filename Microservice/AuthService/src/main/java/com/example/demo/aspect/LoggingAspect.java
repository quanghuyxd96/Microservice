package com.example.demo.aspect;

import com.example.demo.response.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.demo.controller.AuthRestController.login(..)) and args(userName,password)")
    public ResponseEntity<Token> profileAllMethods(ProceedingJoinPoint proceedingJoinPoint, String userName, String password) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        List<String> strings = Arrays.stream(methodSignature.getParameterNames()).collect(Collectors.toList());
        //demo chặn đầu
//        if(userName.equals("admin")){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ResponseEntity<Token> token = (ResponseEntity<Token>) proceedingJoinPoint.proceed();
        stopWatch.stop();
        System.out.println(token.getBody().getToken());
        //demo chặn đuôi
//        if(token.getBody().getToken().length()>0){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
        //Log method execution time
        LOGGER.info("Execution time of " + className + "." + methodName + " : " + stopWatch.getTotalTimeMillis() + " ms");
        return token;
    }

    @Before(value = "execution(* com.example.demo.service.AuthService.generateTokenByUserName(..))")
    public void beforeCheckDeliveryItemUndelivery(JoinPoint joinPoint) {
        LOGGER.info("Before method:" + joinPoint.getSignature());
    }
}