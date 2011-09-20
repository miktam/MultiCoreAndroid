package com.mot.multicore.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import android.util.Log;

public class FileReader {
	
	private final static String TAG = FileReader.class.getName();
	private final static  String encoding = "iso-8859-2";
	
	/**
	 * Transform file to list of strings 
	 * If line has more tokens separated by space, return first _howManyPos_
	 * @return file as list of strings
	 */
	public static List<String> transformFile(String fileLocation, int howManyPos) {

		Log.v(TAG, "read file: " + fileLocation);
		List<String> arrayList = new ArrayList<String>();

		try {

			Scanner scanner = new Scanner(
					new FileInputStream(fileLocation), encoding);
			try {
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					String[] tokens = line.split(" ");
					List<String> firstElements = Arrays.asList(tokens)
							.subList(
									0,
									(howManyPos - 1) < Arrays.asList(tokens)
											.size() ? howManyPos - 1 : Arrays
											.asList(tokens).size());
					
					Log.v(TAG, firstElements.toString());
					
					String combined = "";

					for (String element : firstElements)
						combined += element + " ";
							
					combined += (howManyPos - 1) < Arrays.asList(tokens)
							.size() ? "..." : "";

					Log.v(TAG, line.toString());
					arrayList.add(combined);
				}
			} finally {
				scanner.close();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return arrayList;
	}


}
