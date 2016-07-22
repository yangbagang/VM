package com.ybg.rp.vm.activity.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.Arith;
import com.cnpay.tigerbalm.utils.GsonUtils;
import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.cnpay.tigerbalm.view.list.AutoLoadRecyclerView;
import com.cnpay.tigerbalm.view.list.listener.LoadMoreListener;
import com.google.gson.reflect.TypeToken;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.adapter.BigGoodsAdapter;
import com.ybg.rp.vm.adapter.CartAdapter;
import com.ybg.rp.vm.adapter.GoodsAdapter;
import com.ybg.rp.vm.anim.GoodsAnim;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.entity.BigGoodsInfo;
import com.ybg.rp.vm.entity.GoodsInfo;
import com.ybg.rp.vm.entity.OrderInfo;
import com.ybg.rp.vm.listener.ShoppingSelectListener;
import com.ybg.rp.vm.net.NetWorkUtil;
import com.ybg.rp.vm.net.WHAT;
import com.ybg.rp.vm.net.YFHttpListener;
import com.ybg.rp.vm.popup.ShopCartPopupWindow;
import com.ybg.rp.vm.util.AppPreferences;
import com.ybg.rp.vm.util.SpaceItemDecoration;
import com.ybg.rp.vm.util.dialog.YFDialogUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车主界面
 *
 * Created by zenghonghua on 2016/6/21 0021.
 */
public class ShoppingActivity extends BaseActivity implements View.OnClickListener {

    private final Logger log = Logger.getLogger(ShoppingActivity.class);

    private LinearLayout ll_back;   //返回上一页
    private TextView tv_title;      //倒计时显示

    private TextView tv_count;      //购物车已选商品数量
    private TextView tv_total_money;    //购物车总金额

    private LinearLayout ll_weixin; // 微信支付
    private LinearLayout ll_zhifubao; // 支付宝支付

    private LinearLayout ll_no_data;    //加载失败
    private Button btn_reload;  //重新加载
    private RelativeLayout rl_cart;     //购物车

    private AutoLoadRecyclerView recyclerViewBig;     //大类数据
    private AutoLoadRecyclerView recyclerViewSmall;   //大类对应商品数据

    private ArrayList<BigGoodsInfo> bigGoodsDatas;       //大类数据
    private ArrayList<GoodsInfo> cartDatas;             //购物车数据

    private NetWorkUtil util;
    private Long id;     //大类商品id

    private BigGoodsAdapter bigGoodsAdapter;        //大类数据
    private GoodsAdapter goodsAdapter;      //小类数据adapter

    private ShopCartPopupWindow mPopupWindow;
    private CartAdapter mCartAdapter;

    /**
     * 购物车动画-点击
     */
    private GoodsAnim goodsAnim;

    private int time = 120;//倒计时

    /**
     * 更新购物车金额和商品显示
     */
    public static final int CHANGE_UI = 1000;
    public static final int SHOPPING_CART = 1001;
    public static final int SHOPPING_CLEAR_CART = 1002;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_UI:
                    changeUi();
                    break;
                case SHOPPING_CART:
                    ImageView ball = (ImageView) msg.obj;
                    goodsAnim.setAnim(ball, tv_count, (int[]) ball.getTag());// 开始执行动画
                    break;
                case SHOPPING_CLEAR_CART:
                    if (null != cartDatas)
                        cartDatas.clear();
                    if (null != goodsAdapter)
                        goodsAdapter.notifyDataSetChanged();
                    if (mCartAdapter != null) {
                        mCartAdapter.notifyDataSetChanged();
                    }
                    changeUi();
                    if (null != mPopupWindow)
                        mPopupWindow.dismiss();
                    break;
                case 158:
                    tv_title.setText(time + "S");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 硬件加速*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_shopping);
        ExitShoppingActivity.getInstance().addActivity(this);
//        initTitle("", false);
        initNoVisibleTitle();

        findView();
        mHandler.postDelayed(runnable, 1000);
        goodsAnim = new GoodsAnim(ShoppingActivity.this);

        util = NetWorkUtil.getInstance();
        bigGoodsDatas = new ArrayList<>();
        cartDatas = new ArrayList<>();
        init();

        //进入页面就获取大类数据
        getBigGoodsData();
    }

    private void findView() {
        tv_title = (TextView) findViewById(R.id.shopping_tv_title);
        ll_back = (LinearLayout) findViewById(R.id.shopping_ll_back);

        tv_count = (TextView) findViewById(R.id.shopping_tv_count);
        tv_total_money = (TextView) findViewById(R.id.shopping_tv_total_money);
        ll_weixin = (LinearLayout) findViewById(R.id.shopping_ll_weinxi);
        ll_zhifubao = (LinearLayout) findViewById(R.id.shopping_ll_zhifubao);
        ll_no_data = (LinearLayout) findViewById(R.id.shopping_ll_nodata);
        btn_reload = (Button) findViewById(R.id.shopping_btn_reload);
        recyclerViewBig = (AutoLoadRecyclerView) findViewById(R.id.shopping_recycler_view_big);
        recyclerViewSmall = (AutoLoadRecyclerView) findViewById(R.id.shopping_recycler_view_small);
        rl_cart = (RelativeLayout) findViewById(R.id.shopping_rl_cart);
    }

    /**
     * 倒计时
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if (time == 0) {
                finish();
            } else {
                mHandler.sendEmptyMessage(158);
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if(time<120) {
            time = 120;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(time<120) {
            time = 120;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        time = 120;
        mHandler.sendEmptyMessage(CHANGE_UI);
        goodsAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化
     */
    private void init() {
        ll_back.setOnClickListener(this);
        /** 支付方式-点击事件*/
        ll_weixin.setOnClickListener(this);
        ll_zhifubao.setOnClickListener(this);

        btn_reload.setOnClickListener(this);
        rl_cart.setOnClickListener(this);

        /** LOAD BIG DATA*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewBig.setLayoutManager(layoutManager);
        recyclerViewBig.setHasFixedSize(true);
        recyclerViewBig.setOnPauseListenerParams(false, false);
        recyclerViewBig.setItemAnimator(new DefaultItemAnimator());
        recyclerViewBig.addItemDecoration(new SpaceItemDecoration(8));

        /**大类商品数据 (RecyclerView) */
        bigGoodsAdapter = new BigGoodsAdapter(this, bigGoodsDatas);
        recyclerViewBig.setAdapter(bigGoodsAdapter);
        //大类点击获取小类商品数据
        bigGoodsAdapter.setOnItemClickListener(new BigGoodsAdapter.ItemClickListener() {
            @Override
            public void onItemCLick(View view, int position) {
                BigGoodsInfo bigGoodsInfo = bigGoodsDatas.get(position);
                id = bigGoodsInfo.getId();
                /**根据大类id获取小类数据*/
                goodsAdapter.getSmallGoodsData(1, id);

                bigGoodsAdapter.setSelectIndex(position);
                bigGoodsAdapter.notifyDataSetChanged();
            }
        });

        /** LOAD GOODS INFO*/
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewSmall.setLayoutManager(gridLayoutManager);
        recyclerViewSmall.setHasFixedSize(true);
        recyclerViewSmall.setOnPauseListenerParams(false, false);

        goodsAdapter = new GoodsAdapter(this, cartDatas, mHandler, recyclerViewSmall, ll_no_data);
        recyclerViewSmall.setAdapter(goodsAdapter);

        recyclerViewSmall.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                goodsAdapter.loadNextPage();
            }
        });

        /** 购物车*/
        mCartAdapter = new CartAdapter(this, cartDatas, mHandler, goodsAdapter);
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopping_ll_back:
                //返回上一页
                finish();
                break;
            case R.id.shopping_ll_weinxi:
                //微信支付
                generateOrder(ShoppingSelectListener.WX);
                break;
            case R.id.shopping_ll_zhifubao:
                //支付宝支付
                generateOrder(ShoppingSelectListener.AL);
                break;
            case R.id.shopping_btn_reload:
                //重新加载
                getBigGoodsData();

                break;

            case R.id.shopping_rl_cart:
                /**购物车弹窗*/
                if (cartDatas.size() > 0)
                    showCartPopwindow();
                break;

        }
    }

    /**
     * 获取大类商品数据
     */
    private void getBigGoodsData() {
        Request<String> request = util.post("goodsTypeOne/listAll");
        util.add(this, WHAT.VM_GOODSINFO_BIG, request, new YFHttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                String result = response.get();
                TbLog.i("---------AllCagetory/ShoppingActivity: " + result);
                try {
                    JSONObject json = new JSONObject(result);
                    Type type = new TypeToken<List<BigGoodsInfo>>() {
                    }.getType();
                    List<BigGoodsInfo> list = GsonUtils.createGson().fromJson(json.getString("list"), type);
                    if (list != null) {
                        bigGoodsDatas.addAll(list);
                        bigGoodsAdapter.notifyDataSetChanged();

                        //默认获取大类相对应的小类商品
                        BigGoodsInfo bigGoodsInfo = bigGoodsDatas.get(0);
                        id = bigGoodsInfo.getId();
                        goodsAdapter.getSmallGoodsData(1, id);
                    }
                    recyclerViewBig.loadFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (ll_no_data.getVisibility() == View.VISIBLE)
                        ll_no_data.setVisibility(View.GONE);//隐藏无数据图片
                }
            }

            @Override
            public void onFailed(int what, String msg) {
                TbLog.e("获取大类商品信息-请求失败-" + msg);
                ToastUtil.showToast(ShoppingActivity.this, msg);
                if (ll_no_data.getVisibility() == View.GONE)
                    ll_no_data.setVisibility(View.VISIBLE);//显示无数据图片
            }
        });
    }

    /**
     * 生成订单,    type :  1-支付宝,  2-微信
     */
    private void generateOrder(final String type) {
        if (cartDatas.size() > 0) {
            String jsonStr = GsonUtils.toJsonPropertiesDes(cartDatas, "gid", "num");
            TbLog.i("---Shopping/:" + jsonStr);
            Request<String> makeOrder = util.post("orderInfo/createOrderWithMchineIdAndGoodsJson");
            makeOrder.add("goodsInfo", jsonStr);
            makeOrder.add("vid", AppPreferences.getInstance().getVMId());
            //支付方式：默认 0：com.ybg.rp.vm，1：支付宝，2：微信支付
            YFDialogUtil.showLoadding(ShoppingActivity.this);
            util.add(ShoppingActivity.this, WHAT.VM_ORDER, makeOrder, new YFHttpListener<String>() {
                @Override
                public void onSuccess(int what, Response<String> response) {
                    YFDialogUtil.removeDialog(ShoppingActivity.this);
                    String result = response.get();
                    TbLog.i("---------makeOrder/ShoppingActivity: " + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        OrderInfo orderInfo = GsonUtils.createGson().fromJson(json.getString("orderInfo"), OrderInfo.class);

                        OPApplication.getInstance().setIsOpenTrack(orderInfo.getOrderNo());//设置订单号

                        TbLog.d("----orderInfo----: " + orderInfo.toString());
                        log.info("--购物车生成的订单--"+orderInfo.toString());

                        Intent intent = new Intent(ShoppingActivity.this, PayShoppingCartActivity.class);
                        Bundle bundle = new Bundle();
                        intent.putExtra("type", type);
                        bundle.putSerializable("orderInfo", orderInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);

                        cartDatas.clear();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(int what, String msg) {
                    YFDialogUtil.removeDialog(ShoppingActivity.this);
                    ToastUtil.showCenterToast(ShoppingActivity.this, msg);
                }
            });
        } else {
            ToastUtil.showCenterToast(this, "购物车木有商品!");
        }
    }

    /**
     * 购物车弹窗
     */
    private void showCartPopwindow() {
        if (null == mPopupWindow) {
            mPopupWindow = new ShopCartPopupWindow(ShoppingActivity.this, mCartAdapter, new ShopCartPopupWindow.ClearAllCartListener() {
                @Override
                public void clearAllCart() {
                    mHandler.sendEmptyMessage(ShoppingActivity.SHOPPING_CLEAR_CART);
                }
            });
        }
        mPopupWindow.showPopupWindow(rl_cart);
    }

    /**
     * 计算总金额,和已选商品数量
     */
    private void changeUi() {
        int tvCount = 0;
        double totalMoney = 0d;
        //计算总金额数量
        if (null != cartDatas && cartDatas.size() > 0) {
            for (int i = 0; i < cartDatas.size(); ++i) {
                GoodsInfo cartInfo = cartDatas.get(i);
                int num = cartInfo.getNum();
                double money = cartInfo.getPrice();

                totalMoney = totalMoney + (Arith.mul(money, num));
                tvCount = tvCount + num;
            }
        }
        if (tvCount == 0) {
            tv_count.setVisibility(View.INVISIBLE);
        } else {
            tv_count.setVisibility(View.VISIBLE);
        }
        tv_total_money.setText(StrUtil.doubleFormatTow(totalMoney));
        tv_count.setText(String.valueOf(tvCount));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler=null;

        cartDatas.clear();
        cartDatas = null;
        recyclerViewBig = null;
        recyclerViewSmall = null;
        if(bigGoodsDatas != null) {
            bigGoodsDatas = null;
        }
    }
}
