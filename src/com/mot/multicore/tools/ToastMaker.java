package com.mot.multicore.tools;

import android.app.Activity;
import android.widget.Toast;

public class ToastMaker {
	
	private static ToastMaker INSTANCE = null;
	
	public static ToastMaker getInstance()
	{
		if (null == INSTANCE)
			INSTANCE = new ToastMaker();
		
		return INSTANCE;
	}
	
	public static void getToast(Activity act, String toShow)
	{
		Toast tst = Toast.makeText(act, toShow, Toast.LENGTH_SHORT);
		tst.show();
	}

}
