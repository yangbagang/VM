package com.ybg.rp.vm.listener;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.ybg.rp.vm.activity.shopping.ShoppingActivity;
import com.ybg.rp.vm.adapter.GoodsAdapter;
import com.ybg.rp.vm.entity.GoodsInfo;

import java.util.ArrayList;

/**
 * 购物车添加
 * 包            名:      com.cnpay.vending.yifeng.listener
 * 类            名:      AddShoppingListener
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/4/20
 */
public class AddShoppingListener implements View.OnClickListener {

    private ImageView image;
    private Activity mActivity;
    private ArrayList<GoodsInfo> datas;
    private ArrayList<GoodsInfo> cartDatas;             //购物车数据
    private Handler mHandler;
    private Integer position;
    private GoodsAdapter baseAdapter;

    public AddShoppingListener(ImageView image,Activity mActivity, ArrayList<GoodsInfo> datas, ArrayList<GoodsInfo> cartDatas,
                               Handler mHandler, Integer position, GoodsAdapter baseAdapter) {
        this.image = image;
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
                //改变商品显示数量
                GoodsInfo goodsInfo = datas.get(position);
                int count = goodsInfo.getNum();
                int kuCun = goodsInfo.getKucun();
                TbLog.i("add:count------" + count);
                TbLog.i("add:kuCun------" + kuCun);
                if (count == kuCun) {
//                    ToastUtil.showToast(mActivity, "没有更多库存");
                    ToastUtil.showCenterToast(mActivity, "没有更多库存!");
                    return;
                }

                /**设置添加商品动画*/
                int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                ImageView ball = new ImageView(mActivity);// buyImg是动画的图片，
                ball.setImageDrawable(image.getDrawable());// 设置buyImg的图片
                ball.setTag(startLocation);
                Message msg = new Message();
                msg.what = ShoppingActivity.SHOPPING_CART;
                msg.obj = ball;
                mHandler.sendMessage(msg);

                count++;
                goodsInfo.setNum(count);


                //TODO 添加数据到购物车
                boolean isok = false;
                for (int i = 0; i < cartDatas.size(); i++) {
                    GoodsInfo goodsInfo1 = cartDatas.get(i);
                    //如果gid相同就+1
                    if (goodsInfo1.getgId().equals(goodsInfo.getgId())) {
                        goodsInfo1.setNum(count);
                        isok = true;
                    }
                }
                //有数据的情况下,gid不相同,还是添加进去
                if (!isok) {
                    goodsInfo.setNum(1);
                    cartDatas.add(goodsInfo);
                }
                baseAdapter.notifyDataSetChanged();
                mHandler.sendEmptyMessage(ShoppingActivity.CHANGE_UI);
            }
        });
    }
}
