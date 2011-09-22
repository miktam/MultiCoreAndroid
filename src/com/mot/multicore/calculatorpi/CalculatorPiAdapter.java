package com.mot.multicore.calculatorpi;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mot.multicore.R;

public class CalculatorPiAdapter extends ArrayAdapter<CalculatorPiEntry> {

	private List<CalculatorPiEntry> piEntry;

	public CalculatorPiAdapter(Context context, int textViewResourceId,
			List<CalculatorPiEntry> items) {
		super(context, textViewResourceId, items);
		this.piEntry = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View currentView = convertView;
		if (null == currentView) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			currentView = vi.inflate(R.layout.system_info_row, null);
		}
		CalculatorPiEntry pi = piEntry.get(position);
		if (null != pi) {
			TextView titleView = (TextView) currentView.findViewById(R.id.title);
			TextView detailView = (TextView) currentView.findViewById(R.id.detail);
			TextView helpView = (TextView) currentView.findViewById(R.id.help);
			if (null != titleView) {
				titleView.setText(String.valueOf(pi.timeOfCalculation) + "ms");
			}
			if (null != detailView) {
				detailView.setText("threads used:" + pi.threadsUsed);

			if (null != helpView) {
				helpView.setText(pi.calculatedPiString);
			}
				return currentView;
			}
		}
		
		return currentView;
	}
}
 