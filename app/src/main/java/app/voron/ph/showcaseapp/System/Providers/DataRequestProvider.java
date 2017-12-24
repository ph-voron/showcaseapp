package app.voron.ph.showcaseapp.System.Providers;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import java.util.List;

import app.voron.ph.showcaseapp.Models.OrderingCompletedModel;
import app.voron.ph.showcaseapp.Models.OrderingDataModel;
import app.voron.ph.showcaseapp.Models.ReviewItemDataModel;
import app.voron.ph.showcaseapp.Models.ShowcaseFilteringOptionsModel;
import app.voron.ph.showcaseapp.Models.ShowcaseItemDataModel;
import app.voron.ph.showcaseapp.System.Tasks.TaskCancelationToken;

/**
 * Created by TJack on 15.10.2017.
 */

public interface DataRequestProvider {
    void cancelAllRequests();
    //
    void beginRequestShowcaseItems(ShowcaseFilteringOptionsModel options,
                                   OnResultListener<List<ShowcaseItemDataModel>> listener);
    //
    void beginRequestShowcaseItemById(String itemId,
                                      OnResultListener<ShowcaseItemDataModel> listener);
    //
    void beginRequestReviews(String productId, int limit, OnResultListener<List<ReviewItemDataModel>> listener);
    //
    void beginPutProductIntoCart(String productId, OnResultListener<Integer> listener);
    //
    void beginRequestCartItems(OnResultListener<List<ShowcaseItemDataModel>> listener);
    //
    void beginRemoveItemFromCart(String id, OnResultListener<Integer> listener);
    //
    void beginClearCart(OnResultListener<Void> listener);
    //
    void beginSubmitOrdering(OrderingDataModel model, OnResultListener<OrderingCompletedModel> listener);
    //
    void beginRequestLastOrdering(final OnResultListener<OrderingDataModel> listener);
    //
    Bitmap requestImage(String name);
    void beginRequestImage(String name, OnResultListener<Bitmap> listener);
}
