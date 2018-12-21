package com.fxq.designmethod.chain;

import com.fxq.designmethod.Constant;
import com.fxq.lib.log.Logger;

/**
 * @author huiguo
 * @date 2018/12/21
 */
public class Manager implements Ratify {
    @Override
    public Result deal(Chain chain) {
        Request request = chain.request();
        Logger.d(Constant.LOG_TAG_CHAIN, "Manager=====>request:" + request.toString());
        if (request.days() > 3) {
            // 构建新的Request
            Request newRequest = new Request.Builder().newRequest(request)
                    .setManagerInfo(request.name() + "每月的KPI考核还不错，可以批准")
                    .build();
            return chain.proceed(newRequest);

        }
        return new Result(true, "Manager：早点把事情办完，项目离不开你");
    }

}
