package com.mot.multicore.sysinfo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mot.multicore.R;

/**
 * Simple adapter for SystemInfo
 */
public class SystemInfoAdapter extends ArrayAdapter<SystemInfo> {

	private List<SystemInfo> sysInfoEntries;

	public SystemInfoAdapter(Context context, int textViewResourceId,
			List<SystemInfo> items) {
		super(context, textViewResourceId, items);
		this.sysInfoEntries = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View currentView = convertView;
		if (null == currentView) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			currentView = vi.inflate(R.layout.system_info_row, null);
		}
		SystemInfo sysInfo = sysInfoEntries.get(position);
		if (null != sysInfo) {
			TextView titleView = (TextView) currentView.findViewById(R.id.title);
			TextView detailView = (TextView) currentView.findViewById(R.id.detail);
			TextView helpView = (TextView) currentView.findViewById(R.id.help);
			if (null != titleView) {
				titleView.setText(sysInfo.title);
			}
			if (null != detailView) {
				detailView.setText(sysInfo.details);

			if (null != helpView) {
				helpView.setText(sysInfo.help);
			}
				return currentView;
			}
		}
		
		return currentView;
	}
}
