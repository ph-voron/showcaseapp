package app.voron.ph.showcaseapp.System.Providers;

/**
 * Created by TJack on 15.10.2017.
 */

public interface OnResultListener<Result> {
    public void onResult(Result result, Exception exception);
}
