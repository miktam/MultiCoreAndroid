package com.mot.multicore;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.mot.multicore.tools.FileReader;

/**
 * Show /proc/cpuinfo file
 * Columns: BogoMIPS: http://en.wikipedia.org/wiki/BogoMips
 * 
 *
 */
public class ProcCpuinfoReader extends ListActivity {

	private final String systemBinCat = "/proc/cpuinfo";
	private final String TAG = ProcCpuinfoReader.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, FileReader.transformFile(systemBinCat, 10).toArray()));
	}
}
