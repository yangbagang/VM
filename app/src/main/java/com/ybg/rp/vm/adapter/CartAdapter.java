package com.ybg.rp.vm.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.entity.GoodsInfo;
import com.ybg.rp.vm.listener.CartAddShoppingListener;
import com.ybg.rp.vm.listener.CartLessShoppingListener;

import java.util.ArrayList;


/**
 * 购物车适配器
 *
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.adapter
 * @修改记录:
 * @公司:
 * @date 2016/4/25 0025
 */
public class CartAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<GoodsInfo> cartDatas;             //购物车数据
    private Handler mHandler;
    private GoodsAdapter mGoodsAdapter;

    public CartAdapter(Activity activity, ArrayList<GoodsInfo> cartDatas, Handler handler, GoodsAdapter goodsAdapter) {
        this.mActivity = activity;
        this.cartDatas = cartDatas;
        this.mHandler = handler;
        this.mGoodsAdapter = goodsAdapter;
    }

    @Override
    public int getCount() {
        return cartDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return cartDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PopViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_cart_info, null);
            holder = new PopViewHolder();
            convertView.setTag(holder);
            //找ID
            holder.tv_name = (TextView) convertView.findViewById(R.id.itempop_tv_name);
            holder.tv_price = (TextView) convertView.findViewById(R.id.itempop_tv_price);
            holder.iv_minus = (ImageView) convertView.findViewById(R.id.itempop_iv_minus);
            holder.tv_goods_count = (TextView) convertView.findViewById(R.id.itempop_tv_goods_count);
            holder.iv_add = (ImageView) convertView.findViewById(R.id.itempop_iv_add);
        } else {
            holder = (PopViewHolder) convertView.getTag();
        }

        //设置数据
        GoodsInfo goodsInfo = cartDatas.get(position);
        holder.tv_name.setText(goodsInfo.getGoodsName());
        holder.tv_price.setText("¥ " + goodsInfo.getPrice());
        holder.tv_goods_count.setText(goodsInfo.getNum() + "");

        holder.iv_add.setOnClickListener(new CartAddShoppingListener(mActivity, cartDatas, mHandler, position, this, mGoodsAdapter));
        holder.iv_minus.setOnClickListener(new CartLessShoppingListener(cartDatas, mHandler, position, this, mGoodsAdapter));

        return convertView;
    }

    class PopViewHolder {
        TextView tv_name;
        TextView tv_price;
        ImageView iv_minus;
        TextView tv_goods_count;    //商品数量
        ImageView iv_add;
    }
}
