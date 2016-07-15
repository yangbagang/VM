package com.ybg.rp.vm.listener;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import com.cnpay.tigerbalm.utils.ToastUtil;
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
public class CartAddShoppingListener implements View.OnClickListener {

    private Activity mActivity;
    private ArrayList<GoodsInfo> cartDatas;             //购物车数据
    private Handler mHandler;
    private Integer position;
    private CartAdapter baseAdapter;
    private GoodsAdapter mGoodsAdapter;

    public CartAddShoppingListener(Activity mActivity, ArrayList<GoodsInfo> cartDatas, Handler mHandler,
                                   Integer position, CartAdapter baseAdapter, GoodsAdapter goodsAdapter) {
        this.mActivity = mActivity;
        this.cartDatas = cartDatas;
        this.mHandler = mHandler;
        this.position = position;
        this.baseAdapter = baseAdapter;
        this.mGoodsAdapter = goodsAdapter;
    }

    @Override
    public void onClick(View v) {
        //改变商品显示数量
        GoodsInfo goodsInfo = cartDatas.get(position);
        int count = goodsInfo.getNum();
        if (count == goodsInfo.getKucun()) {
//            ToastUtil.showToast(mActivity, "没有更多库存");
            ToastUtil.showCenterToast(mActivity, "没有更多库存!");
            return;
        }

        count++;
        goodsInfo.setNum(count);

        baseAdapter.notifyDataSetChanged();
        mGoodsAdapter.notifyDataSetChanged();
        mHandler.sendEmptyMessage(ShoppingActivity.CHANGE_UI);
    }
}
