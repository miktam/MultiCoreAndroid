package com.mot.multicore;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mot.multicore.R;
import com.mot.multicore.sysinfo.SystemInfo;
import com.mot.multicore.sysinfo.SystemInfoAdapter;

public class SystemInfoActivity extends ListActivity {

	private ProgressDialog dialog;
	private List<SystemInfo> sysInfoEntries = new ArrayList<SystemInfo>();
	private SystemInfoAdapter sysInfoAdapter;
	private Runnable sysViewerThread;
	private final String TAG = SystemInfoActivity.class.getName();
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_results:
			Log.v(TAG, "show results!");
			AppMenu.showResults(this);
			return true;
		case R.id.menu_help:
			Log.v(TAG, "show help");
			AppMenu.showHelp(this);
			return true;
		case R.id.menu_rate:
			Log.v(TAG, "rate app");
			AppMenu.rateApp(this);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_info);
		this.sysInfoAdapter = new SystemInfoAdapter(this,
				R.layout.system_info_row, sysInfoEntries);
		setListAdapter(this.sysInfoAdapter);

		sysViewerThread = new Runnable() {
			public void run() {
				getSysInfos();
			}

		};
		Thread thread = new Thread(null, sysViewerThread, "MagentoBackground");
		thread.start();
		dialog = ProgressDialog.show(SystemInfoActivity.this, "",
				this.getString(R.string.get_system_info), true);
	}

	private Runnable dataChangerNotifier = new Runnable() {
		public void run() {
			Log.v(TAG, "start data notifier");
			if (null != sysInfoEntries && sysInfoEntries.size() > 0) {
				sysInfoAdapter.notifyDataSetChanged();
				for (int i = 0; i < sysInfoEntries.size(); i++)
					sysInfoAdapter.add(sysInfoEntries.get(i));
			}
			dialog.dismiss();
			sysInfoAdapter.notifyDataSetChanged();
		}
	};

	private void getSysInfos() {
		Log.v(TAG, "add sys info");

		List<SystemInfo> cpuInfo = SystemInfo.procCpuInfoReader();

		// extract meat first
		sysInfoEntries = (SystemInfo.procSpeedReader());
		sysInfoEntries.addAll(cpuInfo.subList(0, 3));
		sysInfoEntries.add(SystemInfo.coresInfo());
		sysInfoEntries.addAll(SystemInfo.procCpuStatReader());

		runOnUiThread(dataChangerNotifier);

	}
}
