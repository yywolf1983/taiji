package top.nones.app;

import android.app.Application;
import com.reggate.lib.RegGateConfig;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RegGateConfig.init(this)
                .mainActivity(MainActivity.class)
                .build();
    }
}
