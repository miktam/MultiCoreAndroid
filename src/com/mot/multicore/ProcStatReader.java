package com.mot.multicore;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.mot.multicore.tools.FileReader;

/**
 * Show proc/stat file
 * Details on each column: http://www.linuxhowtos.org/System/procstat.htm
 *
 */
public class ProcStatReader extends ListActivity {

	private final String procStatLocation = "/proc/stat";
	private final static String TAG = ProcStatReader.class.getName();


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, FileReader.transformFile(procStatLocation, 5).toArray()));
	}

	
}
