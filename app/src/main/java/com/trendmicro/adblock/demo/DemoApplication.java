package com.trendmicro.adblock.demo;

import android.app.Application;

import com.trend.lazyinject.annotation.Inject;
import com.trend.lazyinject.lib.LazyInject;
import com.trendmicro.adblock.AdBlockerUtils;
import com.trendmicro.adblock.UrlChecker;
import com.trendmicro.adblock.UrlCheckerBuildMap;

import java.io.File;

/**
 * Created by swift_gan on 2018/5/24.
 */

public class DemoApplication extends Application {

    @Inject
    UrlChecker.AdBlocker adBlocker;
    @Inject
    UrlChecker.PrivacyBlocker privacyBlocker;

    @Override
    public void onCreate() {
        super.onCreate();
        LazyInject.addBuildMap(UrlCheckerBuildMap.class);
        initBlockList();
    }

    private void initBlockList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initUrlChecker("adlist", false);
                initUrlChecker("privacylist", true);
            }
        }).start();
    }

    private void initUrlChecker(String path, boolean isPrivacy) {
        AdBlockerUtils.copyAssetFolder(getAssets(), path, getFilesDir().getAbsolutePath() + "/" + path);
        File[] files = new File(getFilesDir().getAbsolutePath() + "/" + path).listFiles();
        String[] paths = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            paths[i] = files[i].getAbsolutePath();
        }
        if (isPrivacy) {
            privacyBlocker.init(paths);
        } else {
            adBlocker.init(paths);
        }
    }

}
