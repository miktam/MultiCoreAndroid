package com.mot.multicore.mapreduce;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;

public class MapReduceImpl implements MapReduce {
	private final int BUCKETS;
	ExecutorService executor = null;
	Map<Object, Integer> finalResult;
	private final String TAG = MapReduceImpl.class.getName();

	public MapReduceImpl(int threads) {
		BUCKETS = threads;
	}

	public Map<Object, Integer> mapReduce(List<Object> data) {
		executor = Executors.newFixedThreadPool(BUCKETS);
		finalResult = new HashMap<Object, Integer>();
		List<Object[]> buckets = divideIntoBuckets(BUCKETS, data);
		for (final Object[] bucket : buckets)
			executor.execute(new MapAndReduce(bucket));
		executor.shutdown();
		while (!executor.isTerminated()) {
		}

		return finalResult;
	}

	private List<Pair<Object, Integer>> createIntermidiateMap(Object[] tokens) {
		List<Pair<Object, Integer>> map = new ArrayList<Pair<Object, Integer>>();
		for (Object token : tokens) {
			map.add(new Pair<Object, Integer>(token.toString(), 1));
		}

		return map;
	}

	class Pair<L, R> {
		public final L left;
		public final R right;
		public Pair(L left, R right) {
			this.left = left;
			this.right = right;
		}
	}

	private Map<Object, Integer> reduce(
			List<Pair<Object, Integer>> intermediateMap2) {
		Log.v(TAG, "start reduce");
		Map<Object, Integer> output = new HashMap<Object, Integer>();
		for (Pair<Object, Integer> pair : intermediateMap2) {
			if (output.containsKey(pair.left)) {
				// need to refresh value - do not use value from pair - it is
				// always has value = 1
				output.put(pair.left, output.get(pair.left) + 1);
			} else {
				output.put(pair.left, 1);
			}
		}

		Log.v(TAG, "finish reduce");
		return output;
	}

	/**
	 * divide string into buckets with provided size
	 * 
	 * @param toDivide
	 * @param bucketSize
	 * @return
	 */
	private List<Object[]> divideIntoBuckets(int bucketSize, List<Object> words) {

		Log.v(TAG, "start bucketing");

		Object[] tokens = words.toArray();
		int chunk = tokens.length / bucketSize;
		int rem = tokens.length % chunk;

		List<Object[]> list = new ArrayList<Object[]>();

		int subTokenPosition = 0;
		List<String> subTokenList = new ArrayList<String>();

		for (int i = 0; i < tokens.length; i++) {

			if (subTokenPosition < chunk) {
				subTokenList.add(tokens[i].toString());
				subTokenPosition++;

				// brilliant architecture: if bucket size == 1, add and rest in
				// peace
				if (subTokenPosition == tokens.length) {
					list.add(subTokenList.toArray());
					break;
				}
			} else {

				subTokenPosition = 0;
				list.add(subTokenList.toArray());
				subTokenList = new ArrayList<String>();
				// rewind i
				i--;
			}
		}

		if (bucketSize > 2) {
			// add rest
			String[] subTokensRest = new String[rem];
			for (int rest = 0; rest < rem; rest++) {
				subTokensRest[rest] = tokens[(tokens.length - 1) - (rest)]
						.toString();
			}

			// do not add if no elements found for rest
			if (subTokensRest.length > 0)
				list.add(subTokensRest);
		}

		Log.v(TAG, "finish bucketing, buckets: " + list.size());

		return list;
	}

	public Map<String, Integer> simpleWordCounting(List<Object> words) {

		Object[] tokens = words.toArray();
		Map<String, Integer> wordOccurence = new HashMap<String, Integer>();

		for (Object token : tokens) {
			if (wordOccurence.containsKey(token)) {
				Integer occured = (wordOccurence.get(token));
				occured++;
				wordOccurence.put(token.toString(), occured++);
			} else {
				wordOccurence.put(token.toString(), 1);
			}
		}

		return wordOccurence;
	}

	private class MapByValueComparator implements Comparator<Object> {
		private Map<Object, Integer> map;

		public MapByValueComparator(Map<Object, Integer> map) {
			this.map = map;
		}

		public int compare(Object key1, Object key2) {
			int value1 = map.get(key1);
			int value2 = map.get(key2);

			int diff = value2 - value1;
			if (diff == 0)
				return key1.hashCode() - key2.hashCode();
			else
				return diff;
		}
	}

	public Map<Object, Integer> sortMap(Map<Object, Integer> mapToSort) {
		// sort descending by values
		Map<Object, Integer> sortedMap = new TreeMap<Object, Integer>(
				new MapByValueComparator(mapToSort));
		sortedMap.putAll(mapToSort);
		return sortedMap;
	}

	public List<Object> readFile(String pathToFile) {

		List<Object> words = new ArrayList<Object>();
		try {
			Log.i(TAG, "read file:" + pathToFile);
			File file = new File(pathToFile);

			Scanner scan = new Scanner(file);
			while (scan.hasNext()) {

				String word = scan.next();
				word = sanitizeString(word);

				words.add(word);
			}

			Log.v(TAG, "file succesfully read");

		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		return words;
	}

	private String sanitizeString(String input) {
		StringBuffer buf = new StringBuffer();

		buf = new StringBuffer(input.replace(".", ""));
		buf = new StringBuffer(buf.toString().replace(",", ""));
		buf = new StringBuffer(buf.toString().replace(":", ""));
		buf = new StringBuffer(buf.toString().replace(";", ""));
		buf = new StringBuffer(buf.toString().replace("!", ""));
		buf = new StringBuffer(buf.toString().replace("?", ""));

		return buf.toString().toLowerCase();
	}

	public void displayMap(Map<Object, Integer> map, int values) {

		Log.i(TAG, "---\n display map of size: " + map.size());
		for (Map.Entry<Object, Integer> pair : map.entrySet()) {
			if (values-- > 0)
				Log.i(TAG, pair.getKey() + " -> " + pair.getValue());
		}
	}

	class MapAndReduce implements Runnable {

		private Object[] bucket;

		MapAndReduce(Object[] bucket) {
			this.bucket = bucket;

		}

		public void run() {

			Log.i(TAG, "A + start map of bucket " + bucket[0]);
			List<Pair<Object, Integer>> map = createIntermidiateMap(bucket);
			Log.i(TAG, "A - finished map " + bucket[0] + " with map size = "
					+ map.size());

			Log.i(TAG, "B + start reduce of " + bucket[0]);
			Map<Object, Integer> result = reduce(map);
			Log.i(TAG, "B - finished reduce of " + bucket[0] + " with "
					+ result.size());

			Log.i(TAG, "C + final map size before:: " + finalResult.size());
			synchronized (finalResult) {

				for (Entry<Object, Integer> entry : result.entrySet()) {

					if (finalResult.containsKey(entry.getKey())) {
						finalResult.put(
								entry.getKey(),
								finalResult.get(entry.getKey())
										+ entry.getValue());
					} else
						finalResult.put(entry.getKey(), entry.getValue());
				}
			}

			Log.i(TAG, "C - finalResult after " + finalResult.size());

		}
	};

	public List<Object> generateData() {
		int DATA_SIZE = 16384;
		List<Object> list = new ArrayList<Object>();

		int min = 1;
		int max = 1000;

		for (int i = 0; i < DATA_SIZE; i++) {
			list.add(min + (int) (Math.random() * ((max - min) + 1)));
		}

		return list;
	};
}
