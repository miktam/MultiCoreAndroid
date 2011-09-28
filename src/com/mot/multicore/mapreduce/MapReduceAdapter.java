package com.mot.multicore.mapreduce;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mot.multicore.R;

public class MapReduceAdapter extends ArrayAdapter<MapReduceEntry> {

	private List<MapReduceEntry> entries;

	public MapReduceAdapter(Context context, int textViewResourceId,
			List<MapReduceEntry> items) {
		super(context, textViewResourceId, items);
		this.entries = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View currentView = convertView;
		if (null == currentView) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			currentView = vi.inflate(R.layout.system_info_row, null);
		}
		MapReduceEntry entry = entries.get(position);
		if (null != entry) {
			TextView titleView = (TextView) currentView
					.findViewById(R.id.title);
			TextView detailView = (TextView) currentView
					.findViewById(R.id.detail);

			TextView helpView = (TextView) currentView.findViewById(R.id.help);

			if (null != titleView) {
				titleView.setText(entry.value.toString());
			}
			if (null != detailView) {
				detailView.setText("times found:" + entry.occurence);
			}

			if (null != helpView) {
				helpView.setText("" + entry.threadsUsed);
			}
			return currentView;

		}

		return currentView;
	}
}