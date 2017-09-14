package org.remdev.android.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.remdev.android.common.loader.RandomTextLoader;
import org.remdev.android.common.loader.UniversalLoader;
import org.remdev.android.common.ui.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class LoaderSampleActivity extends BaseActivity implements UniversalLoader.LoadingCallback<String> {

    @BindView(R.id.tv_words)
    protected TextView wordsView;

    @BindView(R.id.et_words_limit)
    protected EditText wordsLimitView;

    private RandomTextLoader loader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loader = new RandomTextLoader(this);

        wordsView = (TextView) findViewById(R.id.tv_words);
        wordsLimitView = (EditText) findViewById(R.id.et_words_limit);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String limit = wordsLimitView.getText().toString();
        if (limit.isEmpty() == false) {
            loader.loadText(Integer.parseInt(limit));
        }
    }

    @Override
    protected int getResId() {
        return R.layout.activity_loader_sample;
    }

    @OnClick(R.id.btn_generate)
    protected void onGenerate() {
        String limit = wordsLimitView.getText().toString();
        if (limit.isEmpty()) {
            wordsLimitView.setError("Enter words limit");
            return;
        }
        showProgressDialog("Loading...");
        loader.reloadText(Integer.parseInt(limit));
    }

    @OnTextChanged(R.id.et_words_limit)
    protected void onTextChanged() {
        wordsLimitView.setError(null);
    }

    @Override
    public void onLoadFinished(UniversalLoader<String> loader, String data) {
        hideProgress();
        wordsView.setText(data);
    }

    @Override
    public void onLoadingFailed(UniversalLoader<String> loader, Throwable error) {
        hideProgress();
        Toast.makeText(this, "Loading failed :(\n" + error.getMessage() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled(UniversalLoader<String> loader) {
        hideProgress();
    }
}
