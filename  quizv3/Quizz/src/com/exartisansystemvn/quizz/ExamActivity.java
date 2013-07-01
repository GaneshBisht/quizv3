package com.exartisansystemvn.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.exartisansystemvn.adapter.ExamQuizListAdapter;

public class ExamActivity extends BaseActivity {
	
	private TextView tvSubject;
	private TextView tvTime;
	private ListView listQuiz;
	private Button btnCheckMark;
	private Button btnRetry;
	private String subject;
	private String time;
	private OnClickListener btnClickListener;
	private ExamQuizListAdapter examQuizListAdapter;

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
		setContentView(R.layout.exam);
	}

	@Override
	protected void initViews() {
		tvSubject = (TextView) findViewById(R.id.tv_subject_title_value);
		tvSubject.setText(subject);
		tvTime = (TextView) findViewById(R.id.tv_exam_time_title_value);
		tvTime.setText(time);
		listQuiz = (ListView) findViewById(R.id.listExamQizz);
		listQuiz.setAdapter(examQuizListAdapter);
		btnCheckMark = (Button) findViewById(R.id.btnCheckExamMark);
		btnCheckMark.setOnClickListener(btnClickListener);
		btnRetry = (Button) findViewById(R.id.btnRetryExam);
		btnRetry.setOnClickListener(btnClickListener);
		btnRetry.setEnabled(false);
	}

	@Override
	protected void initVariables() {
		subject = " " + getIntent().getExtras().getString("subject");
		time = " " + getIntent().getExtras().getString("time") + "'";
		examQuizListAdapter = new ExamQuizListAdapter(this, examinationLibrary.get(subject.trim()));
		btnClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (v==btnCheckMark) {
					btnCheckMark.setEnabled(false);
					btnRetry.setEnabled(true);
					examQuizListAdapter.setAnsDisable(true);
					examQuizListAdapter.notifyDataSetChanged();
					int count = 0;
					for (boolean e : examQuizListAdapter.getAnsChecks()) {
						if (e == true)
							count++;
					}
					Toast toast = Toast.makeText(getBaseContext(), "" + count, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					btnRetry.setEnabled(false);
					btnCheckMark.setEnabled(true);
					examQuizListAdapter.restartState();
					examQuizListAdapter.notifyDataSetChanged();
				}
				
			}
		};
	}

	@Override
	protected void initActions() {
		// TODO Auto-generated method stub
		
	}

}
