package com.mot.multicore.sysinfo;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.mot.multicore.tools.FileReader;

public class SystemInfo {

	private static final String TAG = SystemInfo.class.getName();
	public final String title;
	public final String details;
	public final String help;

	public SystemInfo(String title, String details, String help) {
		super();
		this.title = title;
		this.details = details;
		this.help = help;
	}

	/**
	 * Calculate amount of available cores
	 * 
	 * @return how many cores machine has
	 */
	public static SystemInfo coresInfo() {
		return new SystemInfo("Amount of cores", ""
				+ Runtime.getRuntime().availableProcessors(),
				"Calculated by Runtime.getRuntime().availableProcessors()");
	}

	/**
	 * Read proc speed/frequency - does not work on simulator!
	 */
	public static List<SystemInfo> procSpeedReader() {

		String curFreqPath = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
		String minFreqPath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";
		String maxFreqPath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";

		List<SystemInfo> list = new ArrayList<SystemInfo>();
		list.add(new SystemInfo("current CPU frequency", FileReader
				.readFileWithCat(curFreqPath), "taken from " + curFreqPath));
		list.add(new SystemInfo("minimum CPU frequency", FileReader
				.readFileWithCat(minFreqPath), "taken from " + minFreqPath));
		list.add(new SystemInfo("maximum CPU frequency", FileReader
				.readFileWithCat(maxFreqPath), "taken from " + maxFreqPath));

		return list;
	}

	/**
	 * Read /proc/stat file cpu 2255 34 2290 22625563 6290 127 456 cpu0 1132 34
	 * 1441 11311718 3675 127 438 cpu1 1123 0 849 11313845 2614 0 18 intr
	 * 114930548 113199788 3 0 5 263 0 4 [... lots more numbers ...] ctxt
	 * 1990473 btime 1062191376 processes 2915 procs_running 1 procs_blocked 0
	 * 
	 * The very first "cpu" line aggregates the numbers in all of the other
	 * "cpuN" lines. These numbers identify the amount of time the CPU has spent
	 * performing different kinds of work. Time units are in USER_HZ or Jiffies
	 * (typically hundredths of a second). The meanings of the columns are as
	 * follows, from left to right: user: normal processes executing in user
	 * mode nice: niced processes executing in user mode system: processes
	 * executing in kernel mode idle: twiddling thumbs iowait: waiting for I/O
	 * to complete irq: servicing interrupts softirq: servicing softirqs
	 * 
	 * The "intr" line gives counts of interrupts serviced since boot time, for
	 * each of the possible system interrupts. The first column is the total of
	 * all interrupts serviced; each subsequent column is the total for that
	 * particular interrupt. The "ctxt" line gives the total number of context
	 * switches across all CPUs. The "btime" line gives the time at which the
	 * system booted, in seconds since the Unix epoch. The "processes" line
	 * gives the number of processes and threads created, which includes (but is
	 * not limited to) those created by calls to the fork() and clone() system
	 * calls. The "procs_running" line gives the number of processes currently
	 * running on CPUs. The "procs_blocked" line gives the number of processes
	 * currently blocked, waiting for I/O to complete.
	 * 
	 * @return
	 */
	public static List<SystemInfo> procCpuStatReader() {
		List<SystemInfo> res = new ArrayList<SystemInfo>();
		List<String> procStatInfo = FileReader.transformFile("/proc/stat", 10);

		boolean lastOne = false;
		for (String entry : procStatInfo) {

			Log.v(TAG, entry);

			// no need to work too much - for obvious reasons
			if (true == lastOne)
				break;

			String[] arr = entry.split(" ");

			if (arr.length > 1) {
				SystemInfo sysInfo = new SystemInfo("", "", "");

				if (arr[0].startsWith("cpu"))
					sysInfo = new SystemInfo(
							arr[0],
							arr[1] + " " + arr[2] + " " + arr[3],
							"amount of time the CPU has spent performing different kinds of work: user, nice, system");

				if (arr[0].startsWith("intr"))
					sysInfo = new SystemInfo(arr[0], arr[1],
							"counts of interrupts serviced since boot time");

				if (arr[0].startsWith("ctxt"))
					sysInfo = new SystemInfo(arr[0], arr[1],
							"total number of context switches across all CPUs");

				if (arr[0].startsWith("btime"))
					sysInfo = new SystemInfo(arr[0], arr[1],
							"time at which the system booted, in seconds since the Unix epoch");

				if (arr[0].startsWith("processes"))
					sysInfo = new SystemInfo(arr[0], arr[1],
							"number of processes and threads created");

				if (arr[0].startsWith("procs_running"))
					sysInfo = new SystemInfo(arr[0], arr[1],
							"number of processes currently running on CPUs");

				if (arr[0].startsWith("procs_blocked")) {
					sysInfo = new SystemInfo(arr[0], arr[1],
							"number of processes currently blocked, waiting for I/O to complete");
					lastOne = true;
				}

				res.add(sysInfo);
			}
		}

		Log.v(TAG, "collected: " + res);
		return res;
	}

	/**
	 * Read /proc/cpuinfo file Example file: Processor : ... BogoMIPS : ...
	 * BogoMIPS: http://en.wikipedia.org/wiki/BogoMips Features : ... Hardware :
	 * ...
	 * 
	 * @return
	 */
	public static List<SystemInfo> procCpuInfoReader() {
		List<SystemInfo> res = new ArrayList<SystemInfo>();
		List<String> procInfoData = FileReader.transformFile("/proc/cpuinfo",
				10);

		for (String entry : procInfoData) {
			String[] arr = entry.split(":");

			if (arr.length > 1) {

				if (arr[0].startsWith("Processor"))
					res.add(new SystemInfo(arr[0], arr[1].trim(),
							"type of processor"));

				if (arr[0].startsWith("Bogo"))
					res.add(new SystemInfo(arr[0], arr[1].trim(),
							"number of million times per second a processor can do absolutely nothing"));

				if (arr[0].startsWith("Features"))
					res.add(new SystemInfo(arr[0], arr[1].trim(),
							"processor features"));
			}
		}

		return res;
	}

	/**
	 * Create dummy elements
	 * @param howMany
	 * @return created list
	 */
	public static List<SystemInfo> getDummyElements(int howMany) {
		List<SystemInfo> list = new ArrayList<SystemInfo>();
		for (int i = 0; i < howMany; i++)
			list.add(new SystemInfo("empty element " + i, "", ""));
		return list;
	}
}
