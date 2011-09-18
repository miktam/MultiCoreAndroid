package com.mot.multicore.expenses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import android.util.Log;


public class MonthBillData {

	private static final String TAG = MonthBillData.class.getName();
	String month;
	int year;
	Double saldoPoczatkowe;
	Double saldoKoncowe;

	List<OperationEntry> opsList = null;

	Map<String, List<OperationEntry>> similarOperations = new HashMap<String, List<OperationEntry>>();

	List<OperationEntry> opsListInPlus = null;
	List<OperationEntry> opsListInMinus = null;
	List<OperationEntry> opsListDetailed = null;

	private static MonthBillData INSTANCE = null;

	public List<OperationEntry> getOperations() {
		return opsList;
	}

	public void setYear(String year) {
		this.year = Integer.parseInt(year);
	}

	/**
	 * @param entry
	 *            - opEntry to add and group by main title
	 */
	private void addSimilarOperation(OperationEntry entry) {
		String mainTitle = entry.getMainTitle();
		Log.v(TAG, "op to add: " + mainTitle);

		if (similarOperations.containsKey(mainTitle)) {
			Log.v(TAG, mainTitle + " already exist");
			similarOperations.get(mainTitle).add(entry);
		} else {
			Log.v(TAG, mainTitle + " is first time here, put to new list");
			Log.v(TAG, similarOperations.keySet().toString());
			List<OperationEntry> lToAdd = new ArrayList<OperationEntry>();
			lToAdd.add(entry);
			similarOperations.put(mainTitle, lToAdd);
		}
	}

	/**
	 * @param opEntry
	 *            to add
	 * 
	 *            Additionally - group all entries sorting by similar title
	 */
	public void addOperationEntry(OperationEntry op) {
		
		if (null == opsList)
			opsList = new ArrayList<OperationEntry>();
		
		opsList.add(op);
		Log.v(TAG, "add " + op);
		addSimilarOperation(op);
	}

	public void setMonth(String month) {

		this.month = month;
	}

	public void setSaldoPoczatkowe(String saldoPoczatkowe) {

		this.saldoPoczatkowe = Converter.toDouble(saldoPoczatkowe);

	}

	public void setSaldoKoncowe(String saldoKoncowe) {

		String[] st = saldoKoncowe.split(" ");
		this.saldoKoncowe = Converter.toDouble(st[st.length - 1]);
	}

	private MonthBillData() {
	}

	public static MonthBillData getInstance() {
		if (null == INSTANCE)
			INSTANCE = new MonthBillData();

		return INSTANCE;
	}

	public void parseSource(String source) {

		Log.v(TAG, "start parsing file");
		Log.v(TAG, source);

		Scanner s = new Scanner(source);
		parseMonthYear(s);

		setSaldoPoczatkowe(parseSaldoPoczatkowe(s));

		parseFirstOpDate(s);

		parseEachOperation(s);

		// currently remove this op
		// normalizeOperationDates(firstDate);

	}

	private String parseSaldoPoczatkowe(Scanner s) {
		String saldoPoczatkowe = null;
		while (s.hasNextLine()) {
			String line = s.nextLine();

			if (line.startsWith("Saldo")) {
				String[] lineSaldo = line.split(" ");
				saldoPoczatkowe = lineSaldo[lineSaldo.length - 1];
				break;
			}
		}
		return saldoPoczatkowe;
	}

	private void parseMonthYear(Scanner s) {
		while (s.hasNextLine()) {
			String line = s.nextLine();

			if (line.startsWith("Elektroniczne zestawienie operacji za")) {
				String[] arr = line.split(" ");
				setYear(arr[arr.length - 1]);
				setMonth(arr[arr.length - 2]);
				break;
			}
		}
	}

	/**
	 * move dates to one ahead - as parsing doing not so great
	 * 
	 * @param firstDate
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void normalizeOperationDates(Date firstDate) {

		Log.v(TAG, "\nbefore normalizing");
		for (OperationEntry op : opsList)
			Log.v(TAG, op.toString());

		Object[] split = opsList.toArray();

		for (int i = split.length - 1; i > 0; i--) {
			((OperationEntry) split[i])
					.setDataOperacji(((OperationEntry) split[i - 1])
							.getDataOperacji());
		}

		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(firstDate);
			((OperationEntry) split[0]).setDataOperacji(cal);
		} catch (Exception e) {
			Log.w(TAG, e.getMessage());
		}

		Log.v(TAG, "\nafter normalizing");
		for (OperationEntry op : opsList)
			Log.v(TAG, op.toString());

	}

	/**
	 * get saldo koncowe
	 * 
	 * @param s
	 * @return
	 */
	@SuppressWarnings("unused")
	private String parseSaldoKoncowe(Scanner s) {
		String saldoKoncowe = null;
		while (s.hasNextLine()) {
			String line = s.nextLine();

			if (line.startsWith("Saldo ko")) {
				String[] lineSaldo = line.split(" ");
				saldoKoncowe = lineSaldo[lineSaldo.length - 1];
				break;
			}

		}
		return saldoKoncowe;
	}

	private Date parseFirstOpDate(Scanner s) {
		Date d = null;
		while (s.hasNextLine()) {
			String line = s.nextLine();

			if (line.matches(".*Saldo po operacji.*")) {
				String[] split = line.split(" ");
				d = Converter.toDate(split[split.length - 1]);
				break;
			}
		}

		return d;
	}

	/**
	 * parsing each operation
	 * 
	 * @param s
	 */
	private void parseEachOperation(Scanner s) {
		String line = new String();
		StringBuffer mainTitle = new StringBuffer();
		while (s.hasNextLine()) {
			if (line.equals(""))
				line = s.nextLine();

			if (line.matches("[0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9][0-9][0-9] .*")) {

				mainTitle = new StringBuffer(line);
				line = s.nextLine();
				StringBuffer whole = new StringBuffer();
				// replace with do while
				while (s.hasNextLine()
						&& !line.matches("[0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9][0-9][0-9] .*")
						&& !line.startsWith("Saldo ko")) {

					whole.append(line);
					line = s.nextLine();
				}

				if (line.startsWith("Saldo ko")) {
					// remove saldo koncowe - last part
					String beforeRemoveLastPart = whole.toString();
					String[] arr = beforeRemoveLastPart.split(" ");
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < arr.length - 1; i++) {
						sb.append(arr[i] + " ");
					}

					// add date to the end of the string
					sb.append(" 01-01-2011");
					this.addOperationEntry(new OperationEntry(sb.toString(),
							mainTitle.toString()));
					setSaldoKoncowe(line);
					break;
				}

				OperationEntry operEntry = new OperationEntry(whole.toString(),
						mainTitle.toString());
				this.addOperationEntry(operEntry);
			}
		}
	}

	public List<OperationEntry> getOperationsInPlus() {

		if (this.opsListInPlus == null) {
			opsListInPlus = new ArrayList<OperationEntry>();
			for (OperationEntry op : opsList) {
				if (op.getKwotaOperacji() > 0)
					opsListInPlus.add(op);
			}
		}

		return opsListInPlus;
	}

	public List<OperationEntry> getOperationsInMinus() {

		if (this.opsListInMinus == null) {
			opsListInMinus = new ArrayList<OperationEntry>();
			for (OperationEntry op : opsList) {

				if (op.getKwotaOperacji() < 0)
					opsListInMinus.add(op);
			}
		}

		return opsListInMinus;
	}

	public List<OperationEntry> getOperationsByType(OperationType opType) {
		List<OperationEntry> toReturn = null;
		switch (opType) {
		case ALL:
			toReturn = opsList;
			break;
		case MINUS:
			toReturn = getOperationsInMinus();
			break;
		case PLUS:
			toReturn = getOperationsInPlus();
			break;
		case DETAILED:
			toReturn = getOperationsDetailed();
			break;
		}
		return toReturn;
	}

	private List<OperationEntry> getOperationsDetailed() {

		// have to make one dimentional list instead of matrix
		if (null == opsListDetailed) {			
			opsListDetailed = new ArrayList<OperationEntry>();
			for (Map.Entry<String, List<OperationEntry>> entry : similarOperations
					.entrySet()) {
				String key = entry.getKey();
				Log.v(TAG, key);
				OperationEntry summaryOp = new OperationEntry(key);
				
				List<OperationEntry> values = entry.getValue();
				Log.v(TAG, values.toString());
				
				// collect saldo
				Double saldo = 0.0;
				for (OperationEntry opEntry:values)
				{
					saldo += opEntry.getKwotaOperacji();
				}				
				summaryOp.setKwotaOperacji(saldo);
				opsListDetailed.add(summaryOp);
				opsListDetailed.addAll(values);
			}
		}

		return opsListDetailed;
	}
}
