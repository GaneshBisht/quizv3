package com.exartisansystemvn.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListFileActivity extends BaseActivity implements OnItemClickListener {
	
	private ListView lvFiles;
	private ArrayAdapter<String> lvFilesAdapter;
	private int setting = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listfile, menu);
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(this, QuizActivity.class);
		intent.putExtra("examname", lstExaminationName.get(arg2));
		startActivity(intent);
	}
	
	@Override
	protected void displayActivity() {
		setContentView(R.layout.listfile);
	}

	@Override
	protected void initViews() {
		lvFiles = (ListView) findViewById(R.id.lvFiles);
		lvFiles.setOnItemClickListener(this);
	}

	@Override
	protected void initVariables() {
		lvFilesAdapter = new ArrayAdapter<String>(this, R.layout.row, lstExaminationName);
	}

	@Override
	protected void initActions() {
		lvFiles.setAdapter(lvFilesAdapter);
	}


}
