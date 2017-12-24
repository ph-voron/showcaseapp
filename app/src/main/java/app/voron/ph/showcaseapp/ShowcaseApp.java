package app.voron.ph.showcaseapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import app.voron.ph.showcaseapp.Utilities.AssetsHelper;

/**
 * Created by TJack on 21.10.2017.
 */

public class ShowcaseApp extends Application {
    private static Context mContext = null;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
    //
    public static Context getContext(){
        return mContext;
    }
    //
    public static String getTextFromAsset(String resName){
        return AssetsHelper.getTextFromAsset(getContext(), resName);
    }
    //
    public static Bitmap getBitmapFromAsset(String resName){
        return AssetsHelper.getBitmapFromAsset(getContext(), resName);
    }
}
