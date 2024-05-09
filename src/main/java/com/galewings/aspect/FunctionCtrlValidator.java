package com.galewings.aspect;

import com.galewings.annotation.FunctionCtrlAspect;
import com.galewings.repository.FunctionCtrlRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FunctionCtrlValidator {

    @Autowired
    private FunctionCtrlRepository functionCtrlRepository;

    @Before("@within(functionCtrlAspect) && execution(public * *.*(..))")
    public void validateFunctionCtrl(JoinPoint joinPoint, FunctionCtrlAspect functionCtrlAspect) {
        String flg = functionCtrlRepository.getFlg(functionCtrlAspect.value());

        if (FunctionCtrlRepository.FunctionCtrlFlg.OFF.getCode().equals(flg)) {
            throw new FunctionCtrlException();
        }
    }
}