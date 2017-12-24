package app.voron.ph.showcaseapp.System.Tasks;

import app.voron.ph.showcaseapp.System.Exceptions.TaskWasCanceledException;

/**
 * Created by TJack on 21.10.2017.
 */

public class BasicCancelationToken implements TaskCancelationToken {
    private boolean mIsCanceled = false;
    @Override
    public void cancel() {
        mIsCanceled = true;
    }
    //
    public boolean isCanceled(){
        return mIsCanceled;
    }
    //
    public void throwIfCanceled() throws TaskWasCanceledException {
        if(mIsCanceled) throw new TaskWasCanceledException();
    }
}
