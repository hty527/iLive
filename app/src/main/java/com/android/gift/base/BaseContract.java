package com.android.gift.base;

/**
 * hty_Yuye@Outlook.com
 * 2019/5/6
 * MVP Base V
 */

public interface BaseContract {
    /**
     * presenter
     * @param <V> view
     */
    interface BasePresenter<V>{
        void attachView(V view);
        void detachView();
    }

    /**
     * View
     */
    interface BaseView{

        /**
         * 开始加载中
         * @param offset 数据集起始偏移位置
         */
        void showLoading(int offset);

        /**
         * 错误、为空回调
         * @param code 0：为空 -1：失败
         * @param errorMsg 描述信息
         */
        void showError(int code, String errorMsg);
    }
}