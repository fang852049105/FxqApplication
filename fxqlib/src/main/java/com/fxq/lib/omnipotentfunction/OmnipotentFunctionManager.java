package com.fxq.lib.omnipotentfunction;

import android.text.TextUtils;

import com.fxq.lib.log.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Fanxq
 * Date: 2019-10-28
 */
public class OmnipotentFunctionManager {

    public static final String TAG = "OmnipotentFunctionManager";
    private static OmnipotentFunctionManager instance;
    private Map<String, FunctionNoParamNoResult> mFunctionNoParamNoResultMap;
    private Map<String, FunctionNoParamHasResult> mFunctionNoParamHasResultMap;
    private Map<String, FunctionHasParamNoResult> mFunctionHasParamNoResultMap;
    private Map<String, FunctionHasParamHasResult> mFunctionHasParamHasResultMap;

    public OmnipotentFunctionManager() {
        mFunctionNoParamNoResultMap = new HashMap<>();
        mFunctionNoParamHasResultMap = new HashMap<>();
        mFunctionHasParamNoResultMap = new HashMap<>();
        mFunctionHasParamHasResultMap = new HashMap<>();
    }

    public static OmnipotentFunctionManager getInstance() {
        if (instance == null) {
            instance = new OmnipotentFunctionManager();
        }
        return instance;
    }

    /**
     * 添加无参数无返回值方法
     *
     * @param function
     */
    public void addFunction(FunctionNoParamNoResult function) {
        if (function != null) {
            this.mFunctionNoParamNoResultMap.put(function.functionName, function);
        } else {
            Logger.d(TAG, "FunctionNoParamNoResult is Null");
        }
    }

    /**
     * 调用无参数无返回值方法
     *
     * @param functionName
     */
    public void invokeFunction(String functionName) {
        if (TextUtils.isEmpty(functionName)) {
            return;
        }

        if (mFunctionNoParamNoResultMap != null) {
            FunctionNoParamNoResult function = mFunctionNoParamNoResultMap.get(functionName);
            if (function != null) {
                function.function();
            } else {
                Logger.d(TAG, "FunctionNoParamNoResult not created");
            }
        }
    }

    /**
     * 添加无参数有返回值方法
     *
     * @param function
     */
    public void addFunction(FunctionNoParamHasResult function) {
        if (function != null) {
            this.mFunctionNoParamHasResultMap.put(function.functionName, function);
        } else {
            Logger.d(TAG, "FunctionNoParamNoResult is Null");
        }
    }

    /**
     * 调用无参数有返回值方法
     *
     * @param functionName
     * @param t            返回泛型
     * @param <T>          返回泛型
     * @return
     */
    public <T> T invokeFunction(String functionName, Class<T> t) {
        if (!TextUtils.isEmpty(functionName)) {
            if (mFunctionNoParamHasResultMap != null) {
                FunctionNoParamHasResult function = mFunctionNoParamHasResultMap.get(functionName);
                if (function != null) {
                    if (t != null) {
                        return t.cast(function.function());
                    } else {
                        Logger.d(TAG, "Class T is null");
                    }
                } else {
                    Logger.d(TAG, "FunctionNoParamHasResult not created");
                }
            }
        }
        return null;
    }


    /**
     * 添加有参数无返回值方法
     *
     * @param function
     */
    public void addFunction(FunctionHasParamNoResult function) {
        if (function != null) {
            this.mFunctionHasParamNoResultMap.put(function.functionName, function);
        } else {
            Logger.d(TAG, "FunctionNoParamNoResult is Null");
        }
    }

    /**
     * 调用有参数无返回值方法
     *
     * @param functionName
     * @param param
     * @param <P>
     */
    public <P> void invokeFunction(String functionName, P param) {
        if (TextUtils.isEmpty(functionName)) {
            return;
        }
        if (mFunctionHasParamNoResultMap != null) {
            FunctionHasParamNoResult function = mFunctionHasParamNoResultMap.get(functionName);
            if (function != null) {
                function.function(param);
            } else {
                Logger.d(TAG, "FunctionNoParamHasResult not created");
            }
        }
    }


    /**
     * 添加有参数有返回值方法
     *
     * @param function
     */
    public void addFunction(FunctionHasParamHasResult function) {
        if (function != null) {
            this.mFunctionHasParamHasResultMap.put(function.functionName, function);
        } else {
            Logger.d(TAG, "FunctionNoParamNoResult is Null");
        }
    }

    /**
     * 调用有参数有返回值方法
     *
     * @param functionName
     * @param param
     * @param t
     * @param <T>
     * @param <P>
     * @return
     */
    public <T, P> T invokeFunction(String functionName, P param, Class<T> t) {
        if (!TextUtils.isEmpty(functionName)) {
            if (mFunctionHasParamHasResultMap != null) {
                FunctionHasParamHasResult function = mFunctionHasParamHasResultMap.get(functionName);
                if (function != null) {
                    if (t != null) {
                        return t.cast(function.function(param));
                    } else {
                        Logger.d(TAG, "Class T is null");
                    }
                } else {
                    Logger.d(TAG, "FunctionNoParamHasResult not created");
                }
            }
        }
        return null;
    }


    public void removeFunctionNoParamNoResult(String functionName) {
        mFunctionNoParamNoResultMap.remove(functionName);
    }

    public void removeFunctionNoParamHasResult(String functionName) {
        mFunctionNoParamHasResultMap.remove(functionName);
    }

    public void removeFunctionHasParamNoResult(String functionName) {
        mFunctionHasParamNoResultMap.remove(functionName);
    }

    public void removeFunctionHasParamHasResult(String functionName) {
        mFunctionHasParamHasResultMap.remove(functionName);
    }

    public void removeAll(String functionName) {
        mFunctionNoParamNoResultMap.remove(functionName);
        mFunctionNoParamHasResultMap.remove(functionName);
        mFunctionHasParamNoResultMap.remove(functionName);
        mFunctionHasParamHasResultMap.remove(functionName);
    }

    public void removeAll() {
        mFunctionNoParamNoResultMap.clear();
        mFunctionNoParamHasResultMap.clear();
        mFunctionHasParamNoResultMap.clear();
        mFunctionHasParamHasResultMap.clear();
    }

}
