package com.extraslice.walknpay.ui;

import android.app.Activity;
import android.content.Intent;

import jim.h.common.android.lib.zxing.Intents;

/**
 * @author Jim.H
 */
public final class CustomScanner {

    public static final int REQUEST_CODE = 0x0ba7c0de;

    private CustomScanner() {
    }


    public static void initiateScan(Activity activity, int layoutResId, int viewFinderViewResId,
                                    int previewViewResId, boolean useFrontLight,int manualButton,String sourceClass,String cameraId) {
    	CaptureActivity act = new CaptureActivity();
        Intent intent = new Intent(activity, act.getClass());
        intent.putExtra(CaptureActivity.KEY_LAYOUT_RES_ID, layoutResId);
        intent.putExtra(CaptureActivity.KEY_VIEW_FINDER_VIEW_RES_ID, viewFinderViewResId);
        intent.putExtra(CaptureActivity.KEY_PREVIEW_VIEW_RES_ID, previewViewResId);
        intent.putExtra("MANUAL", manualButton);
        intent.putExtra("SOURCECLASS", sourceClass);
        intent.putExtra("CAMERA_ID", cameraId);
        intent.putExtra(Intents.Scan.WIDTH,600);
        intent.putExtra(Intents.Scan.HEIGHT,400);
        intent.putExtra(CaptureActivity.KEY_USE_FRONT_LIGHT, useFrontLight);
        activity.startActivityForResult(intent, REQUEST_CODE);

    }

    public static IntentResult parseActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra(Intents.Scan.RESULT);
                String formatName = intent.getStringExtra(Intents.Scan.RESULT_FORMAT);
                return new IntentResult(contents, formatName);
            } else {
                return new IntentResult(null, null);
            }
        }
        return null;
    }


}
