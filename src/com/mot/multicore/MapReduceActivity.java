package com.mot.multicore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.mot.multicore.mapreduce.MapReduce;
import com.mot.multicore.mapreduce.MapReduceAdapter;
import com.mot.multicore.mapreduce.MapReduceEntry;
import com.mot.multicore.mapreduce.MapReduceImpl;
import com.mot.multicore.sysinfo.SystemInfo;

public class MapReduceActivity extends ListActivity {

	private ProgressDialog dialog;
	private List<MapReduceEntry> mapReduceValues = new ArrayList<MapReduceEntry>();
	private MapReduceAdapter adapter;
	private Runnable calcThread;
	private final String TAG = MapReduceActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_info);
		this.adapter = new MapReduceAdapter(this, R.layout.system_info_row,
				mapReduceValues);
		setListAdapter(this.adapter);

		calcThread = new Runnable() {
			public void run() {
				calculateData();
			}
		};

		Thread thread = new Thread(null, calcThread, "MagentoBackground");
		thread.start();

		dialog = ProgressDialog.show(MapReduceActivity.this, "",
				this.getString(R.string.converting), true);

	}

	private Runnable dataChangerNotifier = new Runnable() {
		public void run() {
			
			if (null != mapReduceValues && mapReduceValues.size() > 0) {
				adapter.notifyDataSetChanged();
				for (int i = 0; i < mapReduceValues.size(); i++)
					adapter.add(mapReduceValues.get(i));
			}	

			dialog.dismiss();
			adapter.notifyDataSetChanged();
		}
	};

	private void calculateData() {

		Integer THREADS_USED = 8;
		mapReduceValues = new ArrayList<MapReduceEntry>();
		
		
		long start = new Date().getTime();
		MapReduce mr = new MapReduceImpl(THREADS_USED);
		Map map = mr.mapReduce(mr.generateData());
		long timeSpent = new Date().getTime() - start;
		String info = timeSpent + " ms";
		mapReduceValues.add(new MapReduceEntry(
				"time to count occurences of 16K integers", "", info));
		Map<Object, Integer> sorted = mr.sortMap(map);

		String th = "threads used: " + String.valueOf(THREADS_USED);

		for (Entry<Object, Integer> e : sorted.entrySet()) {
			mapReduceValues
					.add(new MapReduceEntry(e.getKey(), e.getValue(), th));
		}

		runOnUiThread(dataChangerNotifier);
	}
}
