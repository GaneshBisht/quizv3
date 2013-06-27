package com.exartisansystemvn.quizz;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ExamHomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_home);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exam, menu);
		return true;
	}

}
