package com.ybg.rp.vm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.entity.BigGoodsInfo;

import java.util.List;


/**
 * 大类商品数据
 *
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.adapter
 * @修改记录:
 * @公司:
 * @date 2016/4/14 0014
 */
public class BigGoodsAdapter extends RecyclerView.Adapter<BigGoodsAdapter.MyHolder> {

    private Context mContext;
    private List<BigGoodsInfo> datas;
    private ItemClickListener mItemClickListener;
    private int selectIndex = -1;

    public BigGoodsAdapter(Context context, List<BigGoodsInfo> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemvView = View.inflate(mContext, R.layout.item_big_goods_info, null);
        return new MyHolder(itemvView, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        /**
         * 设置数据
         */
        BigGoodsInfo bigGoodsInfo = datas.get(position);
        String name = bigGoodsInfo.getCategory();
        holder.tv_name.setText(name);

        if(name.equals("香烟")) {
            holder.iv_image.setImageResource(R.drawable.radiobutton_smoke);
        } else if(name.equals("零食")) {
            holder.iv_image.setImageResource(R.drawable.radiobutton_linshi);
        } else if(name.equals("饮料")) {
            holder.iv_image.setImageResource(R.drawable.radiobutton_yinliao);
        } else {
            holder.iv_image.setImageResource(R.drawable.radiobutton_shuiguo);
        }

        if (position == selectIndex) {
            holder.view.setSelected(true);
        } else {
            holder.view.setSelected(false);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemCLick(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (datas.size() > 0) {
            return datas.size();
        }
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView iv_image; //商品图片
        private TextView tv_name;      //名称

        private ItemClickListener mItemClickListener;

        public MyHolder(View itemView, ItemClickListener listener) {
            super(itemView);
            this.mItemClickListener = listener;
            //找id
            iv_image = (ImageView) itemView.findViewById(R.id.bigGoods_iv_image);
            tv_name = (TextView) itemView.findViewById(R.id.bigGoods_tv_name);
            view = itemView;
        }
    }

    /**
     * 设置item点击事件
     */
    public void setOnItemClickListener(ItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemCLick(View view, int position);
    }

    /**
     * 设置选中item 改变背景
     * @param i
     */
    public void setSelectIndex(int i) {
        selectIndex = i;
    }
}
