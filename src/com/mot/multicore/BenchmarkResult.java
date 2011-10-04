package com.mot.multicore;

import android.util.Log;

public class BenchmarkResult {

	private static final String TAG = BenchmarkResult.class.getName();
	final String what;
	final String result;

	public BenchmarkResult(String what, String result) {
		super();
		
		Log.i(TAG, "BechmarkResult(" + what + "," + result + ")");
		this.what = what;
		this.result = result;
	}

}
