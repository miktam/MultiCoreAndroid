package com.mot.multicore.mapreduce;

public class MapReduceEntry {
	public final Object value;
	public final Integer occurence;
	public final String threadsUsed;
	
	
	public MapReduceEntry(Object value, Integer occurence, String threads) {
		super();
		this.value = value;
		this.occurence = occurence;
		this.threadsUsed = threads;
	}


	public MapReduceEntry(String value2, String string, String time) {
		this.value = value2;
		this.occurence = null;
		this.threadsUsed = time;
	}
	
	
}
