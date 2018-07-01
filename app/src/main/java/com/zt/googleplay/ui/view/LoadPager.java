package com.zt.googleplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.zt.googleplay.R;
import com.zt.googleplay.manager.ThreadManager;
import com.zt.googleplay.utils.UIUtils;

/**
 * Author: ZT on 2016/12/31.
 * 数据加载结果5状态
 */
public abstract class LoadPager extends FrameLayout {

    private static final int STATE_LOAD_UNDO = 1;// 未加载
    private static final int STATE_LOAD_LOADING = 2;// 正在加载
    private static final int STATE_LOAD_EMPTY = 3;// 数据为空
    private static final int STATE_LOAD_ERROR = 4;// 加载失败
    private static final int STATE_LOAD_SUCCESS = 5;// 访问成功

    private int mCurrentState = STATE_LOAD_UNDO;// 当前状态

    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;

    public LoadPager(Context context) {
        super(context);
        initView();
    }

    public LoadPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        // 正在加载
        if (mLoadingView == null) {
            mLoadingView = onCreateLoadingView();
            addView(mLoadingView);
        }

        // 加载失败
        if (mErrorView == null) {
            mErrorView = onCreateErrorView();
            // 点击重试
            Button btn = (Button) mErrorView.findViewById(R.id.load_eor_btn);
            btn.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loadData();
                                }
                            });
            addView(mErrorView);
        }

        // 数据为空
        if (mEmptyView == null) {
            mEmptyView = onCreateEmptyView();
            addView(mEmptyView);
        }

        //刷新UI
        showRightPage();

        //加载数据
        loadData();
    }

    /**
     * 根据当前状态,展示正确页面
     */
    private void showRightPage() {
        if (mLoadingView != null) {
            mLoadingView
                    .setVisibility((mCurrentState == STATE_LOAD_LOADING || mCurrentState == STATE_LOAD_UNDO) ? View.VISIBLE
                            : View.GONE);
        }

        if (mEmptyView != null) {
            mEmptyView
                    .setVisibility(mCurrentState == STATE_LOAD_EMPTY ? View.VISIBLE
                            : View.GONE);
        }

        if (mErrorView != null) {
            mErrorView
                    .setVisibility(mCurrentState == STATE_LOAD_ERROR ? View.VISIBLE
                            : View.GONE);
        }

        // 数据获取成功时，才加载布局
        if (mSuccessView == null && mCurrentState == STATE_LOAD_SUCCESS) {
            mSuccessView = onCreateSuccessView();
            if (mSuccessView != null) {// 防止子类返回null
                addView(mSuccessView);
            }
        }

        if (mSuccessView != null) {
            mSuccessView
                    .setVisibility(mCurrentState == STATE_LOAD_SUCCESS ? View.VISIBLE
                            : View.GONE);
        }
    }

    /**
     * 初始化正在加载布局
     */
    private View onCreateLoadingView() {
        return UIUtils.inflate(R.layout.pager_load);
    }

    /**
     * 初始化加载失败布局
     */
    private View onCreateErrorView() {
        return UIUtils.inflate(R.layout.pager_error);
    }

    /**
     * 初始化数据为空布局
     */
    private View onCreateEmptyView() {
        return UIUtils.inflate(R.layout.pager_empty);
    }

    /**
     * 初始化访问成功布局, 子类必须实现
     */
    public abstract View onCreateSuccessView();

    /**
     * 加载数据
     */


    public void loadData() {

        if (mCurrentState !=STATE_LOAD_LOADING) {
            mCurrentState = STATE_LOAD_LOADING;

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    // 开始加载网络数据
//                    final ResultState state = onLoadData();
//                    UIUtils.runOnMainThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (state != null) {
//                                // 更新当前状态
//                                mCurrentState = state.getState();
//                                // 更新当前页面
//                                showRightPage();
//                            }
//
//                        }
//                    });
//                }
//            }).start();

            //使用线程池来管理线程的使用
            ThreadManager.ThreadPool threadPool = ThreadManager.getThreadPool();
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    // 开始加载网络数据
                    final ResultState state = onLoadData();
                    UIUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (state != null) {
                                // 更新当前状态
                                mCurrentState = state.getState();
                                // 更新当前页面
                                showRightPage();
                            }

                        }
                    });
                }
            });
        }
    }

    /**
     * 加载网络数据,必须子类实现
     *
     * @return 返回加载状态
     */
    public abstract ResultState onLoadData();

    /**
     * 使用枚举表示访问网络的几种状态
     */
    public enum ResultState {
        STATE_SUCCESS(STATE_LOAD_SUCCESS), // 访问成功
        STATE_EMPTY(STATE_LOAD_EMPTY), // 数据为空
        STATE_FAIL(STATE_LOAD_ERROR);// 访问失败

        private int state;

        private ResultState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }
}
