package app.voron.ph.showcaseapp.System;

import app.voron.ph.showcaseapp.System.Providers.BasicDataRequestProvider;
import app.voron.ph.showcaseapp.System.Providers.DataRequestProvider;

/**
 * Created by TJack on 15.10.2017.
 */

public class DependencyResolver {
    //
    private static DataRequestProvider mDataRequestProvider = null;
    //
    public static DataRequestProvider getDataRequestProvider(){
        if(mDataRequestProvider == null){
            mDataRequestProvider = new BasicDataRequestProvider();
        }
        return mDataRequestProvider;
    }
}
