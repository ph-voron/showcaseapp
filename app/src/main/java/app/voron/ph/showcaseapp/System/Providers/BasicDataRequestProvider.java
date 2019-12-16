package app.voron.ph.showcaseapp.System.Providers;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import app.voron.ph.showcaseapp.Models.OrderingCompletedModel;
import app.voron.ph.showcaseapp.Models.OrderingDataModel;
import app.voron.ph.showcaseapp.Models.ReviewItemDataModel;
import app.voron.ph.showcaseapp.Models.ShowcaseFilteringOptionsModel;
import app.voron.ph.showcaseapp.Models.ShowcaseItemDataModel;
import app.voron.ph.showcaseapp.ShowcaseApp;


/**
 * Created by TJack on 15.10.2017.
 */

public class BasicDataRequestProvider implements DataRequestProvider {
    //
    public static final String SHOWCASE_JSON_FILE = "showcase.json";
    public static final String REVIEWS_JSON_FILE = "reviews.json";
    //
    private class ShowcaseItemAssetDataModel{
        @SerializedName("id")
        public String id = null;
        @SerializedName("title")
        public String title = null;
        @SerializedName("description")
        public String description = null;
        @SerializedName("images")
        public List<String> images = null;
        @SerializedName("reviews")
        public List<String> reviews = null;
        @SerializedName("reviewsCount")
        public int reviewsCount = 0;
        @SerializedName("cost")
        public float cost = 0;
        @SerializedName("rating")
        public float rating = 0;
        @SerializedName("time")
        public String time = null;
        @SerializedName("foodCategory")
        public int foodCategory = 0;
        @SerializedName("categoryTags")
        public List<String> categoryTags = null;
        //
        public ShowcaseItemDataModel getModel(){
            ShowcaseItemDataModel model = new ShowcaseItemDataModel();
            model.title = title;
            model.description = description;
            model.id = id;
            model.cost = cost;
            model.timeFormatted = time;
            model.rating = rating;
            model.reviewsCount = reviewsCount;
            model.previewImageName = id + "p.jpg";
            model.coverImageName = id + ".jpg";
            model.foodCatecory = foodCategory;
            model.categoryTags = categoryTags;
            return model;
        }
    }
    //
    private class ReviewItemAssetDataModel{
        @SerializedName("id")
        public String id = null;
        @SerializedName("itemId")
        public String itemId = null;
        @SerializedName("rate")
        public float rate = 0;
        @SerializedName("name")
        public String name = null;
        @SerializedName("text")
        public String text = null;
        //
        public ReviewItemDataModel getModel(){
            ReviewItemDataModel model = new ReviewItemDataModel();
            model.id = id;
            model.itemId = itemId;
            model.name = name;
            model.rate = rate;
            model.text = text;
            return model;
        }
    }
    //
    private Map<String, Integer> mCartData = new HashMap<>();
    private OrderingDataModel mCurrentOrderingData = null;
    private List<WeakReference<Thread>> mTaskList = new ArrayList<>();
    //
    private void addTask(Thread thread){
        synchronized (mTaskList){
            mTaskList.add(new WeakReference<>(thread));
        }
    }
    //
    private void addTask(final Runnable runnable){
        final Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    runnable.run();
                }
                catch (Exception e){
                    Log.e("DataProvider", e.getMessage());
                }
                finally {
                    removeCurrentTask();
                }
            }
        });
        addTask(worker);
        worker.start();
    }
    //
    private void removeTask(Thread thread){
        synchronized (mTaskList){
            int i = 0;
            while (i < mTaskList.size()){
                WeakReference<Thread> item = mTaskList.get(i);
                Thread threadItem = item.get();
                if(thread == null){
                    mTaskList.remove(item);
                    Log.i("DataProvider", "Nulled object removed from task list");
                    continue;
                }
                if(threadItem.hashCode() == thread.hashCode()){
                    mTaskList.remove(item);
                    Log.i("DataProvider", "Specified object was removed from task list");
                    continue;
                }
                i++;
            }
        }
    }
    //
    private void removeCurrentTask(){
        removeTask(Thread.currentThread());
    }
    //
    private void termitateAllTasks(){
        synchronized (mTaskList){
            for (int i = 0; i < mTaskList.size(); i++){
                WeakReference<Thread> item = mTaskList.get(i);
                Thread threadItem = item.get();
                if(threadItem != null){
                    try{
                        threadItem.interrupt();
                    }
                    catch (Exception e){
                        Log.e("DataProvider", "unable to terminate task");
                    }
                }
            }
            mTaskList.clear();
        }
    }
    //
    private <T> void passResponseToListener(final T response, final Exception exception, final OnResultListener<T> listener){
        if(Thread.currentThread().isInterrupted() ||
                (exception != null && exception instanceof InterruptedException)){
            Log.i("DataProvider", "Task was interrupted");
            return;
        }
        //
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(listener != null){
                    listener.onResult(response, exception);
                }
            }
        });
    }
    //
    private ShowcaseItemAssetDataModel[] getShowcaseAssets(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String showcaseJson = ShowcaseApp.getTextFromAsset(SHOWCASE_JSON_FILE);
        return gson.fromJson(showcaseJson, ShowcaseItemAssetDataModel[].class);
    }
    //
    private ReviewItemAssetDataModel[] getReviewsAssets(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String reviewsJson = ShowcaseApp.getTextFromAsset(REVIEWS_JSON_FILE);
        return gson.fromJson(reviewsJson, ReviewItemAssetDataModel[].class);
    }
    //
    private ShowcaseItemDataModel getShowcaseItemDataModelById(String id){
        ShowcaseItemAssetDataModel[] showcaseData = getShowcaseAssets();
        for (ShowcaseItemAssetDataModel item : showcaseData) {
            if(item.id.equals(id)) {
                return item.getModel();
            }
        }
        return null;
    }
    //
    @Override
    public void cancelAllRequests() {
        termitateAllTasks();
    }

    @Override
    public void beginRequestShowcaseItems(final ShowcaseFilteringOptionsModel options,
                                          final OnResultListener<List<ShowcaseItemDataModel>> listener) {
        addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    ShowcaseItemAssetDataModel[] showcaseData = getShowcaseAssets();
                    ArrayList<ShowcaseItemDataModel> result = new ArrayList<>();
                    for (ShowcaseItemAssetDataModel item : showcaseData) {
                        ShowcaseItemDataModel resultModel = item.getModel();
                        if (options != null) {
                            if (options.searchText != null && options.searchText.length() > 0) {
                                if (!resultModel.title.toLowerCase().contains(options.searchText.toLowerCase()) &&
                                        !resultModel.description.toLowerCase().contains(options.searchText.toLowerCase())) {
                                    continue;
                                }
                            }
                            if(options.categoryTag != null && options.categoryTag.length() > 0){
                                if(!resultModel.categoryTags.contains(options.categoryTag)){
                                    continue;
                                }
                            }
                        }
                        if(mCartData.containsKey(resultModel.id)){
                            resultModel.itemsInCart = mCartData.get(resultModel.id);
                        }
                        result.add(resultModel);
                    }
                    passResponseToListener(result, null, listener);

                }
                catch (Exception e){
                    passResponseToListener(null, e, listener);
                }
            }
        });

    }
    //
    @Override
    public void beginRequestShowcaseItemById(final String itemId,
                                             final OnResultListener<ShowcaseItemDataModel> listener) {
        addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    ShowcaseItemDataModel item = getShowcaseItemDataModelById(itemId);
                    if (mCartData.containsKey(itemId) && item != null) {
                        item.itemsInCart = mCartData.get(itemId);
                    }
                    passResponseToListener(item, null, listener);
                } catch (Exception e) {
                    passResponseToListener(null, e, listener);
                }
            }
        });
    }
    //
    @Override
    public void beginRequestReviews(String productId, final int limit,
                                    final OnResultListener<List<ReviewItemDataModel>> listener){
        addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    ReviewItemAssetDataModel[] reviewsData = getReviewsAssets();
                    ArrayList<ReviewItemDataModel> result = new ArrayList<ReviewItemDataModel>();
                    for (ReviewItemAssetDataModel item : reviewsData) {
                        result.add(item.getModel());
                        if(limit > 0 && result.size() >= limit)
                            break;
                    }
                    Thread.sleep(1000);
                    passResponseToListener(result, null, listener);
                }
                catch (Exception e){
                    passResponseToListener(null, e, listener);
                }
            }
        });
    }

    @Override
    public Bitmap requestImage(String name) {
        return ShowcaseApp.getBitmapFromAsset(name);
    }
    //
    @Override
    public void beginRequestImage(final String name, final OnResultListener<Bitmap> listener){
        addTask(new Runnable() {
            @Override
            public void run() {
                try{
                    Bitmap result = ShowcaseApp.getBitmapFromAsset(name);
                    passResponseToListener(result, null, listener);
                }
                catch (Exception e){
                    passResponseToListener(null, e, listener);
                }
            }
        });
    }
    //
    @Override
    public void beginPutProductIntoCart(final String productId,
                                        final OnResultListener<Integer> listener){
        addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!mCartData.containsKey(productId)) {
                        mCartData.put(productId, 1);
                    } else {
                        int count = mCartData.get(productId);
                        mCartData.put(productId, count + 1);
                    }
                    passResponseToListener(mCartData.size(), null, listener);
                }
                catch (Exception e){
                    passResponseToListener(-1, e, listener);
                }
            }
        });
    }
    //
    @Override
    public void beginRequestCartItems(final OnResultListener<List<ShowcaseItemDataModel>> listener){
        addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    List<ShowcaseItemDataModel> result = new ArrayList<>();
                    for (Map.Entry<String, Integer> item: mCartData.entrySet()) {
                        ShowcaseItemDataModel showcaseItem = getShowcaseItemDataModelById(item.getKey());
                        if(showcaseItem != null){
                            showcaseItem.itemsInCart = item.getValue();
                            result.add(showcaseItem);
                        }
                    }
                    passResponseToListener(result, null, listener);
                }
                catch (Exception e){
                    passResponseToListener(null, e, listener);
                }
            }
        });
    }
    //
    @Override
    public void beginRemoveItemFromCart(final String id, final OnResultListener<Integer> listener){
        addTask(new Runnable() {
            @Override
            public void run() {
                try{
                    mCartData.remove(id);
                    passResponseToListener(mCartData.size(), null, listener);
                }
                catch (Exception e){
                    passResponseToListener(-1, e, listener);
                }
            }
        });
    }
    //
    @Override
    public void beginClearCart(final OnResultListener<Void> listener){
        addTask(new Runnable() {
            @Override
            public void run() {
                try{
                    mCartData.clear();
                    passResponseToListener(null, null, listener);
                }
                catch (Exception e){
                    passResponseToListener(null, e, listener);
                }
            }
        });
    }

    @Override
    public void beginSubmitOrdering(final OrderingDataModel model, final OnResultListener<OrderingCompletedModel> listener){
        addTask(new Runnable() {
            @Override
            public void run() {
                try{
                    mCurrentOrderingData = model;
                    OrderingCompletedModel result = new OrderingCompletedModel();
                    result.orderId = UUID.randomUUID().toString();
                    passResponseToListener(result, null, listener);
                }
                catch (Exception e){
                    passResponseToListener(null, e, listener);
                }
            }
        });
    }

    @Override
    public void beginRequestLastOrdering(final OnResultListener<OrderingDataModel> listener){
        addTask(new Runnable() {
            @Override
            public void run() {
                try{
                    OrderingDataModel result = mCurrentOrderingData == null ? new OrderingDataModel() : mCurrentOrderingData;
                    passResponseToListener(result, null, listener);
                }
                catch (Exception e){
                    passResponseToListener(null, e, listener);
                }
            }
        });
    }


}
