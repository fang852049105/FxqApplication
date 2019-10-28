package com.fxq.lib.omnipotentfunction;

import java.io.OptionalDataException;

/**
 * Author: Fanxq
 * Date: 2019-10-28
 */
public abstract class FunctionNoParamNoResult extends OmnipotentFunction {

    public FunctionNoParamNoResult(String functionName) {
        super(functionName);
    }

    public abstract void function();
}
