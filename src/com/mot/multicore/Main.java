package com.mot.multicore;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.mot.multicore.R;
import com.mot.multicore.tools.ToastMaker;

public class Main extends TabActivity {
	private static final String TAG = Main.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
		init();
	}

	private void init() {
		Resources res = getResources();
		TabHost tabHost = getTabHost();
		tabHost.clearAllTabs();
		TabHost.TabSpec spec;

		try {

			Intent sysInfoActivity = new Intent().setClass(this,
					SystemInfoActivity.class);
			spec = tabHost
					.newTabSpec("sysInfo")
					.setIndicator("Processor information",
							res.getDrawable(R.drawable.btn_circle_disable))
					.setContent(sysInfoActivity);
			tabHost.addTab(spec);		

			Intent piCalculator = new Intent().setClass(this,
					CalculatePiActivity.class);
			spec = tabHost
					.newTabSpec("pi")
					.setIndicator("\u03C0 calculator",
							res.getDrawable(R.drawable.btn_square_overlay_disabled))
					.setContent(piCalculator);
			tabHost.addTab(spec);			
			
			Intent guiManipulator = new Intent().setClass(this,
					GuiManipulatorActivity.class);
			spec = tabHost
					.newTabSpec("master")
					.setIndicator("GUI elements adder",
							res.getDrawable(R.drawable.btn_circle_normal))
					.setContent(guiManipulator);
			tabHost.addTab(spec);

			tabHost.setCurrentTab(0);
		} catch (Exception e) {
			Log.w(TAG, "really bad: " + e.getMessage());
			ToastMaker.getToast(this, e.getMessage());
			this.finish();
		}
	}
}