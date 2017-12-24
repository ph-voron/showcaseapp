package app.voron.ph.showcaseapp.Services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;

import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.System.DependencyResolver;

public class ImagePreparingQueueService extends Service {

    public class LocalBinder extends Binder {
        public ImagePreparingQueueService getService() {
            return ImagePreparingQueueService.this;
        }
    }
    //
    private class QueueJob{
        public String imageName = null;
        public int itemPosition = -1;
        public WeakReference<ImageView> imageView = null;
        public boolean useFadeIn = false;
        //
        public QueueJob(String imageName, ImageView receiver){
            this.imageName = imageName;
            this.imageView = new WeakReference<>(receiver);
        }
        //
        public QueueJob(String imageName, ImageView receiver, boolean useFadeIn){
            this.imageName = imageName;
            this.imageView = new WeakReference<>(receiver);
            this.useFadeIn = useFadeIn;
        }
    }
    //
    private final IBinder mBinder = new LocalBinder();
    private Thread mWorkerThread = null;
    private ArrayDeque<QueueJob> mQueue = new ArrayDeque<>();
    //
    public ImagePreparingQueueService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWorkerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Thread.currentThread().isAlive()){
                    try {
                        try {
                            processQueue();
                        }
                        catch (InterruptedException e){
                            throw new InterruptedException();
                        }
                        catch (Exception e){
                            Log.e("Queue", e.getMessage());
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException e){
                        break;
                    }
                }
            }
        });
        mWorkerThread.start();
        Log.i(this.getClass().getSimpleName(), "onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWorkerThread.interrupt();
        Log.i(this.getClass().getSimpleName(), "onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    //
    private void passResponseToReceiver(final Bitmap result, final ImageView imageView, final boolean useFadeIn) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Animation anim = AnimationUtils.loadAnimation(imageView.getContext(), R.anim.fade_in_short);
                    imageView.setImageBitmap(result);
                    if (useFadeIn) {
                        imageView.startAnimation(anim);
                    }
                }
                catch (Exception e){
                    Log.e("ImagePreparing", "Unable to set bitmap to a ImageView");
                }
            }
        });
    }
    //
    private void processQueue() throws InterruptedException{
        QueueJob item = null;
        do{
            synchronized (mQueue) {
                item = mQueue.poll();
            }
            if(item != null){
                Bitmap result = DependencyResolver
                        .getDataRequestProvider()
                        .requestImage(item.imageName);
                if(Thread.interrupted()) throw new InterruptedException();
                //Thread.sleep(100);
                ImageView imageView = item.imageView.get();
                if(imageView != null) {
                    //Log.i("ImageService", "passResponseToReceiver(), queue = " + mQueue.size());
                    passResponseToReceiver(result, imageView, item.useFadeIn);
                }
            }
        }while (item != null);
    }
    //
    public void addJob(String imageName, ImageView receiver, boolean useFadeIn){
        //можно добавить проверку на дублирующиеся для приемников таски
        synchronized (mQueue) {
            for (QueueJob item: mQueue) {
                ImageView imageViewItem = item.imageView.get();
                if(imageViewItem == null){
                    //Log.i("ImageService", "Removed destroyed receiver");
                    mQueue.remove(item);
                    break;
                }
                if(imageViewItem.hashCode() == receiver.hashCode()){
                    //Log.i("ImageService", "Removed twin receiver " + imageViewItem.hashCode());
                    mQueue.remove(item);
                    break;
                }
            }
            mQueue.add(new QueueJob(imageName, receiver, useFadeIn));
        }
    }
}
