package com.fxq.lib.omnipotentfunction;

/**
 * Author: Fanxq
 * Date: 2019-10-28
 */
public abstract class FunctionHasParamNoResult<P> extends OmnipotentFunction{

    public FunctionHasParamNoResult(String functionName) {
        super(functionName);
    }

    public abstract void function(P p);
}

