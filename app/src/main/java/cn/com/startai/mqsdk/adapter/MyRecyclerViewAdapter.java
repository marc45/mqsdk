package cn.com.startai.mqsdk.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.com.startai.mqsdk.R;
import cn.com.startai.mqttsdk.busi.entity.C_0x8005;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private List<C_0x8005.Resp.ContentBean> list;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * 设置点击事件
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置长按点击事件
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public MyRecyclerViewAdapter(List<C_0x8005.Resp.ContentBean> list) {
        this.list = list;
    }


    public C_0x8005.Resp.ContentBean getItem(int position) {
        return list.get(position);
    }


    public void setList(List<C_0x8005.Resp.ContentBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        C_0x8005.Resp.ContentBean contentBean = this.list.get(position);
        String name = contentBean.getAlias();
        if (TextUtils.isEmpty(name)) {
            name = contentBean.getId();
        }

        holder.mText.setText(name);
        holder.bindTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(contentBean.getBindingtime()));
        holder.sn.setText(contentBean.getId());

        String apptype = contentBean.getApptype();

        int imageRes = 0;
        if (contentBean.getConnstatus() == 1) {
            imageRes = R.drawable.ic_personal_video_black_48dp;

        } else {

            imageRes = R.drawable.ic_personal_video_gray_48dp;

        }


        holder.imageView.setImageResource(imageRes);


        holder.itemView.setOnLongClickListener(new MyOnLongClickListener(position));
        holder.itemView.setOnClickListener(new MyOnClickListener(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mText;
        ImageView imageView;
        TextView bindTime;
        TextView sn;

        ViewHolder(View itemView) {
            super(itemView);
            bindTime = itemView.findViewById(R.id.tv_item_bindtime);
            mText = itemView.findViewById(R.id.tv_item_devicelist);
            imageView = itemView.findViewById(R.id.iv_item_devicelist);
            sn = itemView.findViewById(R.id.tv_item_devicelist_sn);

        }
    }


    private class MyOnLongClickListener implements View.OnLongClickListener {
        private int position;

        public MyOnLongClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onLongClick(View v) {
            onItemLongClickListener.onItemLongClick(v, position);
            return true;
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        private int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, position);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}