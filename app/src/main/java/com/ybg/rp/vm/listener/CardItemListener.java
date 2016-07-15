package com.ybg.rp.vm.listener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cnpay.tigerbalm.utils.GsonUtils;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.ybg.rp.vm.activity.shopping.PayShoppingSingleActivity;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.entity.GoodsInfo;
import com.ybg.rp.vm.entity.OrderInfo;
import com.ybg.rp.vm.net.NetWorkUtil;
import com.ybg.rp.vm.net.WHAT;
import com.ybg.rp.vm.net.YFHttpListener;
import com.ybg.rp.vm.util.AppPreferences;
import com.ybg.rp.vm.util.dialog.YFDialogUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 点击Shopping的item购买单个商品,默认为 微信支付
 *
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.listener
 * @修改记录:
 * @公司:
 * @date 2016/4/28 0028
 */
public class CardItemListener implements View.OnClickListener {

    private Activity mActivity;
    private GoodsInfo mGoodsInfo;
    ArrayList<GoodsInfo> mCartDatas;
    private int mPositon;

    private NetWorkUtil util;
    private int mNum;

    public CardItemListener(Activity activity, GoodsInfo goodsInfo, ArrayList<GoodsInfo> cartDatas, int position) {
        this.mActivity = activity;
        this.mGoodsInfo = goodsInfo;
        this.mPositon = position;
        this.mCartDatas = cartDatas;
        util = NetWorkUtil.getInstance();
    }

    /**
     * 点击生成订单,并去支付单个商品的页面
     */
    @Override
    public void onClick(View v) {

        ArrayList<GoodsInfo> list = new ArrayList<>();
        mNum = mGoodsInfo.getNum();     //获取该商品加入购物车的数量,
        mGoodsInfo.setNum(1);       //将该商品信息 设置数量为1,去生成订单
        list.add(mGoodsInfo);

        String jsonStr = GsonUtils.toJsonPropertiesDes(list, "gid", "num");
        TbLog.i("---Shopping/:" + jsonStr);
        Request<String> makeOrder = util.post("makeOrder");
        makeOrder.add("goodsInfo", jsonStr);
        makeOrder.add("vid", AppPreferences.getInstance().getVMId());
        //            支付方式：默认 0：com.ybg.rp.vm，1：支付宝，2：微信支付
        YFDialogUtil.showLoadding(mActivity);
        util.add(mActivity, WHAT.VM_ORDER, makeOrder, new YFHttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                mGoodsInfo.setNum(mNum);    //不管有无库存,支付成功失败,都将num设置回为原来的num

                YFDialogUtil.removeDialog(mActivity);
                String result = response.get();
                TbLog.i("---------AllCagetory/ShoppingActivity: " + result);
                try {
                    JSONObject json = new JSONObject(result);
                    OrderInfo orderInfo = GsonUtils.createGson().fromJson(json.getString("orderInfo"), OrderInfo.class);

                    OPApplication.getInstance().setIsOpenTrack(orderInfo.getOrderNo());//设置订单号

                    TbLog.d("----orderInfo----: " + orderInfo.toString());
                    Intent intent = new Intent(mActivity, PayShoppingSingleActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("type", ShoppingSelectListener.WX);
                    bundle.putSerializable("orderInfo", orderInfo);
                    intent.putExtras(bundle);
                    mActivity.startActivity(intent);

                    //生成订单成功就清空购物车
                    mCartDatas.clear();
                    //ExitShoppingActivity.getInstance().exit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, String msg) {
                mGoodsInfo.setNum(mNum);  //不管有无库存,支付成功失败,都将num设置回为原来的num

                YFDialogUtil.removeDialog(mActivity);
//                ToastUtil.showToast(mActivity, msg);
                ToastUtil.showCenterToast(mActivity, msg);
            }
        });
    }


}
