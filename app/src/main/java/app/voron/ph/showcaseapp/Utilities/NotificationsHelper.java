package app.voron.ph.showcaseapp.Utilities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import app.voron.ph.showcaseapp.R;

/**
 * Created by TJack on 09.11.2017.
 */

public class NotificationsHelper {
    public static void showOnCartAddSnackbar(Context context,
                                             CoordinatorLayout coordinatorLayout,
                                             int productsCount,
                                             View.OnClickListener listener) {
        String notification = context.getResources().getString(R.string.add_to_cart_notification);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout,
                        String.format(notification, String.valueOf(productsCount)),
                        Snackbar.LENGTH_SHORT)
                .setAction(R.string.add_to_cart_notification_action, listener);
        snackbar.show();
    }
    //
    public static void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    //
    public static void showToast(Context context, int resId){
        String text =  context.getResources().getString(resId);
        showToast(context, text);
    }
}
