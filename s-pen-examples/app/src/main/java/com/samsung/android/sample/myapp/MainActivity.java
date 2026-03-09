package com.samsung.android.sample.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_PX;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private int mSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text_view);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String actionName = "";
        float size = mTextView.getTextSize();

        if ((event.getMetaState() & KeyEvent.META_CTRL_ON) != 0) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_A:
                    actionName = getResources().getString(R.string.shutter);
                    break;
                case KeyEvent.KEYCODE_B:
                    actionName = getResources().getString(R.string.camera_switch);
                    break;
                case KeyEvent.KEYCODE_C:
                    actionName = getResources().getString(R.string.next_filter);
                    break;
                case KeyEvent.KEYCODE_D:
                    actionName = getResources().getString(R.string.prev_filter);
                    break;
                case KeyEvent.KEYCODE_E:
                    actionName = getResources().getString(R.string.zoom_in);
                    size += 30f;
                    break;
                case KeyEvent.KEYCODE_F:
                    actionName = getResources().getString(R.string.zoom_out);
                    size -= 30f;
                    break;
            }
        }
        updateUI(actionName, size);

        return super.onKeyDown(keyCode, event);
    }

    private void updateUI(String actionName, float size) {
        mTextView.setText(actionName);

        if (size < 10f) {
            size = 10f;
        } else if (size > 200f) {
            size = 200f;
        }
        mTextView.setTextSize(COMPLEX_UNIT_PX, size);
    }
}
