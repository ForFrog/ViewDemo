package com.frog.pulluprecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Frog on 2017/6/21.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<WaitMVBean.DataBean.ComingBean> comingslist;
    private Context context;
    private LayoutInflater inflater;

    public MyRecyclerAdapter(Context context, List<WaitMVBean.DataBean.ComingBean> comingslist) {
        this.comingslist = comingslist;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_recycler, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return comingslist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mv_name;
        private TextView mv_dec;
        private TextView mv_date;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mv_name = itemView.findViewById(R.id.mv_name);
            mv_dec = itemView.findViewById(R.id.mv_dec);
            mv_date = itemView.findViewById(R.id.mv_date);
            imageView = itemView.findViewById(R.id.image);
        }

        public void setData(int position) {
            WaitMVBean.DataBean.ComingBean coming = comingslist.get(position);

            String name = coming.getNm();
            mv_name.setText(name);

            String date = coming.getShowInfo();
            mv_date.setText(date);

            String dec = coming.getScm();
            mv_dec.setText(dec);

            //注：当你发下图片无法打开是，做个字符串替换即可
            String imagUrl = coming.getImg();
            String newImagUrl = imagUrl.replaceAll("w.h", "50.80");

            //使用Glide加载图片
            Glide.with(context)
                    .load(newImagUrl)
                    .into(imageView);
        }
    }
}
