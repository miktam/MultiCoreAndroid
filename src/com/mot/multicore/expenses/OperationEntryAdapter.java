package com.mot.multicore.expenses;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mot.multicore.R;

public class OperationEntryAdapter extends ArrayAdapter<OperationEntry> {

	protected List<OperationEntry> items;
	protected Activity activity;

	public OperationEntryAdapter(Context context, int textViewResourceId,
			List<OperationEntry> items, Activity ac) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.activity = ac;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.detailed_view, null);
		}
		OperationEntry opEntry = items.get(position);
		
		if (opEntry != null) {

			if (!opEntry.isFaked) {
				TextView saldo = (TextView) v.findViewById(R.id.icon);
				TextView mainTitle = (TextView) v.findViewById(R.id.secondLine);
				TextView descrOpOperation = (TextView) v
						.findViewById(R.id.opis);
				TextView date = (TextView) v.findViewById(R.id.date);
				mainTitle.setText(opEntry.getMainTitle());
				descrOpOperation.setText(opEntry.getTag());
				saldo.setText("" + opEntry.getKwotaOperacji());
				date.setText(opEntry.getDataKsiegowaniaFormatted());
			}

		}
		return v;
	}
}