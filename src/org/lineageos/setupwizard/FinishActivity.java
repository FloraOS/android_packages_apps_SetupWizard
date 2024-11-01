/*
 * SPDX-FileCopyrightText: 2016 The CyanogenMod Project
 * SPDX-FileCopyrightText: 2017-2024 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.setupwizard;


import static org.lineageos.setupwizard.SetupWizardApp.LOGV;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Toast;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;

import com.google.android.setupcompat.util.SystemBarHelper;
import org.lineageos.setupwizard.util.SetupWizardUtils;


public class FinishActivity extends BaseSetupWizardActivity {
    public static final String TAG = FinishActivity.class.getSimpleName();

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    // "Why not just start this activity with an Intent extra?" you might ask. Been there.
    // We need this to affect the theme, and even onCreate is not early enough for that,
    // so "static volatile boolean" it is. Feel free to rework this if you dare.
    private static volatile boolean sIsFinishing;

    private View mRootView;
    private Resources.Theme mEdgeToEdgeWallpaperBackgroundTheme;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	getGlifLayout().setDescriptionText(getString(R.string.finish_description));
	setNextText(R.string.start);

        if (sIsFinishing) {
            startFinishSequence();
        }
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.finish_layout;
    }

    @Override
    protected int getTitleResId() {
        return R.string.finish_title;
    }

    @Override
    protected int getIconResId() {
        return R.drawable.ic_celebration;
    }

    @Override
    public void onNavigateNext() {
        if (!sIsFinishing) {
            sIsFinishing = true;
            startActivity(getIntent());
            finish();
        }
        hideNextButton();
    }

    private void startFinishSequence() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        hideNextButton();
        SetupWizardUtils.finishSetupWizard(FinishActivity.this);
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        if (sIsFinishing) {
            if (mEdgeToEdgeWallpaperBackgroundTheme == null) {
                theme.applyStyle(R.style.EdgeToEdgeWallpaperBackground, true);
                mEdgeToEdgeWallpaperBackgroundTheme = theme;
            }
            return mEdgeToEdgeWallpaperBackgroundTheme;
        }
        return theme;
    }
}



