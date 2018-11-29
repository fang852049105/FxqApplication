package com.example.fangxq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fangxq.myapplication.R;

/**
 * @author huiguo
 * @date 2018/11/26
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private Context mContext;
    private int layoutId;
    private int size = 10;
    private OnItemClickLitener mOnItemClickLitener;

    public RecycleViewAdapter(Context context, int layoutId, int size) {
        this.mContext = context;
        this.layoutId = layoutId;
        this.size = size;
    }

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        mOnItemClickLitener = onItemClickLitener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(mContext).inflate(layoutId, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                Log.e("fxq", "x: " + x + "-------" + "y: " + y);
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onItemClick(v,x, y);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mButtonText;

        public ViewHolder(View itemView) {
            super(itemView);
            mButtonText = (TextView) itemView.findViewById(R.id.tv_button);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View v, int x, int y);
    }
}
