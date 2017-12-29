package com.sinashow.headline.splash;

import android.os.Handler;
import android.os.Message;

import com.sinashow.headline.R;
import com.sinashow.headline.base.BaseActivity;
import com.sinashow.headline.main.MainActivity;
import com.sinashow.headline.utils.statusBar.ImmerseStatusBar;

import java.lang.ref.WeakReference;

public class SplashActivity extends BaseActivity {

    private static final int CODE_2MAIN = 0;
    private static final int DELAY_2MAIN = 1500;

    private static class SmartHandler extends Handler {

        private WeakReference<SplashActivity> weakReference;

        public SmartHandler(SplashActivity splashActivity) {
            weakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SplashActivity splashActivity = weakReference.get();
            if (splashActivity == null) return;
            switch (msg.what) {
                case CODE_2MAIN:
                    splashActivity.invokActivity(splashActivity, MainActivity.class);
                    break;
            }
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    public void onCreate() {
        ImmerseStatusBar.setImmerseStatusBar(this, R.color.transparent);
        SmartHandler smartHandler = new SmartHandler(this);
        smartHandler.sendEmptyMessageDelayed(CODE_2MAIN, DELAY_2MAIN);
    }
}
