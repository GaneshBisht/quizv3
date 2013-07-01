package com.exartisansystemvn.adapter;

import java.util.ArrayList;

import com.exartisansystemvn.bean.Quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class ExamQuizListAdapter extends BaseAdapter {
	private ArrayList<Quiz> examQuizList;
	private LayoutInflater inflater;
	private Context context;
	private int[] ansId;
	private boolean[] ansChecks;
	private boolean isAnsDisable ;
	

	public boolean isAnsDisable() {
		return isAnsDisable;
	}

	public void setAnsDisable(boolean isAnsDisable) {
		this.isAnsDisable = isAnsDisable;
	}

	public boolean[] getAnsChecks() {
		return ansChecks;
	}

	public void setAnsChecks(boolean[] ansChecks) {
		this.ansChecks = ansChecks;
	}

	public ExamQuizListAdapter(Context context, ArrayList<Quiz> quizData){
		isAnsDisable = false;
		this.context = context;
		this.examQuizList = quizData;
		inflater = LayoutInflater.from(context);
		ansId = new int[quizData.size()];
		ansChecks = new boolean[quizData.size()];
		for(int i=0; i<quizData.size();i++) {
			ansId[i] = -1;
			ansChecks[i] = false;
		}
	}
	
	public void restartState(){
		isAnsDisable = false;
		ansId = new int[examQuizList.size()];
		ansChecks = new boolean[examQuizList.size()];
		for(int i=0; i<examQuizList.size();i++) {
			ansId[i] = -1;
			ansChecks[i] = false;
		}
	}

	@Override
	public int getCount() {
		return examQuizList.size();
	}

	@Override
	public Object getItem(int position) {
		return examQuizList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final int pos = position;
		if (convertView == null){
			convertView = inflater.inflate(com.exartisansystemvn.quizz.R.layout.exam_row, null);
			holder = new ViewHolder();
			holder.tvQuestion = (TextView) convertView.findViewById(com.exartisansystemvn.quizz.R.id.tvExamQuestion);
			holder.rdAnswers = (RadioGroup) convertView.findViewById(com.exartisansystemvn.quizz.R.id.rdExamAnswers);
			convertView.setTag(holder);
		} else holder = (ViewHolder) convertView.getTag();
		holder.tvQuestion.setText(examQuizList.get(position).getQuestion());
		holder.rdAnswers.removeAllViews();
		
        for (int i = 0; i<examQuizList.get(position).getAnswers().size(); i++){
        	RadioButton radioButton = new RadioButton(context);
        	radioButton.setId(i);
			radioButton.setText(examQuizList.get(position).getAnswers().get(i));
			radioButton.setEnabled(!isAnsDisable);
			holder.rdAnswers.addView(radioButton);	
        }
        holder.rdAnswers.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId != -1) ansId[pos] = checkedId;
				if (checkedId == examQuizList.get(pos).getCorrectAnswer()) ansChecks[pos] = true; else ansChecks[pos] = false;
			}
		});
        holder.rdAnswers.clearCheck();
        holder.rdAnswers.check(ansId[position]);
		return convertView;
	}

}

class ViewHolder {
	TextView tvQuestion;
	RadioGroup rdAnswers;
}
