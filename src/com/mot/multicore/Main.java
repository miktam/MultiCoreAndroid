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

	public static TabHost tabHost;

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
		tabHost = getTabHost();
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

			Intent mapReduce = new Intent().setClass(this,
					MapReduceActivity.class);
			spec = tabHost
					.newTabSpec("map reduce")
					.setIndicator(
							"Map Reduce",
							res.getDrawable(R.drawable.btn_square_overlay_disabled))
					.setContent(mapReduce);
			tabHost.addTab(spec);

			tabHost.setCurrentTab(0);
		} catch (Exception e) {
			Log.w(TAG, "really bad: " + e.getMessage());
			ToastMaker.getToast(this, e.getMessage());
			this.finish();
		}
	}
}