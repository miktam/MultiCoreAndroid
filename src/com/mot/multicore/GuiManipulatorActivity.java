package com.mot.multicore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.mot.multicore.sysinfo.SystemInfo;
import com.mot.multicore.sysinfo.SystemInfoAdapter;

public class GuiManipulatorActivity extends ListActivity {

	private ProgressDialog dialog;
	private List<SystemInfo> sysInfoEntries = new ArrayList<SystemInfo>();
	private SystemInfoAdapter adapter;
	private Runnable guiThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_info);
		this.adapter = new SystemInfoAdapter(this,
				R.layout.system_info_row, sysInfoEntries);
		setListAdapter(this.adapter);

		guiThread = new Runnable() {
			public void run() {
				getData();
			}

		};
		Thread thread = new Thread(null, guiThread, "MagentoBackground");
		thread.start();
		dialog = ProgressDialog.show(GuiManipulatorActivity.this, "",
				this.getString(R.string.converting), true);		
	}

	private Runnable dataChangerNotifier = new Runnable() {
		public void run() {
			long start = new Date().getTime();
			if (null != sysInfoEntries && sysInfoEntries.size() > 0) {
				adapter.notifyDataSetChanged();
				for (int i = 0; i < sysInfoEntries.size(); i++)
					adapter.add(sysInfoEntries.get(i));
			}
			// add time needed for all work
			long timeSpent = new Date().getTime() - start;
			String info = timeSpent + " ms";
			adapter.insert(new SystemInfo("time to create 10K elements", "", info), 0);
			dialog.dismiss();
			adapter.notifyDataSetChanged();
		}
	};

	private void getData() {
		sysInfoEntries = SystemInfo.getDummyElements(10000);
		runOnUiThread(dataChangerNotifier);
	}
}
