package com.library.common.component.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class UpdateAppService extends IntentService {

    public final static String INTENT_UPDATE_SERVICE = "intentUpdateService";

    public UpdateAppService() {
        super("appUpdateService");
    }

    /**
     * 通过APK_URL升级应用
     *
     * @param context ()
     * @param fileUrl ()
     */
    public static void updateApp(Context context, String fileUrl) {
        Intent intentService = new Intent(context, UpdateAppService.class);
        intentService.putExtra("fileUrl", fileUrl);
        intentService.setAction(INTENT_UPDATE_SERVICE);
        context.startService(intentService);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
