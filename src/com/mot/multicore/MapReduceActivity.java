package com.mot.multicore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
		
		AppMenu.showResults(this);

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

		// collect data
		long start = new Date().getTime();
		List<BenchmarkResult> bench = new ArrayList<BenchmarkResult>();
		List<Integer> initialProcesses = SystemInfo
				.measureUserSystemProcCount();

		// calculate
		MapReduce mr = new MapReduceImpl(THREADS_USED);
		Map map = mr.mapReduce(mr.generateData());

		// measure data
		List<Integer> endProcesses = SystemInfo.measureUserSystemProcCount();

		long timeSpent = new Date().getTime() - start;
		String info = timeSpent + " ms";
		bench.add(new BenchmarkResult(
				"time to count occurences of 16K integers", info));

		ListIterator<Integer> first = initialProcesses.listIterator();
		ListIterator<Integer> last = endProcesses.listIterator();

		List<Integer> finalResults = new ArrayList<Integer>();
		while (first.hasNext() && last.hasNext()) {
			Integer res = last.next() - first.next();
			Log.i(TAG, "counted: " + res);
			finalResults.add(res);
		}

		int step = 1;

		List<String> displayFinalResults = new ArrayList<String>();

		StringBuffer line = new StringBuffer();
		for (Integer result : finalResults) {

			if (step % 2 == 0) {
				line.append(result);
				displayFinalResults.add(line.toString());
			} else {
				line.append(result + "/");
			}

			step++;
		}

		int coreCounter = 1;
		for (String displayRes : displayFinalResults) {
			bench.add(new BenchmarkResult(
					"total user/system processes executed during benchmark on core #" + coreCounter,
					displayRes.toString()));

			coreCounter++;
		}

		AppMenu.collectBenchInfo(bench);

		// end of measure

		Map<Object, Integer> sorted = mr.sortMap(map);

		String th = "threads used: " + String.valueOf(THREADS_USED);

		for (Entry<Object, Integer> e : sorted.entrySet()) {
			mapReduceValues
					.add(new MapReduceEntry(e.getKey(), e.getValue(), th));
		}

		runOnUiThread(dataChangerNotifier);
	}
}
