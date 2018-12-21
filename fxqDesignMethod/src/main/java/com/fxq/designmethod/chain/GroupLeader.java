package com.fxq.designmethod.chain;


import com.fxq.designmethod.Constant;
import com.fxq.lib.log.Logger;

/**
 * @author huiguo
 * @date 2018/12/21
 */
public class GroupLeader implements Ratify {

    @Override
    public Result deal(Chain chain) {
        Request request = chain.request();
        Logger.d(Constant.LOG_TAG_CHAIN, "GroupLeader=====>request:" + request.toString());

        if (request.days() > 1) {
            // 包装新的Request对象
            Request newRequest = new Request.Builder().newRequest(request)
                    .setGroupLeaderInfo(request.name() + "平时表现不错，而且现在项目也不忙")
                    .build();
            return chain.proceed(newRequest);
        }

        return new Result(true, "GroupLeader：早去早回");
    }
}
