package com.fxq.lib.omnipotentfunction;

/**
 * Author: Fanxq
 * Date: 2019-10-28
 */
public abstract class FunctionHasParamHasResult<T,P> extends OmnipotentFunction{

    public FunctionHasParamHasResult(String functionName) {
        super(functionName);
    }

    public abstract T function(P p);
}

