package com.mot.multicore;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppMenu {

	private final static String appName = "MultiCore Android";
	private final static String appMarketLocation = "market://details?id=com.mot.multicore";
	private static final String TAG = AppMenu.class.getName();

	private static List<BenchmarkResult> benchmarkResults;

	public static void rateApp(final Context context) {

		final Dialog dialog = new Dialog(context);
		dialog.setTitle(context.getString(R.string.rate));

		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView textView = new TextView(context);
		textView.setText(context.getString(R.string.if_you_enjoy));
		textView.setWidth(400);
		textView.setPadding(5, 0, 5, 10);

		textView.setGravity(Gravity.LEFT);
		layout.addView(textView);

		Button buttonRate = new Button(context);
		buttonRate.setText("Rate " + appName);
		buttonRate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(appMarketLocation)));
				dialog.dismiss();
			}
		});
		layout.addView(buttonRate);

		Button buttonRemindLater = new Button(context);
		buttonRemindLater.setText(context.getString(R.string.remind_later));
		buttonRemindLater.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		layout.addView(buttonRemindLater);

		dialog.setContentView(layout);
		dialog.show();
	}

	public static void showHelp(final Context context) {

		final Dialog dialog = new Dialog(context);
		dialog.setTitle(context.getString(R.string.about));

		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView textView = new TextView(context);
		textView.setText(context
				.getString(R.string.about_app_short_and_concise));
		textView.setWidth(400);
		textView.setPadding(5, 0, 5, 10);

		textView.setGravity(Gravity.LEFT);
		layout.addView(textView);

		Button dismissButton = new Button(context);
		dismissButton.setText(context.getString(R.string.dismiss));
		dismissButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		layout.addView(dismissButton);

		dialog.setContentView(layout);
		dialog.show();
	}

	public static void showResults(final Context context) {

		final Dialog dialog = new Dialog(context);
		dialog.setTitle(context.getString(R.string.results));

		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView textView = new TextView(context);
		textView.setWidth(480);
		textView.setPadding(5, 0, 5, 10);

		textView.setGravity(Gravity.LEFT);
		if (null == benchmarkResults || benchmarkResults.size() == 0) {
			Log.v(TAG, "no data collected!");

			textView.setText(context.getString(R.string.run_benchmark_first));

			layout.addView(textView);

			Button runBenchmark = new Button(context);
			runBenchmark.setText(context
					.getString(R.string.run_benchmark_button));
			runBenchmark.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {							
					Main.tabHost.setCurrentTab(1);
					dialog.dismiss();
				}
			});
			layout.addView(runBenchmark);

			Button dismissButton = new Button(context);
			dismissButton.setText(context.getString(R.string.dismiss));
			dismissButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			layout.addView(dismissButton);

			dialog.setContentView(layout);
			dialog.show();

		} else {
			Log.v(TAG, "Some data collected!");

			StringBuffer buffer = new StringBuffer();
			for (BenchmarkResult res : benchmarkResults) {
				buffer.append(new StringBuffer(res.what + " " + res.result
						+ "\n"));
			}
			textView.setText(buffer.toString());
			layout.addView(textView);

			Button dismissButton = new Button(context);
			dismissButton.setText(context.getString(R.string.dismiss));
			dismissButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			layout.addView(dismissButton);

			dialog.setContentView(layout);
			dialog.show();
		}

	}

	public static void collectBenchInfo(List<BenchmarkResult> result) {
		benchmarkResults = new ArrayList<BenchmarkResult>();
		benchmarkResults = result;
	}
}
