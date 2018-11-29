package fxq.android.com.commonbusiness;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Fangxq on 2018/4/23.
 */

public interface SwipeFinishTouchCheckAction {
    /**
     * 向右滑动是否可关闭activity
     * @return
     */
    boolean onScrollToClose();

    /**
     * 是否点击横向滑动的view
     * @param rect
     * @param point
     * @return
     */
    boolean inChild(Rect rect, Point point);
}
