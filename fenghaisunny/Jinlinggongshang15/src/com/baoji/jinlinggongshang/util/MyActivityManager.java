package com.baoji.jinlinggongshang.util;

import java.util.LinkedList;
import java.util.List;
 
import android.app.Activity;
import android.app.Application;
 
/**2014-2-12
 * @author sunhaifeng
 *
 */
public class MyActivityManager extends Application {
    private List<Activity> activitys = null;
    private static MyActivityManager instance;
 
    private MyActivityManager() {
        activitys = new LinkedList<Activity>();
    }
 
    /**
     * ����ģʽ�л�ȡΨһ��MyActivityManagerʵ��
     * 
     */
    public static MyActivityManager getInstance() {
        if (null == instance) {
            instance = new MyActivityManager();
        }
        return instance;
 
    }
 
    // ���Activity��������
    public void addActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if(!activitys.contains(activity)){
                activitys.add(activity);
            }
        }else{
            activitys.add(activity);
        }
         
    }
    
    public void removeAvtivity(Activity activity){
        if (activitys != null && activitys.size() > 0) {
            if(activitys.contains(activity)){
                activitys.remove(activity);
            }
        }else{
            return;
        }
    	
    }
 
    // ��������Activity��finish
    public void exit() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }
        System.exit(0);
    }
 
}
 