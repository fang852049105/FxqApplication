package fxq.android.com.commonbusiness;

/**
 * Created by Fangxq on 2018/4/23.
 */

public interface SwipeFinishAction extends SwipeFinishTouchCheckAction {

    void startScroll(int startX, int dx, boolean finish);

    void onTouchDown();

    void onScroll();

}
