package com.mot.multicore.expenses;

import java.math.BigDecimal;
import java.util.Calendar;

import android.util.Log;


public class OperationEntry {

	private static final String TAG = "OperationEntry";
	private Calendar dataKsiegowania;
	private Calendar dataOperacji;
	private String opisOperacji;
	private Double saldoPoOperacji;
	private Double kwotaOperacji;
	private String mainTitle;
	private String tag;
	private boolean isCategory;
	public final boolean isFaked;

	@Override
	public String toString() {
		return (getDataKsiegowania() == null ? "TYP:"
				: getDataKsiegowaniaFormatted() + "|")
				+ getMainTitle()
				+ "|"
				+ (getDataKsiegowania() == null ? "SUMA: ":"") + getKwotaOperacji();
	}

	public OperationEntry(String mainTitle) {
		isFaked = true;
		this.mainTitle = mainTitle;
	}

	public OperationEntry(String fullOpis, String mainTitle) {

		Log.v(TAG, "create:" + mainTitle + "|" + fullOpis);

		isFaked = false;

		String[] all = mainTitle.split(" ");
		StringBuffer clean = new StringBuffer();
		for (int i = 1; i < all.length; i++)
			clean.append(all[i] + " ");

		this.mainTitle = clean.toString();

		// split main title
		String[] mainArray = mainTitle.split(" ");
		String dataKsiegowania = "";
		if (mainArray.length > 1)
			dataKsiegowania = mainArray[0];

		String[] ops = fullOpis.split(" ");

		if (ops.length > 2) {

			String saldoPoOp = ops[ops.length - 2];
			String kwota = ops[ops.length - 3];
			String dataOperacji = ops[ops.length - 1];

			StringBuffer tagBuf = new StringBuffer(ops[0]);
			tagBuf.append(" ");
			tagBuf.append(ops[1]);
			tagBuf.append(" ");
			tagBuf.append(ops[2]);
			this.tag = tagBuf.toString();

			this.kwotaOperacji = Converter.toDouble(kwota);
			this.saldoPoOperacji = Converter.toDouble(saldoPoOp);
			this.dataKsiegowania = Converter.toCalendar(dataKsiegowania);
			this.dataOperacji = Converter.toCalendar(dataOperacji);

			StringBuilder opis = new StringBuilder();
			for (int i = 0; i < ops.length - 3; i++) {

				opis.append(ops[i] + " ");
			}

			this.opisOperacji = opis.toString();

			Log.v(TAG, "created entry: " + this.toString());
		}

	}

	public String getTag() {
		return tag;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public Calendar getDataOperacji() {
		return dataOperacji;
	}

	public String getDataOperacjiFormatted() {
		StringBuilder date = new StringBuilder();
		date.append(getDataOperacji().get(Calendar.DAY_OF_MONTH) + " "
				+ Converter.toMonth(getDataOperacji().get(Calendar.MONTH)));
		return date.toString();
	}

	public String getDataKsiegowaniaFormatted() {
		StringBuilder date = new StringBuilder();
		date.append(getDataKsiegowania().get(Calendar.DAY_OF_MONTH) + " "
				+ Converter.toMonth(getDataOperacji().get(Calendar.MONTH)));
		return date.toString();
	}

	public void setDataOperacji(Calendar dataOperacji) {
		this.dataOperacji = dataOperacji;
	}

	public Calendar getDataKsiegowania() {
		return dataKsiegowania;
	}

	public void setDataKsiegowania(Calendar dataKsiegowania) {
		this.dataKsiegowania = dataKsiegowania;
	}

	public String getOpisOperacji() {
		return opisOperacji.toLowerCase();
	}

	public void setOpisOperacji(String opisOperacji) {
		this.opisOperacji = opisOperacji;
	}

	public Double getSaldoPoOperacji() {
		return round(saldoPoOperacji);
	}

	public void setSaldoPoOperacji(Double saldoPoOperacji) {
		this.saldoPoOperacji = saldoPoOperacji;
	}

	public Double getKwotaOperacji() {
		return round(kwotaOperacji);
	}

	public void setKwotaOperacji(Double kwotaOperacji) {
		this.kwotaOperacji = kwotaOperacji;
	}

	public void setCategory(boolean isCategory) {
		this.isCategory = isCategory;
	}

	public boolean isCategory() {
		return isCategory;
	}
	
	private static double round(double d) {
	    BigDecimal bd = new BigDecimal(d);
	    // two places after dot is enough
	    int decimalPlaces = 2;
	    bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
	    return bd.doubleValue();
	}


}
