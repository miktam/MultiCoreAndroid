package com.mot.multicore.calculatorpi;

import java.math.BigDecimal;

import android.util.Log;

/**
 * Single entry for CalculatorPiAdapter
 * 
 */
public class CalculatorPiEntry {

	private static final String TAG = CalculatorPiEntry.class.getName();
	
	public final long timeOfCalculation;
	public final int threadsUsed;
	public final BigDecimal calculatedPi;
	public final String calculatedPiString;

	public CalculatorPiEntry(long timeOfCalculation, int procsUsed,
			BigDecimal calculatedPi, int trimTo) {
		super();
		this.timeOfCalculation = timeOfCalculation;
		this.threadsUsed = procsUsed;
		this.calculatedPi = calculatedPi;
		this.calculatedPiString = calculatedPi.toPlainString().substring(0, trimTo) + "... size =" + calculatedPi.precision();
	}

	public static CalculatorPiEntry calculatePi(int procs, int digits) {
		Log.v(TAG, "start calculaton with params: " + procs + " " + digits);
		CalculatorPi calc = new CalculatorPi(digits, procs);
		long startTime = System.currentTimeMillis();
		calc.calculatePi();
		long endTime = System.currentTimeMillis();
		
		long timeInMilli = (endTime - startTime);
		Log.v(TAG, "took " + timeInMilli);

		return new CalculatorPiEntry(timeInMilli, procs, calc.getPi(), 30);

	}

}
