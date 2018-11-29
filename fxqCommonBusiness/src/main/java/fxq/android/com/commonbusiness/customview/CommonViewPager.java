package fxq.android.com.commonbusiness.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import fxq.android.com.mylibrary.R;
import fxq.android.com.commonbusiness.adapter.CommonViewPagerAdapter;
import fxq.android.com.commonbusiness.adapter.ViewPagerHolderCreator;

public class CommonViewPager<T> extends RelativeLayout {
    private AutoScrollViewPager mViewPager;
    private CommonViewPagerAdapter mAdapter;
    private CirclePageIndicator mCircleIndicatorView;
    public CommonViewPager(@NonNull Context context) {
        super(context);
        init();
    }

    public CommonViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonViewPager(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommonViewPager(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.common_view_pager_layout,this,true);
        mViewPager = (AutoScrollViewPager) view.findViewById(R.id.common_view_pager);
        mCircleIndicatorView = (CirclePageIndicator) view.findViewById(R.id.common_view_pager_indicator_view);
    }

    /**
     * 设置数据
     * @param data
     * @param creator
     */
    public void setPages(List<T> data, ViewPagerHolderCreator creator){
        mAdapter = new CommonViewPagerAdapter(data,creator);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(data.size() * 100);
        if (data.size() > 1) {
            mCircleIndicatorView.setVisibility(VISIBLE);
            mCircleIndicatorView.setCount(data.size());
            mCircleIndicatorView.setViewPager(mViewPager);
        } else {
            mCircleIndicatorView.setVisibility(GONE);
        }
        mViewPager.startAutoScroll();
        mAdapter.notifyDataSetChanged();
    }

    public void setCurrentItem(int currentItem){
        mViewPager.setCurrentItem(currentItem);
    }

    public int getCurrentItem(){
        return mViewPager.getCurrentItem();
    }

    public void setOffscreenPageLimit(int limit){
        mViewPager.setOffscreenPageLimit(limit);
    }

    /**
     * 设置切换动画，see {@link ViewPager#setPageTransformer(boolean, ViewPager.PageTransformer)}
     * @param reverseDrawingOrder
     * @param transformer
     */
    public void setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer){
        mViewPager.setPageTransformer(reverseDrawingOrder,transformer);
    }

    /**
     * see {@link ViewPager#setPageTransformer(boolean, ViewPager.PageTransformer)}
     * @param reverseDrawingOrder
     * @param transformer
     * @param pageLayerType
     */
    public void setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer,
                                   int pageLayerType) {
        mViewPager.setPageTransformer(reverseDrawingOrder, transformer);
    }

    /**
     * see {@link ViewPager#addOnPageChangeListener(ViewPager.OnPageChangeListener)}
     * @param listener
     */
    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener){
        mViewPager.addOnPageChangeListener(listener);
    }

    /**
     * 设置是否显示Indicator
     * @param visible
     */
    private void setIndicatorVisible(boolean visible){
        if(visible){
            mCircleIndicatorView.setVisibility(VISIBLE);
        }else{
            mCircleIndicatorView.setVisibility(GONE);
        }

    }

    public ViewPager getViewPager() {
        return mViewPager;
    }
}