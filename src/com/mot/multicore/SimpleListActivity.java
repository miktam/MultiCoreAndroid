package com.mot.multicore;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.mot.multicore.R;
import com.mot.multicore.expenses.OperationEntry;
import com.mot.multicore.expenses.OperationEntryAdapter;
import com.mot.multicore.expenses.OperationType;
import com.mot.multicore.expenses.threads.DataDownloader;

public class SimpleListActivity extends ListActivity {

	private ProgressDialog progressDialog;
	private List<OperationEntry> operations = null;
	private OperationEntryAdapter opEntryAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		operations = new ArrayList<OperationEntry>();
		this.opEntryAdapter = new OperationEntryAdapter(this,
				R.layout.detailed_view, operations, this);
		setListAdapter(this.opEntryAdapter);

		progressDialog = ProgressDialog.show(SimpleListActivity.this, "",
				this.getString(R.string.converting), true);

		Thread thread = new Thread(null, new DataDownloader(operations,
				OperationType.ALL, this, opEntryAdapter, progressDialog),
				"parser");
		thread.start();
	}
}