package app.voron.ph.showcaseapp.Utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

import app.voron.ph.showcaseapp.R;

/**
 * Created by TJack on 08.10.2017.
 */

public class ImageHelper {
    //
    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            //
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(rect);
            float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o){
        }
        return result;
    }
    //
    public static Bitmap prepareAvatar(Bitmap sourceBmp, Context context){
        int valueInPixels = (int) context.getResources().getDimension(R.dimen.nav_avatar_size);
        return getRoundedRectBitmap(sourceBmp, valueInPixels);
    }
    //
    public static Bitmap prepareAvatar(int resId, Context context){
        Bitmap source = BitmapFactory.decodeResource(context.getResources(), resId);
        return prepareAvatar(source, context);
    }
    //
    public static Bitmap getBitmapForFoodId(int foodId, Context context){
        int resId = 0;
        switch (foodId){
            case 1:
                resId = R.drawable.ic_food_bacon;
                break;
            case 2:
                resId = R.drawable.ic_food_chicken;
                break;
            case 3:
                resId = R.drawable.ic_food_chicken_leg;
                break;
            case 4:
                resId = R.drawable.ic_food_crab;
                break;
            case 5:
                resId = R.drawable.ic_food_fish;
                break;
            case 6:
                resId = R.drawable.ic_food_fried_egg;
                break;
            case 7:
                resId = R.drawable.ic_food_hamburger;
                break;
            case 8:
                resId = R.drawable.ic_food_mushroom;
                break;
            case 9:
                resId = R.drawable.ic_food_pancakes;
                break;
            case 10:
                resId = R.drawable.ic_food_pizza;
                break;
            case 11:
                resId = R.drawable.ic_food_pumpkin;
                break;
            case 12:
                resId = R.drawable.ic_food_shrimp;
                break;
            case 13:
                resId = R.drawable.ic_food_soup;
                break;
            case 14:
                resId = R.drawable.ic_food_spaghetti;
                break;
            case 15:
                resId = R.drawable.ic_food_sushi;
                break;
            case 16:
                resId = R.drawable.ic_food_tomato;
                break;
            case 17:
                resId = R.drawable.ic_food_steak;
                break;
            case 18:
                resId = R.drawable.ic_food_salad;
                break;
            case 19:
                resId = R.drawable.ic_food_strawberry;
                break;
            case 20:
                resId = R.drawable.ic_food_hot_drinks;
                break;
            default:
                resId = R.drawable.ic_food_soup;
                break;
        }
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

}
