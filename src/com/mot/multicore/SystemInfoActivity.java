package com.mot.multicore;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.mot.multicore.R;
import com.mot.multicore.sysinfo.SystemInfo;
import com.mot.multicore.sysinfo.SystemInfoAdapter;

public class SystemInfoActivity extends ListActivity {

	private ProgressDialog m_ProgressDialog;
	private List<SystemInfo> sysInfoEntries = new ArrayList<SystemInfo>();
	private SystemInfoAdapter sysInfoAdapter;
	private Runnable sysViewerThread;
	private final String TAG = SystemInfoActivity.class.getName();

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
		m_ProgressDialog = ProgressDialog.show(SystemInfoActivity.this,
				"Please wait...", "Retrieving data ...", true);
	}

	private Runnable dataChangerNotifier = new Runnable() {
		public void run() {
			Log.v(TAG, "start data notifier");
			if (null != sysInfoEntries && sysInfoEntries.size() > 0) {
				sysInfoAdapter.notifyDataSetChanged();
				for (int i = 0; i < sysInfoEntries.size(); i++)
					sysInfoAdapter.add(sysInfoEntries.get(i));
			}
			m_ProgressDialog.dismiss();
			sysInfoAdapter.notifyDataSetChanged();
		}
	};

	private void getSysInfos() {
		Log.v(TAG, "add sys info");

		List<SystemInfo> cpuInfo = SystemInfo.procCpuInfoReader();

		// extract meat first
		sysInfoEntries = cpuInfo.subList(0, 3);
		sysInfoEntries.addAll(SystemInfo.procCpuStatReader());
		sysInfoEntries.addAll(SystemInfo.procSpeedReader());

		Log.v(TAG, "sys info added");

		runOnUiThread(dataChangerNotifier);

	}
}
