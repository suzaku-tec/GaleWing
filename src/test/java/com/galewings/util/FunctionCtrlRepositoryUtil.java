package com.galewings.util;

import com.galewings.entity.FunctionCtrl;

public class FunctionCtrlRepositoryUtil {

    public static FunctionCtrl createFunctionCtrlMock(String id, String flg) {
        FunctionCtrl functionCtrl = new FunctionCtrl();
        functionCtrl.id = id;
        functionCtrl.flg = flg;

        return functionCtrl;
    }

}
