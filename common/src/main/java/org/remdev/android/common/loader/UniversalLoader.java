package org.remdev.android.common.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides the simple implementation of {@link Loader}
 *
 * <br>
 *     Allows to extend loader to perform simple loading of some data in a separate thread
 * <br>
 *     <b>Important!</b>
 *     <br>
 *         This implementation provides two methods for triggering loading
 *         <ul>
 *             <li>{@link UniversalLoader#executeLoad(Bundle)}</li>
 *             <li>{@link UniversalLoader#executeLoad(Bundle, boolean)}</li>
 *         </ul>
 *     <br>
 *         The second one allows to use already loaded result by calling
 *         <p>
 *         {@code
 *             compatActivity.getSupportLoaderManager().initLoader(id, args, this);
 *         }
 *         </p>
 *             if the passed boolean parameter is set to false.
 *         <br>
 *             The first {@link UniversalLoader#executeLoad(Bundle)} just calls
 *             {@link UniversalLoader#executeLoad(Bundle, boolean)} with flag true, which causes
 *             <p>
 *             {@code
 *                  compatActivity.getSupportLoaderManager().restartLoader(id, args, this);
 *             }
 *             </p>
 * @param <T> type of data to be loaded
 */
public abstract class UniversalLoader<T> implements LoaderManager.LoaderCallbacks<T> {

    private static final Log log = LogFactory.create(UniversalLoader.class);

    private static AtomicInteger sequentialId = new AtomicInteger(10000);

    private final FragmentActivity compatActivity;

    private Throwable lastError;

    private final int id;
    private final @NonNull LoadingCallback<T> callback;

    public <R extends FragmentActivity & LoadingCallback<T>> UniversalLoader(R compatActivityWithCallback, int id) {
        this(compatActivityWithCallback, id, compatActivityWithCallback);
    }

    public UniversalLoader(FragmentActivity compatActivity, int id, @NonNull LoadingCallback<T> callback) {
        this.compatActivity = compatActivity;
        this.id = id;
        this.callback = callback;
    }

    protected abstract T doLoad(Bundle data);

    protected void onLoadFinished(T result) {
        callback.onLoadFinished(this, result);
    }

    protected void onLoadCancelled() {
        callback.onCancelled(this);
    }

    protected void onLoadingFailed(Throwable error) {
        callback.onLoadingFailed(this, error);
    }

    protected Context getContext() {
        return compatActivity;
    }

    /**
     * Restarts wrapped loader and reloads data ignoring already loaded result
     * @param args the args for loading
     */
    protected void executeLoad(Bundle args) {
        executeLoad(args, true);
    }

    protected void executeLoad(Bundle args, boolean reload) {
        if (reload) {
            compatActivity.getSupportLoaderManager().restartLoader(id, args, this);
        } else {
            compatActivity.getSupportLoaderManager().initLoader(id, args, this);
        }
    }

    public void cancel() {
        compatActivity.getSupportLoaderManager().destroyLoader(id);
    }

    @Override
    public void onLoadFinished(Loader<T> loader, T data) {
        if (lastError != null) {
            onLoadingFailed(lastError);
            lastError = null;
        } else {
            onLoadFinished(data);
        }
    }

    @Override
    public Loader<T> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<T>(compatActivity) {

            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public T loadInBackground() {
                try {
                    return doLoad(args);
                } catch (Throwable e) {
                    log.e(e, "Error while loading data: %s", e.getMessage());
                    lastError = e;
                    return null;
                }
            }

            @Override
            public void onCanceled(T data) {
                super.onCanceled(data);
                onLoadCancelled();
            }
        };
    }

    @Override
    public void onLoaderReset(Loader<T> loader) { }

    protected static int generateId() {
        return sequentialId.incrementAndGet();
    }

    public interface LoadingCallback<T> {
        void onLoadFinished(UniversalLoader<T> loader, T data);
        void onLoadingFailed(UniversalLoader<T> loader, Throwable error);
        void onCancelled(UniversalLoader<T> loader);
    }

    public static class LoadingCallbackAdapter<R> implements LoadingCallback<R> {
        @Override
        public void onLoadFinished(UniversalLoader<R> loader, R data) {
            log.i("On load finished: %d", loader.id);
        }

        @Override
        public void onLoadingFailed(UniversalLoader<R> loader, Throwable error) {
            log.i(error, "On load failed: %d", loader);
        }

        @Override
        public void onCancelled(UniversalLoader<R> loader) {
            log.i("Loading cancelled: %d", loader.id);
        }
    }
}
