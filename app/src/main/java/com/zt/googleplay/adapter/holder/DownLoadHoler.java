package com.zt.googleplay.adapter.holder;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.zt.googleplay.R;
import com.zt.googleplay.bean.AppInfo;
import com.zt.googleplay.bean.DownLoadInfo;
import com.zt.googleplay.manager.DownloadManager;
import com.zt.googleplay.ui.view.ProgressHorizontal;
import com.zt.googleplay.utils.UIUtils;

/**
 * Author: ZT on 2017/1/6.
 */
public class DownLoadHoler extends BaseViewHolder<AppInfo> implements View.OnClickListener,DownloadManager.DownLoadOberver{
    private Button selectBut,downBut, shareBut;
    private DownloadManager downloadManager;
    private FrameLayout fl;
    private int currentState;
    private float lastProgress;
    private ProgressHorizontal progress;

    @Override
    protected View initView() {
        View view = UIUtils.inflate(R.layout.download_ui);
        selectBut = (Button) view.findViewById(R.id.down_select);
        downBut = (Button) view.findViewById(R.id.down_downbut);
        shareBut = (Button) view.findViewById(R.id.down_share);
        selectBut.setOnClickListener(this);
        downBut.setOnClickListener(this);
        shareBut.setOnClickListener(this);
        fl = (FrameLayout) view.findViewById(R.id.fl_progress);
        fl.setOnClickListener(this);
        //new 自定义水平进度条
        progress = new ProgressHorizontal(UIUtils.getContext());
        progress.setProgressBackgroundResource(R.drawable.progress_bg);//设置进度条的背景图片
        progress.setProgressResource(R.drawable.progress_normal);//设置进度图片
        progress.setProgressTextColor(Color.WHITE);//设置进度文字颜色
        progress.setProgressTextSize(UIUtils.dip2px(18));//设置文字大小

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fl.addView(progress,lp);

        //注册观察者
        downloadManager = DownloadManager.getDownloadManager();
        downloadManager.registerOberver(this);

        return view;
    }
    @Override
    public void refreshView(AppInfo data) {
        //进入详情页后判断上次是否有下载过
        DownLoadInfo downLoadInfo = downloadManager.getDownLoadInfo(data.id);
        if(downLoadInfo!=null){ //下载过
             currentState = downLoadInfo.currentState; //获取状态
             lastProgress = downLoadInfo.getProgress();//上次的下载进度
        }else {
            //没下载
            currentState = DownloadManager.STATE_UNDO;
            lastProgress = 0;
        }
        refreshUI(currentState,lastProgress);
    }
    private void refreshUI(int currentState,float lastProgress){
            this.currentState = currentState;
            this.lastProgress = lastProgress;

            switch (currentState){
                case DownloadManager.STATE_UNDO:
                    downBut.setVisibility(View.GONE);
                    fl.setVisibility(View.VISIBLE);
                    progress.setCenterText("下载");
                    break;
                case DownloadManager.STATE_WAITE:
                    downBut.setVisibility(View.GONE);
                    fl.setVisibility(View.VISIBLE);
                    progress.setCenterText("等待中...");
                    break;
                case DownloadManager.STATE_DOWNLOADING:
                    downBut.setVisibility(View.GONE);
                    fl.setVisibility(View.VISIBLE);
                    progress.setCenterText("");
                    progress.setProgress(lastProgress);
                    break;
                case DownloadManager.STATE_PAUSE:
                    downBut.setVisibility(View.GONE);
                    fl.setVisibility(View.VISIBLE);
                    progress.setCenterText("暂停");
                    progress.setProgress(lastProgress);
                    break;
                case DownloadManager.STATE_SUCCESS:
                    downBut.setVisibility(View.VISIBLE);
                    fl.setVisibility(View.GONE);
                    downBut.setText("安装");
                    break;
                case DownloadManager.STATE_ERROR:
                    downBut.setVisibility(View.VISIBLE);
                    fl.setVisibility(View.GONE);
                    downBut.setText("下载失败");
                    break;
            }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.down_select:
                Toast.makeText(UIUtils.getContext(), "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.down_downbut:
            case R.id.fl_progress:
                if(currentState==DownloadManager.STATE_UNDO || currentState==DownloadManager.STATE_PAUSE ||
                        currentState==DownloadManager.STATE_ERROR ){
                    downloadManager.down(getData());//开始下载
                }else if(currentState==DownloadManager.STATE_DOWNLOADING || currentState==DownloadManager.STATE_WAITE){
                    downloadManager.pause(getData());//暂停下载
                }else if(currentState==DownloadManager.STATE_SUCCESS){
                    downloadManager.install(getData());//安装
                }
                break;
            case R.id.down_share:
                Toast.makeText(UIUtils.getContext(), "分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void downloadStateChange( DownLoadInfo downLoadInfo) {
        refreshUIonMainThread(downLoadInfo);
    }
    @Override
    public void downloadProgressChange( DownLoadInfo downLoadInfo) {
        refreshUIonMainThread(downLoadInfo);
    }
    //主线程更新UI
    private void refreshUIonMainThread(final DownLoadInfo downLoadInfo){
        AppInfo appInfo = getData();
        if(downLoadInfo.id.equals(appInfo.id)){
            UIUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    refreshUI(downLoadInfo.currentState,downLoadInfo.getProgress());
                }
            });
        }
    }
}
