package com.fxq.designmethod.chain;

import com.fxq.designmethod.Constant;
import com.fxq.lib.log.Logger;

/**
 * @author huiguo
 * @date 2018/12/21
 */
public class DepartmentLeader implements Ratify {

    @Override
    public Result deal(Chain chain) {
        Request request = chain.request();
        Logger.d(Constant.LOG_TAG, "DepartmentHeader=====>request:" + request.toString());
        if (request.days() > 7) {
            return new Result(false, "你这个完全没必要");
        }
        return new Result(true, "DepartmentHeader：不要着急，把事情处理完再回来！");
    }
}
