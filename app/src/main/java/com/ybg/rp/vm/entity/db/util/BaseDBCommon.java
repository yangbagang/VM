package com.ybg.rp.vm.entity.db.util;

import com.cnpay.tigerbalm.utils.SimpleUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.util.Config;

import java.io.File;
import java.util.ArrayList;

import xutils.db.DbManager;
import xutils.db.Selector;
import xutils.ex.DbException;

/**
 * 数据库工具
 * <p/>
 * 包            名:      com.cnpay.vending.yifeng.util
 * 类            名:      BaseDBCommon
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/14
 */
public class BaseDBCommon {
    private DbManager manager;

    public BaseDBCommon() {
        DbManager.DaoConfig config = new DbManager.DaoConfig();
        config.setDbName(Config.DB_NAME);
        config.setDbVersion(Config.DB_VERSION);
        String dbPath = SimpleUtil.getSDCardPath() + "/cnpay_vm";
        config.setDbDir(new File(dbPath));//需要修改成 数据库外放
        config.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                // TODO: ...
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
     * 添加数据
     *
     * @param obj
     */
    public void addObject(Object obj) {
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

    public Object findFirst(Class<?> cls,String c,String o ,String v) {
        try {
            manager.selector(cls).where(c,o,v).findFirst();
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
     * @param selector
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> findAll(Selector selector) {
        try {
            return (ArrayList<T>) selector.findAll();
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
     * 查询全部数据
     * and
     *
     * @param entityType
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> findAll(Class<T> entityType, String columnName, String op,
                                    Object value, String andColumn, String andOp, Object andValue) {
        try {
            return (ArrayList<T>) manager.selector(entityType).where(columnName, op, value)
                    .and(andColumn, andOp, andValue).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 清除所有数据
     *
     * @param entityType
     * @param <T>
     */
    public <T> void clearData(Class<T> entityType) {
        try {
            manager.delete(entityType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
