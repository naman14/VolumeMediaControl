package com.naman14.volumemedia;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.media.AudioManager;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

public class Utils {

    public static void forceVolumeControlStream(Context context, int type){
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Class param1 = Integer.TYPE;
        Object ret;
        try{
            ret = am.getClass().getMethod("forceVolumeControlStream", new Class[] { param1 }).invoke(am, new Object[] { type });
        }
        catch (Exception e)
        {
            e.printStackTrace();
//            Log.d(TAG, "Exception : " + e.toString());
            return;
        }
    }

    public static boolean checkAccessibilityPermission(Context context) {
        AccessibilityManager accessibilityManager = (AccessibilityManager)context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : list) {
            if (info.getResolveInfo().serviceInfo.packageName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


}