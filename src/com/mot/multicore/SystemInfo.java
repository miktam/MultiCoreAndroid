package com.mot.multicore;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mot.multicore.tools.FileReader;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class SystemInfo extends ListActivity {

	private final String TAG = SystemInfo.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, getSystemInfo()));
	}

	private List<String> getSystemInfo() {
		List<String> list = new ArrayList<String>();

		// read frequency:
		list.add("current speed: "
				+ FileReader
						.readFileWithCat("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"));
		list.add("min speed: "
				+ FileReader
						.readFileWithCat("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"));
		list.add("max speed: "
				+ FileReader
						.readFileWithCat("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"));

		Log.v(TAG, list.toString());

		return list;
	}
}
