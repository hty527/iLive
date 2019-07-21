package com.android.gift.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import com.android.gift.manager.ApplicationManager;
import com.android.gift.service.bean.BuildMessageInfo;
import com.android.gift.util.Logger;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TinyHung@Outlook.com
 * 2017/12/1
 * 版本更新文件下载及安装
 */

public class DownLoadService extends Service {

    private static final String TAG = "DownLoadService";
    private DownloadManager manager;
    private DownloadCompleteReceiver receiver;
    private File mFilePath;//文件输出位置
    private String url;
    private boolean isDownload=false;
    private long mEnqueueID;
    private Timer mTimer;
    private Handler mHandler;
    private int mTotalSizeBytes=-1;
    private boolean isRegisterReceiver;//是否已注册广播
    //开始下载
    public static final String BUILD_START = "build_start";
    //下载中
    public static final String BUILD_DOWNLOAD = "build_download";
    //正在下载中，防止重新下载
    public static final String BUILD_DOWNLOADING = "build_downloading";
    //结束下载
    public static final String BUILD_END = "build_end";
    //下载失败
    public static final String BUILD_ERROR = "build_error";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(isDownload){
            BuildMessageInfo buildMessageInfo=new BuildMessageInfo();
            buildMessageInfo.setCmd(BUILD_DOWNLOADING);
            ApplicationManager.getInstance().observerUpdata(buildMessageInfo);
            return Service.START_NOT_STICKY;
        }
        url = intent.getStringExtra("url");
        try{
            initDownManager();
        }catch (RuntimeException e){
            e.printStackTrace();
            BuildMessageInfo buildMessageInfoError=new BuildMessageInfo();
            //更新下载失败状态
            buildMessageInfoError.setCmd(BUILD_ERROR);
            ApplicationManager.getInstance().observerUpdata(buildMessageInfoError);
            e.printStackTrace();
            try {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent0 = new Intent(Intent.ACTION_VIEW, uri);
                intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return Service.START_NOT_STICKY;
    }

    /**
     * 初始化下载
     */
    private void initDownManager() {
        mFilePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), getFileName(url));
        if (mFilePath.exists()) {
            mFilePath.delete();
        }
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        down.setAllowedOverRoaming(false);
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
//        down.setMimeType(mimeString);
        down.setMimeType("application/vnd.android.package-archive");
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        down.setVisibleInDownloadsUi(true);
        down.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getFileName(url));
        down.setTitle(" 下载中...");
        //返回一个任务ID，用来获取任务进度
        mEnqueueID = manager.enqueue(down);
        //更新下载中状态
        BuildMessageInfo buildMessageInfo=new BuildMessageInfo();
        buildMessageInfo.setCmd(BUILD_START);
        ApplicationManager.getInstance().observerUpdata(buildMessageInfo);
        isDownload=true;
        mHandler = new Handler();
        //开启定时任务查询下载状态
        startQueryProgress();
        if(!isRegisterReceiver) {
            isRegisterReceiver=true;
            registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    /**
     * 通过Url切割文件名
     * @param url
     * @return
     */
    private String getFileName(String url) {
        if(TextUtils.isEmpty(url)){
            return "";
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 开始定时查询下载状态
     */
    private void startQueryProgress() {
        mTimer = new Timer();
        mTimer.schedule(queryDownloadTask,0,100);//开启定时任务
    }

    private void stopQueryProgress(){
        if(null!=mTimer) mTimer.cancel(); mTimer=null;
    }

    private TimerTask queryDownloadTask=new TimerTask() {
        @Override
        public void run() {
            queryProgress();
        }
    };

    /**
     * 查询下载进度
     */
    private void queryProgress() {
        if(null!=manager&&0!=mEnqueueID){
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(mEnqueueID);
            Cursor cursor = null;
            try {
                cursor = manager.query(query);
                if (cursor != null && cursor.moveToFirst()) {
                    //已经下载文件大小
                    final int bytesDownloadSoFarIndex = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    //下载文件的总大小
                    if(-1==mTotalSizeBytes){
                        mTotalSizeBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    }
                    if(null!=mHandler){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                BuildMessageInfo buildMessageInfo=new BuildMessageInfo();
                                buildMessageInfo.setCmd(BUILD_DOWNLOAD);
                                buildMessageInfo.setTotalSize(mTotalSizeBytes);
                                buildMessageInfo.setDownloadSize(bytesDownloadSoFarIndex);
                                ApplicationManager.getInstance().observerUpdata(buildMessageInfo);
                            }
                        });
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    /**
     * 监听的广播
     */
    private class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                isDownload=false;
                BuildMessageInfo buildMessageInfo=new BuildMessageInfo();
                //更新下载完成状态
                buildMessageInfo.setCmd(BUILD_END);
                buildMessageInfo.setTotalSize(mTotalSizeBytes);
                buildMessageInfo.setDownloadSize(mTotalSizeBytes);
                ApplicationManager.getInstance().observerUpdata(buildMessageInfo);
                stopQueryProgress();
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if(manager.getUriForDownloadedFile(downId)!=null){
                    if(null!=mFilePath&&mFilePath.isFile()&&mFilePath.exists()){
                        installApk(mFilePath);
                    }
                }else{
                    BuildMessageInfo buildMessageInfoError=new BuildMessageInfo();
                    //更新下载失败状态
                    buildMessageInfoError.setCmd(BUILD_ERROR);
                    ApplicationManager.getInstance().observerUpdata(buildMessageInfoError);
                }
                DownLoadService.this.stopSelf();
            }
        }
    }

    /**
     * 安装App
     * @param file
     */
    protected void installApk(File file) {
        Logger.d(TAG,"file:"+file.getAbsolutePath());
        if (file == null || !file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        int sdkVersion = this.getApplicationInfo().targetSdkVersion;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && sdkVersion >= 24) {
            // 即是在清单文件中配置的authorities
            uri = FileProvider.getUriForFile(this, getApplication().getPackageName() + ".fileProvider", file);//newfile
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            uri = Uri.parse("file://" + file.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }


    public static boolean deleteFileWithPath(String filePath) {
        SecurityManager checker = new SecurityManager();
        File f = new File(filePath);
        checker.checkDelete(filePath);
        if (f.isFile()) {
            f.delete();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isRegisterReceiver&&null!=receiver){
            isRegisterReceiver=false;
            try {
                unregisterReceiver(receiver);
            }catch (RuntimeException e){
            }
        }
        stopQueryProgress();
        if(null!=mHandler) mHandler.removeMessages(0);
        mTotalSizeBytes=-1;
    }
}