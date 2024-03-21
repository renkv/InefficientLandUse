package com.land.modular.task;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler  implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable e, Method method, Object... args) {
        //处理异常
    }
    /*@Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }*/
}
