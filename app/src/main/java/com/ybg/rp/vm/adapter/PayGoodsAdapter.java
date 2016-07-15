package com.ybg.rp.vm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.entity.GoodsInfo;

import java.util.List;

/**
 * 支付购物车 数据
 *
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.adapter
 * @修改记录:
 * @公司:
 * @date 2016/4/12 0012
 */
public class PayGoodsAdapter extends BaseAdapter {

    private Context mContext;
    private List<GoodsInfo> datas;

    public PayGoodsAdapter(Context context, List<GoodsInfo> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        if (datas.size() > 0) {
            return datas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_pay_goods_info, null);
            holder = new ViewHolder();
            convertView.setTag(holder);

            //找id
            holder.iv_image = (ImageView) convertView.findViewById(R.id.payGoods_iv_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.payGoods_tv_name);
            holder.tv_price = (TextView) convertView.findViewById(R.id.payGoods_tv_price);
            holder.tv_standard = (TextView) convertView.findViewById(R.id.payGoods_tv_standard);
            holder.tv_count = (TextView) convertView.findViewById(R.id.payGoods_tv_count);
            holder.tv_rail = (TextView) convertView.findViewById(R.id.payGoods_tv_rail);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //TODO 设置数据
        GoodsInfo goodsInfo = datas.get(position);
        holder.tv_name.setText(goodsInfo.getGoodsName());
        holder.tv_standard.setText(goodsInfo.getGoodsDesc());
        holder.tv_price.setText("¥ " + goodsInfo.getPrice());
        holder.tv_rail.setText("轨道: "+goodsInfo.getTrackNo());
        holder.tv_count.setText("数量: " + goodsInfo.getNum());

            //隐藏服务器返回的数据,库存和数量为0的情况()
//        if(goodsInfo.getNum()<1) {
//            convertView.setVisibility(View.GONE);
//        } else {
//            convertView.setVisibility(View.VISIBLE);
//        }

        String goodsPic = goodsInfo.getGoodsPic();
        //String goodsPic = "http://res.cloudinary.com/liuyuesha/image/fetch/http://himawari8-dl.nict.go.jp/himawari8/img/D531106/1d/550/2016/01/04/095000_0_0.png";
//        DisplayImageOptions options = ImageLoadUtil.getOptions4PictureList(R.mipmap.icon_default_pic);
//        ImageLoadUtil.displayImage(goodsPic, holder.iv_image, options);

        //ImageUtils.getInstance(mContext).getImage(goodsPic, R.mipmap.icon_default_pic).into(holder.iv_image);
        Glide.with(mContext)
                .load(goodsPic)
                .placeholder(R.mipmap.icon_default_pic)
                .error(R.mipmap.icon_default_pic)
                .into(holder.iv_image);

        return convertView;
    }

    class ViewHolder {
        private ImageView iv_image;
        private TextView tv_name;       //商品名
        private TextView tv_price;      //价格
        private TextView tv_standard;   //规格
        private TextView tv_count;      //数量
        private TextView tv_rail;   //轨道
    }
}
