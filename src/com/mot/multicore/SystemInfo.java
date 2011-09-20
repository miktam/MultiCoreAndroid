package com.mot.multicore;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.mot.multicore.tools.FileReader;

public class SystemInfo extends ListActivity {

	private final String TAG = SystemInfo.class.getName();
	private final String curFreqPath = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
	private final String minFreqPath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";
	private final String maxFreqPath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getSystemInfo()));
	}

	private List<String> getSystemInfo() {
		List<String> list = new ArrayList<String>();

		// read frequency:
		list.add("current speed: " + FileReader.readFileWithCat(curFreqPath));
		list.add("min speed: " + FileReader.readFileWithCat(minFreqPath));
		list.add("max speed: " + FileReader.readFileWithCat(maxFreqPath));

		Log.v(TAG, list.toString());

		return list;
	}
}
