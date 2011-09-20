package com.mot.multicore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mot.multicore.tools.ToastMaker;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * Worker thread - just add elements to the list in thread
 */
public class ThreadMaster extends ListActivity {

	private ProgressDialog progressDialog;
	private final String TAG = ThreadMaster.class.getName();
	public static List<String> members = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.v(TAG, "current members: " + members);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, members);

		setListAdapter(adapter);

		progressDialog = ProgressDialog.show(ThreadMaster.this, "",
				this.getString(R.string.converting), true);

		Thread thread = new Thread(null, new Worker(new Date().getTime(), this,
				adapter, progressDialog));
		thread.start();
	}

}

class Worker implements Runnable {

	private List<String> members = new ArrayList<String>();
	private Activity activity;
	private ProgressDialog dialog;
	private ArrayAdapter<String> adapter;
	private final String TAG = ThreadMaster.class.getName();
	private long start;

	public Worker(long start, Activity act, ArrayAdapter<String> adapter,
			ProgressDialog progressDialog) {
		this.activity = act;
		this.dialog = progressDialog;
		this.adapter = adapter;
		this.start = start;
		Log.v(TAG, "created " + this);
	}

	@SuppressWarnings("serial")
	private List<String> getData(final int howMany) {
		return new ArrayList<String>() {
			{
				for (int i = 0; i < howMany; i++)
					add("element " + i);
			}
		};
	}

	public void run() {

		Log.v(TAG, "execute " + this);

		members = getData(1000);
		Log.v(TAG, "current members: " + members);

		activity.runOnUiThread(new Runnable() {
			public void run() {
				adapter.clear();
				if (members != null && members.size() > 0) {
					for (String member : members) {
						adapter.add(member);
					}

					// add time needed for all work
					String info = "adding " + adapter.getCount()
							+ " elems took " + (new Date().getTime() - start)
							+ " ms";
					adapter.insert(info, 0);
					
					ToastMaker.getToast(activity, info);

				}
				dialog.dismiss();
				adapter.notifyDataSetChanged();

			}
		});
	}
}
