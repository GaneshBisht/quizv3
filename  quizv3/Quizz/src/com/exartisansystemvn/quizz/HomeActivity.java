package com.exartisansystemvn.quizz;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity {
	
	private Button btnPractice;
	private Button btnExam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		initViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.settings:
		{
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private OnClickListener btnsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v==btnExam) {
				Intent i = new Intent(getBaseContext(), ExamHomeActivity.class);
				startActivity(i);
			}
			else {
				Intent i = new Intent(getBaseContext(), ListFileActivity.class);
				startActivity(i);
			}
			
		}
	};
	
	private void initViews(){
		btnExam = (Button) findViewById(R.id.btnExam);
		btnExam.setOnClickListener(btnsListener);
		btnPractice = (Button) findViewById(R.id.btnPractice);
		btnPractice.setOnClickListener(btnsListener);
	}

}
