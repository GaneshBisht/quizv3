package com.exartisansystemvn.quizz;

import java.util.Collections;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.exartisansystemvn.datamanager.ExamLibraryManager;
import com.exartisansystemvn.util.FontUlts;

public class QuizActivity extends BaseActivity {
	private TextView tvQnums;
	private TextView tvQuestion;
	private TextView tvResult;
	private TextView tvListResult;
	private Button btnSubmit;
	private Button btnAnswer;
	private RadioGroup rdAnswers;
	private LinearLayout layoutResult;
	private int mCurrentQuiznumber;
	private int quiznums;
	private boolean[] userAnswers;
	private int[] ansId;
	private boolean submited;
	private boolean answerornot[];
	private String examname;
	private ExamLibraryManager libraryManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void displayActivity() {
		setContentView(R.layout.quiz);
	}
	
	protected void initVariables() {
		libraryManager = ExamLibraryManager.getInstance();
		mCurrentQuiznumber = 1;
		quiznums = 0;
		submited = false;

	}
	
	protected void initViews() {
		layoutResult = (LinearLayout) findViewById(R.id.layoutResult);
		tvQnums = (TextView) findViewById(R.id.tvQnums);
		tvQuestion = (TextView) findViewById(R.id.tvQuestion);
		tvResult = (TextView) findViewById(R.id.tvResult);
		tvListResult = (TextView) findViewById(R.id.tvListResult);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnAnswer = (Button) findViewById(R.id.btnAnswer);
		rdAnswers = (RadioGroup) findViewById(R.id.rdAnwsers);
		rdAnswers.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId != -1)
					ansId[mCurrentQuiznumber - 1] = checkedId;
				if(checkedId == libraryManager.getExamContent(examname).get(mCurrentQuiznumber - 1).getCorrectAnswer())
					userAnswers[mCurrentQuiznumber - 1] = true;

			}
		});

	}

	protected void initActions() {

		examname = this.getIntent().getExtras().getString("examname");
		quiznums = libraryManager.getExamContent(examname).size();
		tvQnums.setText("" + quiznums);
		userAnswers = new boolean[quiznums];
		ansId = new int[quiznums];
		for (int i = 0; i < quiznums; i++)
			ansId[i] = -1;
		answerornot = new boolean[quiznums];
		for (int i = 0; i < answerornot.length; i++) {
			answerornot[i] = false;
		}
		Collections.shuffle(libraryManager.getExamContent(examname));
		showQuizz(mCurrentQuiznumber);
		// after option choice(0 or 1) will affect on button submit and answer
		onAfterSetting(checkMethod);

	}
	/**
	 * 
	 * @param choice - if choice == 1: answer quiz by quiz. another choice: answer all quizs. 
	 */
	private void onAfterSetting(int choice) {
		if (choice == 1) {
			btnSubmit.setVisibility(Button.GONE);
			btnAnswer.setVisibility(Button.VISIBLE);
			layoutResult.setVisibility(LinearLayout.GONE);
		} else {
			btnSubmit.setVisibility(Button.VISIBLE);
			btnAnswer.setVisibility(Button.GONE);
			layoutResult.setVisibility(LinearLayout.VISIBLE);
		}
	}

	

	/**
	 * replace older Quiz by current Quiz and show it on screen
	 * 
	 * @param currentQuiznumber
	 *            - Integer
	 */
	private void showQuizz(int currentQuiznumber) {
		if (currentQuiznumber - 1 >= 0 && currentQuiznumber - 1 < quiznums) {
			
			String qHead = String.valueOf(currentQuiznumber);
			char ansHead = 'A';
			FontUlts.setFontFor(tvQuestion, fontName, FontUlts.TYPE_TEXT_VIEW, this);
			tvQuestion.setText(new String(qHead + "/ " + libraryManager.getExamContent(examname).get(currentQuiznumber - 1).getQuestion()));
			rdAnswers.removeAllViews();

			for (int i = 0; i < libraryManager.getExamContent(examname).get(currentQuiznumber - 1).getAnswers()
					.size(); i++) {
				if (i>0) ansHead++;
				RadioButton radioButton = new RadioButton(this);
				FontUlts.setFontFor(radioButton, fontName, FontUlts.TYPE_RADIO_BUTTON, this);
				radioButton.setText(new String(ansHead + "/ " + libraryManager.getExamContent(examname).get(currentQuiznumber - 1)
						.getAnswers().get(i)));
				radioButton.setId(i);
				if (submited)
					radioButton.setEnabled(false);
				rdAnswers.addView(radioButton);
			}

			if (answerornot[currentQuiznumber - 1] == true) {
				rdAnswers.getChildAt(
						libraryManager.getExamContent(examname).get(currentQuiznumber - 1).getCorrectAnswer())
						.setBackgroundColor(Color.RED);
				if (libraryManager.getExamContent(examname).get(currentQuiznumber - 1).getCorrectAnswer() != ansId[currentQuiznumber - 1]&&ansId[currentQuiznumber - 1]!=-1)
					rdAnswers.getChildAt(ansId[currentQuiznumber - 1])
							.setBackgroundColor(Color.BLUE);
				for (int i = 0; i < rdAnswers.getChildCount(); i++)
					rdAnswers.getChildAt(i).setEnabled(false);
			}

			rdAnswers.clearCheck();
			rdAnswers.check(ansId[mCurrentQuiznumber - 1]);

		} else if (currentQuiznumber - 1 >= quiznums)
			mCurrentQuiznumber = quiznums;
		else if (currentQuiznumber - 1 < 0)
			mCurrentQuiznumber = 1;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
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

	public void onClickSubmit(View v) {
		int count = 0;
		String listresult = "";
		for (boolean e : userAnswers) {
			if (e == true)
				count++;
		}
		tvResult.setText("" + count);
		btnSubmit.setEnabled(false);
		for(int i=0; i<libraryManager.getExamContent(examname).size();i++) {
			listresult = new String(listresult+(i+1)+". "+ libraryManager.getExamContent(examname).get(i).getAnswers().get(libraryManager.getExamContent(examname).get(i).getCorrectAnswer()).substring(0, 1)+"\n");
		}
		tvListResult.setText(listresult);
		// for after submit
		submited = true;
		showQuizz(mCurrentQuiznumber);

	}

	public void onClickAnswer(View v) {
		answerornot[mCurrentQuiznumber - 1] = true;
		showQuizz(mCurrentQuiznumber);
	}

	public void onClickNext(View v) {
		showQuizz(++mCurrentQuiznumber);
	}

	public void onClickPrevious(View v) {
		showQuizz(--mCurrentQuiznumber);
	}
	

}
