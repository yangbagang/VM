package com.ybg.rp.vm.listener;

import java.util.ArrayList;

/**
 * 包            名:      com.ybg.rp.vm.listener
 * 类            名:      ResultCallback
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/22 0022
 */
public class ResultCallback {

    public interface ReturnListener {
        /**
         * 启动
         */
        public void startRecord();

        /**
         * 返回结果
         */
        public void returnRecord(ArrayList<String> list);
    }

    public interface ResultListener {
        /**
         * 启动
         */
        public void startFunction();

        /**
         * 返回结果
         */
        public void isResultOK(Boolean ok);
    }

    /**
     * 用于最大排放量的设置
     */
    public interface MaxListener {
        public void selectMax(int max);
    }

}
