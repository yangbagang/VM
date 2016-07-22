package com.ybg.rp.vm.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cnpay.tigerbalm.utils.GsonUtils;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.cnpay.tigerbalm.view.list.listener.LoadFinishCallBack;
import com.google.gson.reflect.TypeToken;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.entity.GoodsInfo;
import com.ybg.rp.vm.listener.AddShoppingListener;
import com.ybg.rp.vm.listener.CardItemListener;
import com.ybg.rp.vm.listener.LessShoppingListener;
import com.ybg.rp.vm.net.NetWorkUtil;
import com.ybg.rp.vm.net.WHAT;
import com.ybg.rp.vm.net.YFHttpListener;
import com.ybg.rp.vm.util.AppPreferences;
import com.ybg.rp.vm.util.dialog.YFDialogUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 订购页面数据
 *
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.adapter
 * @修改记录:
 * @公司:
 * @date 2016/4/11 0011
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.Myholder> {

    private ArrayList<GoodsInfo> datas;

    private Activity mActivity;
    private ArrayList<GoodsInfo> cartDatas;             //购物车数据
    private Handler mHandler;

    private LoadFinishCallBack mLoadFinisCallBack;
    private LinearLayout ll_no_data; //显示数据

    private NetWorkUtil util;


    private int start = 1;
    private Long id;

    public GoodsAdapter(Activity mActivity, ArrayList<GoodsInfo> cartDatas, Handler mHandler
            , LoadFinishCallBack mLoadFinisCallBack, LinearLayout ll_no_data) {
        this.mActivity = mActivity;
        this.cartDatas = cartDatas;
        this.mHandler = mHandler;
        this.mLoadFinisCallBack = mLoadFinisCallBack;
        this.ll_no_data = ll_no_data;
        this.datas = new ArrayList<>();

        util = NetWorkUtil.getInstance();
    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = View.inflate(parent.getContext(), R.layout.item_goods_info, null);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goods_info, parent, false);
        return new Myholder(itemView);
    }

    @Override
    public void onBindViewHolder(Myholder holder, final int position) {
        /**设置数据*/
        GoodsInfo goodsInfo = datas.get(position);
        holder.tv_name.setText(goodsInfo.getGoodsName());
        holder.tv_price.setText("¥ " + goodsInfo.getPrice());
        holder.tv_standard.setText(goodsInfo.getGoodsDesc());

        String goodsPic = goodsInfo.getGoodsPic();
        Glide.with(mActivity)
                .load(goodsPic)
                .placeholder(R.mipmap.icon_default_pic)
                .error(R.mipmap.icon_default_pic)
                .into(holder.iv_image);

        //如果购物车没有数据,将商品信息的数量设为0
        if (cartDatas.size() < 1) {
            //清空购物车后
            goodsInfo.setNum(0);
        } else {
            //购物车有数据
            for (GoodsInfo info : cartDatas) {
                String id = info.getgId();
                String id1 = goodsInfo.getgId();
                int num = info.getNum();
                if (id.equals(id1)) {
                    goodsInfo.setNum(num);
                }
            }
        }
        holder.tv_count.setText(goodsInfo.getNum() + "");

        //根据商品数量是否显示 删除按钮
        if (goodsInfo.getNum() > 0) {
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.iv_minus.setVisibility(View.VISIBLE);
        } else {
            holder.tv_count.setVisibility(View.INVISIBLE);
            holder.iv_minus.setVisibility(View.INVISIBLE);
        }
        holder.iv_add.setOnClickListener(new AddShoppingListener(holder.iv_image, mActivity, datas, cartDatas, mHandler, position, this));
        holder.iv_minus.setOnClickListener(new LessShoppingListener(mActivity, datas, cartDatas, mHandler, position, this));
        holder.ll_item.setOnClickListener(new CardItemListener(mActivity,goodsInfo,cartDatas,position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 获取小类数据
     *
     * @param currPage 页码
     * @param id       大类商品ID
     */
    public void getSmallGoodsData(final int currPage, Long id) {
        TbLog.e("请求分页  ---  currPage = " + currPage + "--- ID = " + id);
        //TbLog.i("----bid"+ id+"vid"+YFApplication.getInstance().initParams().getMachineId());
        this.start = currPage;
        this.id = id;
        Request<String> request = util.post("vendLayerTrackGoods/queryGoodsByTypeOne");
        request.add("bid", id);         //大类id
        request.add("vid", AppPreferences.getInstance().getVMId());          //机器id
        request.add("pageNum", currPage);
        request.add("pageSize", 30);
        YFDialogUtil.showLoadding(mActivity);
        util.add(mActivity, WHAT.VM_GOODSINFO_DETAIL, request, new YFHttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                YFDialogUtil.removeDialog(mActivity);
                String result = response.get();
                TbLog.i("---------Vend/ShoppingActivity: " + result);
                try {
                    JSONObject json = new JSONObject(result);
                    Type type = new TypeToken<List<GoodsInfo>>() {
                    }.getType();
                    List<GoodsInfo> list = GsonUtils.createGson().fromJson(json.getString("list"), type);
                    if (list.size() < 1) {
                        ToastUtil.showToast(mActivity, "暂无更多数据");
                        start--;
                    }
                    if (currPage == 1) {
                        datas.clear();
                    }
                    datas.addAll(list);
                    notifyDataSetChanged();
                    mLoadFinisCallBack.loadFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (ll_no_data.getVisibility() == View.VISIBLE)
                        ll_no_data.setVisibility(View.GONE);//隐藏无数据图片
                }
            }

            @Override
            public void onFailed(int what, String msg) {
                mLoadFinisCallBack.loadFinish();
                TbLog.e("获取商品信息-请求失败-" + msg);
                YFDialogUtil.removeDialog(mActivity);
                //ToastUtil.showToast(mActivity, msg);
                if (ll_no_data.getVisibility() == View.GONE)
                    ll_no_data.setVisibility(View.VISIBLE);//显示无数据图片
            }
        });
    }


    /**
     * 加载更多
     */
    public void loadNextPage() {
        TbLog.e("请求---loadNextPage");
        start++;
        getSmallGoodsData(start, id);
    }


    /**
     * 自定义holder类,继承RecyclerView的ViewHolder
     */
    public class Myholder extends RecyclerView.ViewHolder {

        private CardView ll_item;   //单个item
        private ImageView iv_bg;
        private ImageView iv_image; //商品图片
        private TextView tv_name;   //商品名
        private TextView tv_price;  //价格
        private TextView tv_standard;   //规格
        private TextView tv_count;      //商品数量

        private ImageView iv_add;   //添加
        private ImageView iv_minus;     //删除

        public Myholder(View itemView) {
            super(itemView);
            //找id
            //iv_bg = (ImageView) itemView.findViewById(R.id.goods_iv_bg);
            ll_item = (CardView) itemView.findViewById(R.id.goods_card_item);
            iv_image = (ImageView) itemView.findViewById(R.id.goods_iv_image);
            iv_minus = (ImageView) itemView.findViewById(R.id.goods_iv_minus);
            iv_add = (ImageView) itemView.findViewById(R.id.goods_iv_add);

            tv_name = (TextView) itemView.findViewById(R.id.goods_tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.goods_tv_price);
            tv_standard = (TextView) itemView.findViewById(R.id.goods_tv_standard);
            tv_count = (TextView) itemView.findViewById(R.id.goods_tv_count);
        }
    }

}
