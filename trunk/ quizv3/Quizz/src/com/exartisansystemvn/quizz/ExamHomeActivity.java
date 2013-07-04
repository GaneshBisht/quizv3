package com.exartisansystemvn.quizz;

import com.exartisansystemvn.datamanager.ExamLibraryManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ExamHomeActivity extends BaseActivity {
	
	private Spinner spinnerExamTime;
	private Spinner spinnerExamSubject;
	private Button btnStartExam;
	private Button btnCancelExam;
	private OnClickListener btnClickListener;
	private ArrayAdapter<String> subjectAdapter;
	private ArrayAdapter<Integer> timeAdapter;
	private ExamLibraryManager libraryManager;
	//private OnItemSelectedListener spinnerlListener;
	private Integer[] timeArray = {30, 45, 60};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exam, menu);
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

	@Override
	protected void displayActivity() {
		setContentView(R.layout.exam_home);
		
	}

	@Override
	protected void initViews() {
		spinnerExamTime = (Spinner) findViewById(R.id.spinner_exam_time);
		spinnerExamTime.setAdapter(timeAdapter);
		spinnerExamSubject = (Spinner) findViewById(R.id.spinner_exam_subject);
		spinnerExamSubject.setAdapter(subjectAdapter);
		btnCancelExam = (Button) findViewById(R.id.btnCancelExam);
		btnCancelExam.setOnClickListener(btnClickListener);
		btnStartExam = (Button) findViewById(R.id.btnStartExam);
		btnStartExam.setOnClickListener(btnClickListener);
		
	}

	@Override
	protected void initVariables() {
		libraryManager = new ExamLibraryManager();
		subjectAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,libraryManager.getListExamName());
		subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, timeArray);
		timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btnClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (v==btnStartExam){
					Intent i = new Intent(getBaseContext(), ExamActivity.class);
					i.putExtra("time", spinnerExamTime.getSelectedItem().toString());
					i.putExtra("subject", spinnerExamSubject.getSelectedItem().toString());
					startActivity(i);
				}
				
			}
		};
	}

	@Override
	protected void initActions() {
		
		
	}

}
