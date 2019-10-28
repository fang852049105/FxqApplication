package com.fxq.lib.omnipotentfunction;

/**
 * Author: Fanxq
 * Date: 2019-10-28
 */
public abstract class FunctionNoParamHasResult<T> extends OmnipotentFunction{

    public FunctionNoParamHasResult(String functionName) {
        super(functionName);
    }

    public abstract T function();
}
