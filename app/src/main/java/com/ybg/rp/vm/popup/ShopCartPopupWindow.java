package com.ybg.rp.vm.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.adapter.CartAdapter;


/**
 * 购物车列表
 * 包            名:      com.cnpay.vending.yifeng.popup
 * 类            名:      ShopCartPopupWindow
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/4/27
 */
public class ShopCartPopupWindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private ClearAllCartListener clearAllCartListener;

    private View parent;

    public ShopCartPopupWindow(Context context, CartAdapter mCartAdapter, ClearAllCartListener clearAllCartListener) {
        super(context);
        this.mContext = context;
        this.clearAllCartListener = clearAllCartListener;

        View view = View.inflate(context, R.layout.pop_shopping_cart, null);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(false);

        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
        //mPopupWindow.setAnimationStyle(R.style.Animation_Popup);
        ImageView iv_close = (ImageView) view.findViewById(R.id.pop_iv_close);          //关闭
        LinearLayout ll_delete = (LinearLayout) view.findViewById(R.id.pop_ll_delete);  //清空购物车
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.relative);
        ListView popListview = (ListView) view.findViewById(R.id.pop_listview);

        iv_close.setOnClickListener(this);
        ll_delete.setOnClickListener(this);


        popListview.setAdapter(mCartAdapter);

        //再次点击弹窗消失
        setFocusable(true);
        rl.setFocusableInTouchMode(true);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                parent.setBackgroundResource(R.mipmap.icon_rb_bg_n);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_iv_close:
                dismiss();
                break;
            case R.id.pop_ll_delete:
                if (null != clearAllCartListener) {
                    clearAllCartListener.clearAllCart();
                }
                break;
        }
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        this.parent = parent;
        parent.setBackgroundColor(mContext.getResources().getColor(R.color.pop_cart));
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 269);   //269位高度偏移量
        } else {
            this.dismiss();
        }
    }

    /**
     * popup回调
     */
    public interface ClearAllCartListener {
        /**
         * 清空购物车
         */
        void clearAllCart();
    }
}
