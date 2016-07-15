package com.ybg.rp.vm.adapter;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.view.list.listener.LoadFinishCallBack;
import com.cnpay.tigerbalm.view.list.listener.LoadResultCallBack;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.entity.db.LogInfo;
import com.ybg.rp.vm.util.helper.EntityDBUtil;

import java.util.ArrayList;
import java.util.List;

import xutils.common.KeyValue;
import xutils.db.sqlite.SqlInfo;
import xutils.db.table.DbModel;

/**
 * 包            名:      com.ybg.rp.vm.adapter
 * 类            名:      MachinesLogAdapter
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/22 0022
 */
public class MachinesLogAdapter extends RecyclerView.Adapter<MachinesLogAdapter.LogHolder>{
    private int page;
    private int size = 20;
    private ArrayList<LogInfo> infos;
    private EntityDBUtil dbUtil;
    private LoadFinishCallBack mLoadFinisCallBack;
    private LoadResultCallBack mLoadResultCallBack;

    public MachinesLogAdapter(LoadFinishCallBack mLoadFinisCallBack, LoadResultCallBack mLoadResultCallBack) {
        this.mLoadFinisCallBack = mLoadFinisCallBack;
        this.mLoadResultCallBack = mLoadResultCallBack;
        infos = new ArrayList<LogInfo>();
        dbUtil = EntityDBUtil.getInstance();
    }

    @Override
    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_log, parent, false);
        return new LogHolder(v);
    }

    @Override
    public void onBindViewHolder(LogHolder holder, int position) {
        LogInfo log = infos.get(position);
        holder.tv_item_content.setText(StrUtil.parseEmpty(log.getContent()));
        holder.tv_item_date.setText(log.getCreateDate());
        holder.tv_item_opername.setText(log.getOperName());
    }

    @Override
    public int getItemCount() {
        if (infos.size()>0){
            return infos.size();
        }
        return 0;
    }

    public class LogHolder extends RecyclerView.ViewHolder{
        private TextView tv_item_date;
        private TextView tv_item_content;
        private TextView tv_item_opername;

        public LogHolder(View itemView) {
            super(itemView);

            tv_item_date = (TextView) itemView.findViewById(R.id.tv_item_date);
            tv_item_content = (TextView) itemView.findViewById(R.id.tv_item_content);
            tv_item_opername = (TextView) itemView.findViewById(R.id.tv_item_opername);
        }
    }

    public void loadFirst() {
        infos.clear();
        page = 0;
        loadData();
    }

    public void loadNextPage() {
        page++;
        loadData();
    }

    private void loadData() {
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                TbLog.i("----执行前-");
                mLoadFinisCallBack.loadFinish();
                TbLog.i("----执行 后-");
                mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                TbLog.i("[-logList="+infos.size()+"-]");
                notifyDataSetChanged();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                /**
                 * Select * From TABLE_NAME Limit 9 Offset 10;
                 * 表示从TABLE_NAME表获取数据，跳过10行，取9行
                 * select * from LOG_INFO t order by t.CREATE_DATE desc Limit :10,:9
                 * 表示从LOG_INFO表获取数据，跳过10行，取9行
                 */
                try {
                    String sql = "select * from LOG_INFO t order by t.CREATE_DATE desc Limit :limit offset :pageSize";
                    SqlInfo sqlInfo = new SqlInfo();
                    sqlInfo.setSql(sql);
                    sqlInfo.addBindArg(new KeyValue("limit", size)); // 条数
                    sqlInfo.addBindArg(new KeyValue("pageSize", (page * size - 1)));//页面-0开始

                    TbLog.i("-----limit = " + size + "----pageSize = " + page);
                    if (page == 0){
                        infos.clear();
                    }
                    List<DbModel> dbModels = dbUtil.getDb().findDbModelAll(sqlInfo);
                    for (int i = 0; i < dbModels.size(); i++) {
                        DbModel db = dbModels.get(i);
                        LogInfo log = new LogInfo();
                        log.setId(db.getLong("id"));
                        log.setCreateDate(db.getString("CREATE_DATE"));
                        log.setOperId(db.getLong("OPER_ID"));
                        log.setOperName(db.getString("OPER_NAME"));
                        log.setContent(db.getString("CONTENT"));
                        log.setType(db.getInt("TYPE"));
                        infos.add(log);
                    }
                    if (dbModels.size() < 1) {
                        page--;
                        TbLog.i("没有更多-日志数据");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            }
        }.execute();
    }
}
