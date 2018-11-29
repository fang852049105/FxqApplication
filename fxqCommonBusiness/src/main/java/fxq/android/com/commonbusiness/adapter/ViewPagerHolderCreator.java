package fxq.android.com.commonbusiness.adapter;

/**
 * Created by Fangxq on 2017/5/31.
 */
public interface ViewPagerHolderCreator<VH extends ViewPagerHolder> {
    /**
     * 创建ViewHolder
     * @return
     */
    public VH createViewHolder();
}
