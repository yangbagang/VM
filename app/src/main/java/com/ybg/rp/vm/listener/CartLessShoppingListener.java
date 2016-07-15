package com.ybg.rp.vm.listener;

import android.os.Handler;
import android.view.View;

import com.ybg.rp.vm.activity.shopping.ShoppingActivity;
import com.ybg.rp.vm.adapter.CartAdapter;
import com.ybg.rp.vm.adapter.GoodsAdapter;
import com.ybg.rp.vm.entity.GoodsInfo;

import java.util.ArrayList;


/**
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.listener
 * @修改记录:
 * @公司:
 * @date 2016/4/25 0025
 */
public class CartLessShoppingListener implements View.OnClickListener {

    private ArrayList<GoodsInfo> cartDatas;             //购物车数据
    private Handler mHandler;
    private Integer position;
    private CartAdapter baseAdapter;
    private GoodsAdapter mGoodsAdapter;

    public CartLessShoppingListener(ArrayList<GoodsInfo> cartDatas, Handler mHandler,
                                    Integer position, CartAdapter baseAdapter, GoodsAdapter goodsAdapter) {
        this.cartDatas = cartDatas;
        this.mHandler = mHandler;
        this.position = position;
        this.baseAdapter = baseAdapter;
        this.mGoodsAdapter = goodsAdapter;
    }

    @Override
    public void onClick(View v) {
        GoodsInfo goodsInfo = cartDatas.get(position);
        int count = goodsInfo.getNum();
        count--;
        if (count > 0) {
            goodsInfo.setNum(count);
        } else {
            goodsInfo.setNum(0);
            cartDatas.remove(goodsInfo);
        }

        baseAdapter.notifyDataSetChanged();
        mGoodsAdapter.notifyDataSetChanged();
        mHandler.sendEmptyMessage(ShoppingActivity.CHANGE_UI);
    }
}
