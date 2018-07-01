package com.zt.googleplay.adapter.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zt.googleplay.R;
import com.zt.googleplay.bean.AppInfo;
import com.zt.googleplay.bean.DownLoadInfo;
import com.zt.googleplay.http.HttpHelper;
import com.zt.googleplay.manager.DownloadManager;
import com.zt.googleplay.ui.view.ProgressArc;
import com.zt.googleplay.utils.MyBitmapUtils;
import com.zt.googleplay.utils.UIUtils;

/**
 * Author: ZT on 2016/12/28.
 */
public class HomeHolder extends BaseViewHolder<AppInfo> implements DownloadManager.DownLoadOberver,View.OnClickListener{
    private ImageView appIcon;
    private TextView appName,appSize, downText,appDes;
    private RatingBar appStars;
    private DownloadManager mDM;
    private ProgressArc progressArc;
    private FrameLayout frameLayout;
    private int currentState;
    private float lastProgress;

    //加载listview的item的布局，init控件
    @Override
    protected View initView() {
        View view = UIUtils.inflate(R.layout.list_item_home);
        initFindView(view);
        //注册观察者
        mDM = DownloadManager.getDownloadManager();
        mDM.registerOberver(this);
        return view;
    }
    //init 控件
    private void initFindView(View view){
        appIcon = (ImageView) view.findViewById(R.id.list_home_icon);
        frameLayout = (FrameLayout) view.findViewById(R.id.list_home_framelayout);
        appName = (TextView) view.findViewById(R.id.list_home_appname);
        appStars = (RatingBar) view.findViewById(R.id.list_home_starts);
        downText = (TextView) view.findViewById(R.id.list_home_downText);
        appDes = (TextView) view.findViewById(R.id.list_home_appdes);
        appSize = (TextView) view.findViewById(R.id.list_home_appsize);

        progressArc = new ProgressArc(UIUtils.getContext());
        //设置圆形进度条的直径
        progressArc.setArcDiameter(UIUtils.dip2px(26));
        //设置进度条颜色
        progressArc.setProgressColor(UIUtils.getColor(R.color.progress));
        //设置进度条的布局参数
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(UIUtils.dip2px(27),UIUtils.dip2px(27));
        frameLayout.addView(progressArc,lp);
        //设置点击事件
        frameLayout.setOnClickListener(this);
    }


    //空item的控件赋值
    @Override
    public void refreshView(final AppInfo data) {
        MyBitmapUtils.getInstanceBitmapUtils().display(appIcon, HttpHelper.URL+"image?name="+data.iconUrl);
        appName.setText(data.name);
        String size = Formatter.formatFileSize(UIUtils.getContext(), data.size);//long格式转化为MB文件格式
        appSize.setText(size);
        appDes.setText(data.des);
        appStars.setRating(data.stars);//星星数


        DownLoadInfo downLoadInfo = mDM.getDownLoadInfo(data.id);
        if(downLoadInfo!=null){ //下载过
            currentState = downLoadInfo.currentState; //获取状态
            lastProgress = downLoadInfo.getProgress();//上次的下载进度
        }else {
            //没下载
            currentState = DownloadManager.STATE_UNDO;
            lastProgress = 0;
        }
        refreshUI(currentState,lastProgress,data.id);
    }
    private void refreshUI(int currentState,float lastProgress,String id){
        //解决listview的重用机制，造成的进度条bug显示
        if(!getData().id.equals(id)){
            return;
        }

        this.currentState = currentState;
        this.lastProgress = lastProgress;

        switch (currentState){
            case DownloadManager.STATE_UNDO:
                progressArc.setBackgroundResource(R.mipmap.ic_download);
                //没有进度
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
               downText.setText("下载");
                break;
            case DownloadManager.STATE_WAITE:
                progressArc.setBackgroundResource(R.mipmap.ic_download);
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
                downText.setText("等待");
                break;
            case DownloadManager.STATE_DOWNLOADING:
                progressArc.setBackgroundResource(R.mipmap.ic_pause);
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                progressArc.setProgress(lastProgress,true);
                downText.setText((int)(lastProgress*100)+"%");
                break;
            case DownloadManager.STATE_PAUSE:
                progressArc.setBackgroundResource(R.mipmap.ic_resume);
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                break;
            case DownloadManager.STATE_SUCCESS:
                progressArc.setBackgroundResource(R.mipmap.ic_download);
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                downText.setText("安装");
                break;
            case DownloadManager.STATE_ERROR:
                progressArc.setBackgroundResource(R.mipmap.ic_install);
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                downText.setText("下载失败");
                break;
        }
    }

    @Override
    public void downloadStateChange(DownLoadInfo downLoadInfo) {
        refreshUIonMainThread(downLoadInfo);
    }

    @Override
    public void downloadProgressChange(DownLoadInfo downLoadInfo) {
        refreshUIonMainThread(downLoadInfo);
    }

    //主线程更新UI
    private void refreshUIonMainThread(final DownLoadInfo downLoadInfo){
        AppInfo appInfo = getData();
        if(downLoadInfo.id.equals(appInfo.id)){
            UIUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    refreshUI(downLoadInfo.currentState,downLoadInfo.getProgress(),downLoadInfo.id);
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.list_home_framelayout:
                if(currentState==DownloadManager.STATE_UNDO || currentState==DownloadManager.STATE_PAUSE ||
                        currentState==DownloadManager.STATE_ERROR ){
                    mDM.down(getData());//开始下载
                }else if(currentState==DownloadManager.STATE_DOWNLOADING || currentState==DownloadManager.STATE_WAITE){
                    mDM.pause(getData());//暂停下载
                }else if(currentState==DownloadManager.STATE_SUCCESS){
                    mDM.install(getData());//安装
                }
                break;
        }
    }
}
