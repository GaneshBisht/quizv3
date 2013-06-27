package com.exartisansystemvn.bean;

import java.util.ArrayList;

public class Quiz {
	private String question;
	private ArrayList<String> answers;
	/*
	 * correctAnswer == 1: A is the correct answer
	 * correctAnswer == 2: B is the correct answer
	 * correctAnswer == 3: C is the correct answer
	 * so on..
	 */
	private int correctAnswer;
	

	public Quiz(){
		
	}
	public Quiz(String question, ArrayList<String> answers) {
		super();
		this.question = question;
		this.answers = answers;
	}
	public Quiz(String question, ArrayList<String> answers, int correctAnswer) {
		super();
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public ArrayList<String> getAnswers() {
		return answers;
	}
	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}
	public int getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
}
