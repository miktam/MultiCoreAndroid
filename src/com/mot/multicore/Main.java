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
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		tabHost.clearAllTabs();
		TabHost.TabSpec spec; // Reusable TabSpec for each tab

		try {

			Intent systemInfo = new Intent().setClass(this, ProcStatReader.class);
			spec = tabHost
					.newTabSpec("stat")
					.setIndicator("/proc/stat",
							res.getDrawable(R.drawable.btn_circle_normal))
					.setContent(systemInfo);
			tabHost.addTab(spec);
			
			Intent systemBinCatReader = new Intent().setClass(this, ProcCpuinfoReader.class);
			spec = tabHost
					.newTabSpec("cpuinfo")
					.setIndicator("/proc/cpuinfo",
							res.getDrawable(R.drawable.btn_square_overlay_normal))
					.setContent(systemBinCatReader);
			tabHost.addTab(spec);

			tabHost.setCurrentTab(0);
		} catch (Exception e) {
			Log.w(TAG, "really bad: " + e.getMessage());
			ToastMaker.getToast(this, e.getMessage());
			this.finish();
		}
	}
}