package com.galewings.aspect;

import com.galewings.annotation.FunctionCtrlAspect;
import com.galewings.exception.FunctionCtrlException;
import com.galewings.repository.FunctionCtrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class FunctionCtrlValidatorTest {
    @Mock
    FunctionCtrlRepository functionCtrlRepository;
    @InjectMocks
    FunctionCtrlValidator functionCtrlValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateFunctionCtrl() {
        when(functionCtrlRepository.getFlg(anyString())).thenReturn("getFlgResponse");

        functionCtrlValidator.validateFunctionCtrl(null, createFunctionCtrlAspect("test"));
    }

    @Test
    void testValidateFunctionCtrlThrowFunctionCtrlException() {
        when(functionCtrlRepository.getFlg(anyString())).thenReturn(FunctionCtrlRepository.FunctionCtrlFlg.OFF.getCode());

        try {
            functionCtrlValidator.validateFunctionCtrl(null, createFunctionCtrlAspect("test"));
            fail();
        } catch (FunctionCtrlException e) {
            // 想定通り
        }
    }

    private FunctionCtrlAspect createFunctionCtrlAspect(String value) {
        return new FunctionCtrlAspect() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme