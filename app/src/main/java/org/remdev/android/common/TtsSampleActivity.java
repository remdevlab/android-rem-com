package org.remdev.android.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.remdev.android.common.ui.base.activity.BaseActivity;
import org.remdev.android.common.utils.TTSController;


public class TtsSampleActivity extends BaseActivity {

    private EditText textControl;

    @Override
    protected int getResId() {
        return R.layout.activity_tts_sample;
    }

    @Override
    protected void onStart() {
        super.onStart();
        TTSController.getInstance(new TTSController.ControllerStateListener() {
            @Override
            public void onInitialized(int status) {
                if (status == TTSController.STATE_ERROR) {
                    Toast.makeText(TtsSampleActivity.this, "Could not initialize TTS :(", Toast.LENGTH_SHORT).show();
                    return;
                }
                TTSController.getInstance().sayNow(getString(R.string.greeting));
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textControl = findView(this, R.id.et_text_to_say);
        Button sayNowBtn = findView(this, R.id.btn_say_now);
        sayNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSController.getInstance().sayNow(textControl.getText().toString());
            }
        });

        Button sayLaterBtn = findView(this, R.id.btn_say_later);
        sayLaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSController.getInstance().sayLater(textControl.getText().toString());
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T extends View> T findView(Activity activity, int id) {
        return ((T) activity.findViewById(id));
    }
}
