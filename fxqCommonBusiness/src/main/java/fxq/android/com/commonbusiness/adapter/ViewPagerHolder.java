package fxq.android.com.commonbusiness.adapter;

import android.content.Context;
import android.view.View;

/**
 * Created by Fangxq on 2017/5/31.
 */
public interface ViewPagerHolder<T> {
    /**
     *  创建View,提供给Adapter布局
     * @param context
     * @return
     */
    View createView(Context context);

    /**
     * 绑定数据
     * @param context
     * @param position
     * @param data
     */
    void onBind(Context context, int position, T data);
}
