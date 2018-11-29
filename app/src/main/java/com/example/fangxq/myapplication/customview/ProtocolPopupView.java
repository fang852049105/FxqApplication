package com.example.fangxq.myapplication.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.fangxq.myapplication.R;
import com.example.fangxq.myapplication.utils.AnimationUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Fangxq on 2017/10/26.
 */
public class ProtocolPopupView {

    private Context mContext;
    private PopupWindow mPopupWindow = null;
    private WeakReference<Activity> mActivityReference;

    private ProtocolPopupView(Context context, Activity activity) {
        this.mContext = context;
        this.mActivityReference = new WeakReference<>(activity);
        initPopupWindow();
    }

    public synchronized static ProtocolPopupView initProtocolPopupView(Context context, Activity activity) {

        return new ProtocolPopupView(context, activity);
    }

    private void initPopupWindow() {
        if (mPopupWindow == null) {
            View viewPopupWindow = LayoutInflater.from(mContext).inflate(R.layout.layout_rh_protocol_webview_dialog, null, false);
            WebView infoWebView = (WebView) viewPopupWindow.findViewById(R.id.webview_content);
            infoWebView.getSettings().setJavaScriptEnabled(true);
            infoWebView.getSettings().setDatabaseEnabled(true);
            infoWebView.getSettings().setDomStorageEnabled(true);
            infoWebView.loadUrl("http://test10.mm.airent.test.aiershou.com/product/insurance/?id=3&id_activity=44&id_sku=419954&choose_installments_num=12&third=1");
            mPopupWindow = new PopupWindow(viewPopupWindow, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setAnimationStyle(R.style.PushInOutStyle);
            //mPopupWindow.getContentView().startAnimation(AnimationUtil.createInAnimation(mContext, fromYDelta));
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //backgroundAlpha(1f);
                    mActivityReference.clear();
                    mActivityReference = null;
                    mContext = null;
                    mPopupWindow = null;
                }
            });
            mPopupWindow.update();
        }
    }

    public void show(View view) {
        //mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        mPopupWindow.showAsDropDown(view, 0, 0);
        //backgroundAlpha(0.5f);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivityReference.get().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mActivityReference.get().getWindow().setAttributes(lp);
    }


}
