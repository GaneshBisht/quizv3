package com.exartisansystemvn.quizz;
/**
 * exam activity 
 */
import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.exartisansystemvn.adapter.ExamQuizListAdapter;
import com.exartisansystemvn.bean.Quiz;
import com.exartisansystemvn.datamanager.ExamLibraryManager;

public class ExamActivity extends BaseActivity {
	
	private LinearLayout examRootLayout;
	private TextView tvSubject;
	private TextView tvTime;
	private ListView listQuiz;
	private Button btnCheckMark;
	private Button btnRetry;
	private String subject;
	private String time;
	private OnClickListener btnClickListener;
	private ExamQuizListAdapter examQuizListAdapter;
	private CountDownTimer examTimeCountDown;
	private ExamLibraryManager libraryManager;

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
	/**
	 * tao menu
	 * @author anh
	 * 
	 */
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
		examRootLayout = (LinearLayout) findViewById(R.id.examRootLayout);
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
		libraryManager = new ExamLibraryManager();
		subject = " " + getIntent().getExtras().getString("subject");
		time = " " + getIntent().getExtras().getString("time") + "'";
		examQuizListAdapter = new ExamQuizListAdapter(this, shuffleExamQuiz(libraryManager.getExamContent(subject.trim()), true));
		//examQuizListAdapter = new ExamQuizListAdapter(this, examinationLibrary.get(subject.trim()));
		btnClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (v==btnCheckMark) {
					checkMark();
				} else {
					btnRetry.setEnabled(false);
					btnCheckMark.setEnabled(true);
					examQuizListAdapter.restartState();
					examQuizListAdapter.notifyDataSetChanged();
					examTimeCountDown.start();
				}
				
			}
		};
	}

	@Override
	protected void initActions() {
		long time = Long.valueOf(getIntent().getStringExtra("time"))*60000;
		examTimeCountDown = new CountDownTimer(time, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				tvTime.setText(" " + millisUntilFinished/60000 + "'");
				
			}
			
			@Override
			public void onFinish() {
				tvTime.setText(" 0'");
				checkMark();
				
			}
		};
		examTimeCountDown.start();
	}
	
	private void checkMark(){
		examTimeCountDown.cancel();
		btnCheckMark.setEnabled(false);
		btnRetry.setEnabled(true);
		examQuizListAdapter.setAnsDisable(true);
		examQuizListAdapter.notifyDataSetChanged();
		int count = 0;
		for (boolean e : examQuizListAdapter.getAnsChecks()) {
			if (e)
				count++;
		}
		//Toast toast = Toast.makeText(getBaseContext(), "" + count, Toast.LENGTH_SHORT);
		//toast.show();
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		  shareIntent.setType("text/plain");
		  shareIntent.putExtra(Intent.EXTRA_TEXT, new String("" + count));
		  startActivity(Intent.createChooser(shareIntent, "Share..."));
	}
	
	private ArrayList<Quiz> shuffleExamQuiz(ArrayList<Quiz> quizList, boolean willShuffleAnswers){
		ArrayList<Quiz> result = quizList;
		String correct_ans;
		Collections.shuffle(result);
		if (willShuffleAnswers) for (Quiz quiz : result) {
			correct_ans = quiz.getAnswers().get(quiz.getCorrectAnswer());
			Collections.shuffle(quiz.getAnswers());
			for (String ans : quiz.getAnswers()) {
				if (ans.equals(correct_ans)) quiz.setCorrectAnswer(quiz.getAnswers().indexOf(ans));
			}
		}
		/*ArrayList<Quiz> result = quizList;
		String[] qHead = new String[result.size()];
		for (int i=0; i<qHead.length; i++) qHead[i] = "" + (i + 1);
		String correct_ans;
		char[] head;
		ArrayList<String> tmpAns;
		Collections.shuffle(result);
		for (Quiz quiz : result) {
			quiz.setQuestion(qHead[result.indexOf(quiz)].concat(quiz.getQuestion().substring(quiz.getQuestion().indexOf('.'))));
			correct_ans = quiz.getAnswers().get(quiz.getCorrectAnswer());
			head = new char[quiz.getAnswers().size()];
			head[0]='A';
			for (int i=1; i<head.length; i++) {
				head[i] = head[i-1];
				head[i]++;
			}
			int i=0;
			tmpAns = new ArrayList<String>();
			Collections.shuffle(quiz.getAnswers());
			for (String ans : quiz.getAnswers()) {
				if (ans.equals(correct_ans)) quiz.setCorrectAnswer(i);
				tmpAns.add(new String("" + head[i]).concat(ans.substring(ans.indexOf('.'))));
				i++;
			}
			quiz.setAnswers(tmpAns);
		}*/
		return result;
	}

	
}
