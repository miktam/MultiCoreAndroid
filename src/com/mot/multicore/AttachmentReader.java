package com.mot.multicore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mot.multicore.tools.ToastMaker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class AttachmentReader extends Activity {

	private static final String TAG = AttachmentReader.class.getName();
	public static final String DIRECTORY_APPLICATION = "/sdcard/multicore/";
	public static final String DATA_FILENAME = "data.dat";
	public static final String FULL_PATH_TO_FILE = DIRECTORY_APPLICATION + DATA_FILENAME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.d(TAG, "AttachmentReader.onCreate");

		super.onCreate(savedInstanceState);
		
		readIntent(getIntent());

		Uri path = getIntent().getData();
		Log.v(TAG, "attachment to read = " + path);

		ContentResolver cr = getContentResolver();

		try {
			InputStream stream = cr.openInputStream(path);
			saveFileInPeace(stream);
			
			Intent prodigy = new Intent(this, Main.class);
			this.startActivity(prodigy);
			
		} catch (FileNotFoundException e) {
			ToastMaker.getToast(this, e.getLocalizedMessage());
			this.finish();
		}

	}

	private void saveFileInPeace(InputStream fis) {

		// create a File object for the parent directory
		File destinationFileDir = new File(DIRECTORY_APPLICATION);
		// have the object build the directory structure, if needed.
		destinationFileDir.mkdirs();
		// create a File object for the output file
		File destinationFile = new File(destinationFileDir, DATA_FILENAME);

		try {
			byte[] readData = new byte[1024];			
			// now attach the OutputStream to the file object, instead of a String representation
			FileOutputStream fos = new FileOutputStream(destinationFile);
			int i = fis.read(readData);

			while (i != -1) {
				fos.write(readData, 0, i);
				i = fis.read(readData);
			}
			fis.close();
			fos.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void readIntent(Intent intent) {
		String path = intent.getData().getPath();
		Log.v(TAG, "path = " + path);

		String dataString = intent.getDataString();
		Log.v(TAG, "dataString = " + dataString);

		String uri = intent.getData().toString();
		Log.v(TAG, "uri = " + uri);
	}
	
	public static boolean isFileExist()
	{
		File destinationFile = new File(FULL_PATH_TO_FILE);
		boolean fileExist = destinationFile.exists();
		Log.d(TAG, "is file exist? " + fileExist);
		return fileExist;		
	}
}
