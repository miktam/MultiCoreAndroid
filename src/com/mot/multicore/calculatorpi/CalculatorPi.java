package com.mot.multicore.calculatorpi;

import java.math.BigDecimal;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

/**
 * Pi calculator - based on Machin's formula:
 * http://milan.milanovic.org/math/english/pi/machin.html
 * 
 */
public class CalculatorPi {

	final int roundingMode = BigDecimal.ROUND_HALF_EVEN;
	private final BigDecimal FOUR = new BigDecimal("4");
	private BigDecimal arctan1_5;
	private BigDecimal arctan1_239;
	private BigDecimal pi;
	private final int digits;
	private Thread[] threads;
	private int numProcessors;
	private CyclicBarrier cb;

	public BigDecimal getPi() {
		return pi;
	}

	public CalculatorPi(int digits, int numProcessors) {
		this.digits = digits;
		this.numProcessors = numProcessors;
	}

	public void calculatePi() {
		threads = new Thread[numProcessors];
		cb = new CyclicBarrier(numProcessors + 1, new Runnable() {
			public void run() {
				finishUp();
			}
		});

		for (int a = 0; a < numProcessors; a++) {
			threads[a] = new CalculatorPi.Threads();
			threads[a].start();
		}
		try {
			cb.await();
		} catch (InterruptedException ie) {

		} catch (BrokenBarrierException be) {

		}
	}

	/**
	 * Compute the value of pi to the specified number of digits after the
	 * decimal point. The value is computed using Machin's formula:
	 * 
	 * pi/4 = 4*arctan(1/5) - arctan(1/239)
	 * 
	 * and a power series expansion of arctan(x) to sufficient precision.
	 */
	public void computePi() {
		try {
			arctan1_5 = arctan(5, digits + 5);
			arctan1_239 = arctan(239, digits + 5);

			cb.await();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (BrokenBarrierException be) {
			be.printStackTrace();
		}

	}

	public void finishUp() {
		pi = arctan1_5.multiply(FOUR).subtract(arctan1_239).multiply(FOUR);
		pi.setScale(digits, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Compute the value, in radians, of the arctangent of the inverse of the
	 * supplied integer to the specified number of digits after the decimal
	 * point. The value is computed using the power series expansion for the arc
	 * tangent:
	 * 
	 * arctan(x) = x - (x^3)/3 + (x^5)/5 - (x^7)/7 + (x^9)/9 ...
	 */
	public BigDecimal arctan(int inverseX, int scale) {
		BigDecimal result, numer, term;
		BigDecimal invX = BigDecimal.valueOf(inverseX);
		BigDecimal invX2 = BigDecimal.valueOf(inverseX * inverseX);

		numer = BigDecimal.ONE.divide(invX, scale, roundingMode);

		result = numer;
		int i = 1;
		do {
			numer = numer.divide(invX2, scale, roundingMode);
			int denom = 2 * i + 1;
			term = numer.divide(BigDecimal.valueOf(denom), scale, roundingMode);
			if ((i % 2) != 0) {
				result = result.subtract(term);
			} else {
				result = result.add(term);
			}
			i++;
		} while (term.compareTo(BigDecimal.ZERO) != 0);
		return result;
	}

	private class Threads extends Thread {
		public void run() {
			computePi();
		}
	}

}
