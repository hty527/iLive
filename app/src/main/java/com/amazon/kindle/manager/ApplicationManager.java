package com.amazon.kindle.manager;

import android.os.Build;
import android.os.StrictMode;
import com.amazon.kindle.observer.SubjectObservable;
import java.lang.ref.WeakReference;
import java.util.Observer;

/**
 * TinyHung@Outlook.com
 * 2017/12/12.
 * 持有数据库管理者对象，缓存管理者对象等。。当内存不足时释放这些对象
 */

public class ApplicationManager {

    private static final String TAG = "ApplicationManager";
    private static WeakReference<ApplicationManager> mInstanceWeakReference=null;//自己
    public static SubjectObservable mObservableWeakReference;//观察者

    public static synchronized ApplicationManager getInstance(){
        synchronized (ApplicationManager.class){
            if(null!=mInstanceWeakReference&&null!=mInstanceWeakReference.get()){
                return mInstanceWeakReference.get();
            }
            ApplicationManager applicationManager=new ApplicationManager();
            mInstanceWeakReference=new WeakReference<ApplicationManager>(applicationManager);
        }
        return mInstanceWeakReference.get();
    }

    public ApplicationManager(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    //添加一个观察者
    public void addObserver(Observer observer){
        if(null==mObservableWeakReference){
            mObservableWeakReference=new SubjectObservable();
        }
        mObservableWeakReference.addObserver(observer);
    }
    //移除一个观察者
    public void removeObserver(Observer observer){
        if(null!=mObservableWeakReference) mObservableWeakReference.deleteObserver(observer);
    }
    //移除所有观察者
    public void removeAllObserver(){
        if(null!=mObservableWeakReference) mObservableWeakReference.deleteObservers();
    }
    //刷新
    public void observerUpdata(Object obj){
        if(null!=mObservableWeakReference) mObservableWeakReference.updataSubjectObserivce(obj);
    }

    public void onDestory(){
        removeAllObserver();
        if(null!=mInstanceWeakReference){
            mInstanceWeakReference.clear();
            mInstanceWeakReference=null;
        }
        mObservableWeakReference=null;
    }
}
