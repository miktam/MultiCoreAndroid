package com.mot.multicore;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.mot.multicore.R;
import com.mot.multicore.calculatorpi.CalculatorPiAdapter;
import com.mot.multicore.calculatorpi.CalculatorPiEntry;
import com.mot.multicore.tools.ToastMaker;

public class CalculatePiActivity extends ListActivity {

	private ProgressDialog dialog;
	private List<CalculatorPiEntry> calcPiValues = new ArrayList<CalculatorPiEntry>();
	private CalculatorPiAdapter adapter;
	private Runnable calcThread;
	private final String TAG = CalculatePiActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_info);
		this.adapter = new CalculatorPiAdapter(this, R.layout.system_info_row,
				calcPiValues);
		setListAdapter(this.adapter);

		calcThread = new Runnable() {
			public void run() {
				calculateData();
			}
		};

		Thread thread = new Thread(null, calcThread, "MagentoBackground");
		thread.start();

		dialog = ProgressDialog.show(CalculatePiActivity.this, "",
				this.getString(R.string.converting), true);

	}

	private Runnable dataChangerNotifier = new Runnable() {
		public void run() {
			Log.v(TAG, "start data notifier");
			if (null != calcPiValues && calcPiValues.size() > 0) {
				adapter.notifyDataSetChanged();
				for (int i = 0; i < calcPiValues.size(); i++)
					adapter.add(calcPiValues.get(i));
			}
			dialog.dismiss();
			adapter.notifyDataSetChanged();
		}
	};

	private void calculateData() {
		List<CalculatorPiEntry> values = new ArrayList<CalculatorPiEntry>();
		int howManyDigitsForPi = 100;

		for (int threads = 1; threads <= 10; threads++) {
			CalculatorPiEntry pi = CalculatorPiEntry.calculatePi(threads,
					howManyDigitsForPi);
			values.add(pi);

		}

		calcPiValues = values;
		runOnUiThread(dataChangerNotifier);
	}

	@SuppressWarnings("unused")
	private void pickTheFastesOneAndToast(List<CalculatorPiEntry> calcPiValues2) {

		if (null != calcPiValues2 && calcPiValues2.size() > 0) {
			CalculatorPiEntry bestEntry = calcPiValues2.get(0);

			for (CalculatorPiEntry entry : calcPiValues2) {
				if (entry.timeOfCalculation < bestEntry.timeOfCalculation)
					bestEntry = entry;
			}

			ToastMaker.getToast(this, "Winner: " + bestEntry.timeOfCalculation
					+ " used " + bestEntry.threadsUsed + " threads");
		}

	}
}
