package com.ybg.rp.vm.util.helper;

import android.text.TextUtils;

import com.cnpay.tigerbalm.utils.DateUtil;
import com.cnpay.tigerbalm.utils.SimpleUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.entity.db.LogInfo;
import com.ybg.rp.vm.entity.db.Operator;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.entity.db.TranOnlineData;
import com.ybg.rp.vm.util.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xutils.common.KeyValue;
import xutils.db.DbManager;
import xutils.db.sqlite.SqlInfo;
import xutils.db.table.DbModel;
import xutils.ex.DbException;

/**
 * 数据库工具
 * <p>
 * 包            名:      com.ybg.rp.vm.util.helper
 * 类            名:      EntityDBUtil
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/21 0021
 */
public class EntityDBUtil {
    private DbManager manager;
    private static EntityDBUtil dbUtil;

    public static EntityDBUtil getInstance() {
        if (null == dbUtil) {
            dbUtil = new EntityDBUtil();
        }
        return dbUtil;
    }

    private EntityDBUtil() {
        DbManager.DaoConfig config = new DbManager.DaoConfig();
        config.setDbName(Config.DB_NAME);
        config.setDbVersion(Config.DB_VERSION);
        String dbPath = SimpleUtil.getSDCardPath() + "/cnpay_vm";
        config.setDbDir(new File(dbPath));//需要修改成 数据库外放
        config.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager dbManager, int i, int i1) {
                // db.addColumn(...);
                // db.dropTable(...);
                // ...
            }
        });
        manager = xutils.x.getDb(config);
    }

    public DbManager getDb() {
        return manager;
    }


    /**
     * 添加log日志
     *
     * @param content 内容
     */
    public void saveLog(String content) {
        try {
            LogInfo logInfo = new LogInfo();
            logInfo.setContent(content);
            Operator operator = OPApplication.getInstance().getOper();
            if (null != operator) {
                logInfo.setOperName(operator.getOperName());
                logInfo.setOperId(operator.getOperId());
            }
            logInfo.setCreateDate(DateUtil.getCurrentDate(DateUtil.dateFormatYMDHMS));
            dbUtil.addObject(logInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 记录错误轨道信息
     *
     * @param trackNo
     */
    public void saveFaultTrackNo(String trackNo) {
        try {
            TrackBean tv = manager.selector(TrackBean.class).where("TRACK_NO", "=", trackNo).findFirst();
            if (null == tv) {
                return;
            }
            tv.setFault(1);
            dbUtil.saveOrUpdate(tv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加数据
     *
     * @param obj
     */
    public void addObject(Object obj) {
        //TbLog.i("[-add:"+obj.toString()+"-]");
        try {
            manager.save(obj);
        } catch (DbException e) {
            TbLog.e("添加数据错误---" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 添加数据OR 修改
     *
     * @param obj
     */
    public void saveOrUpdate(Object obj) {
        //TbLog.i("[-update:"+obj.toString()+"-]");
        try {
            manager.saveOrUpdate(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询最后一个
     *
     * @param cls
     * @return
     */
    public Object findFirst(Class<?> cls) {
        try {
            return manager.findFirst(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T findFirst(Class<T> entityType, String columnName, String op, Object value) {
        try {
            return manager.selector(entityType).where(columnName, op, value).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 查询全部数据
     *
     * @param entityType
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> findAll(Class<T> entityType) {
        try {
            return (ArrayList<T>) manager.findAll(entityType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询全部数据
     *
     * @param entityType
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> findAll(Class<T> entityType, String columnName, String op, Object value) {
        try {
            return (ArrayList<T>) manager.selector(entityType).where(columnName, op, value).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询-销售数据-接口
     *
     * @param startDate 开始时间- yyyy-MM-dd HH:mm:ss
     * @param endDate   结束时间- yyyy-MM-dd HH:mm:ss
     * @param limit     显示条数
     * @param pageSize  页码 1开始
     * @return
     */
    public ArrayList<TranOnlineData> findTranData(String startDate, String endDate, int limit, int pageSize) {
        TbLog.i("[-- startDate =" + startDate + "--- endDate =" + endDate + "--- limit = " + limit + "--- pageSize = " + pageSize);
        ArrayList<TranOnlineData> tranOnlineDatas = new ArrayList<TranOnlineData>();
        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            TbLog.e("时间字段不能为NULL");
            return tranOnlineDatas;
        }

        try {
            String sql = "select * from TRAN_ONLINE t where t.[SALE_RESULT] != :status  " +
//                        "  and t.TRAN_DATE like :tranDate " +
                    " and date(t.[TRAN_DATE]) >= date(:startDate) and date(t.[TRAN_DATE]) <= date(:endDate)" +
                    " order by t.[CREATE_DATE] desc Limit :limit offset :pageSize";
            SqlInfo sqlInfo = new SqlInfo();
            /** 两个时间之间的数据*/
//            sqlInfo.addBindArg(new KeyValue("tranDate", String.valueOf(DateUtil.formatDateStr2Desc(startDate, DateUtil.dateFormatYMD)) + "%"));
            sqlInfo.addBindArg(new KeyValue("status", 0));// 0 失败 1 成功
            sqlInfo.addBindArg(new KeyValue("startDate", startDate));
            sqlInfo.addBindArg(new KeyValue("endDate", endDate));
            sqlInfo.addBindArg(new KeyValue("limit", pageSize)); // 条数
            sqlInfo.addBindArg(new KeyValue("pageSize",(pageSize * limit - 1)));//页面-0开始
            sqlInfo.setSql(sql);
            List<DbModel> dbModels = dbUtil.getDb().findDbModelAll(sqlInfo);
//            TbLog.i("[findTran :"+dbModels.toString()+"]");
            for (int i = 0; i < dbModels.size(); i++) {
                DbModel dm = dbModels.get(i);
                TranOnlineData data = new TranOnlineData();
                data.setCreateDate(dm.getString("CREATE_DATE"));//CREATE_DATE
                data.setOrderNo(dm.getString("ORDER_NO"));
                data.setTranDate(DateUtil.getDateByFormat(dm.getString("TRAN_DATE"), DateUtil.dateFormatYMDHMS));
                data.setPayType(dm.getString("PAY_TYPE"));
                data.setSaleResult(dm.getString("SALE_RESULT"));
                data.setOrderPrice(dm.getDouble("ORDER_PRICE"));
                data.setTrackNos(dm.getString("TRACK_NOS"));
                tranOnlineDatas.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tranOnlineDatas;
    }
}
