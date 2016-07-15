package com.ybg.rp.vm.listener;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import com.ybg.rp.vm.activity.shopping.ShoppingActivity;
import com.ybg.rp.vm.adapter.GoodsAdapter;
import com.ybg.rp.vm.entity.GoodsInfo;

import java.util.ArrayList;


/**
 * 包            名:      com.cnpay.vending.yifeng.listener
 * 类            名:      LessShoppingListener
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/4/20
 */
public class LessShoppingListener implements View.OnClickListener {

    private Activity mActivity;
    private ArrayList<GoodsInfo> datas;
    private ArrayList<GoodsInfo> cartDatas;             //购物车数据
    private Handler mHandler;
    private Integer position;
    private GoodsAdapter baseAdapter;

    public LessShoppingListener(Activity mActivity, ArrayList<GoodsInfo> datas, ArrayList<GoodsInfo> cartDatas, Handler mHandler,
                                Integer position, GoodsAdapter baseAdapter) {
        this.mActivity = mActivity;
        this.datas = datas;
        this.cartDatas = cartDatas;
        this.mHandler = mHandler;
        this.position = position;
        this.baseAdapter = baseAdapter;
    }

    @Override
    public void onClick(final View v) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GoodsInfo goodsInfo = datas.get(position);
                int count = goodsInfo.getNum();
                count--;
                goodsInfo.setNum(count);

                //TODO 删除购物车数据
                for (int i = 0; i < cartDatas.size(); i++) {
                    GoodsInfo goodsInfo1 = cartDatas.get(i);
                    //如果gid相同就-1
                    if (goodsInfo1.getgId().equals(goodsInfo.getgId())) {
                        goodsInfo1.setNum(count);
                        if (count < 1) {
                            cartDatas.remove(goodsInfo1);
                        }
                    }
                }
                baseAdapter.notifyDataSetChanged();
                mHandler.sendEmptyMessage(ShoppingActivity.CHANGE_UI);
            }
        });
    }
}
