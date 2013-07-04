package com.exartisansystemvn.bean;

import java.util.ArrayList;

public class Exam {
	private String examName;
	private ArrayList<Quiz> content;
	
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public ArrayList<Quiz> getContent() {
		return content;
	}
	public void setContent(ArrayList<Quiz> content) {
		this.content = content;
	}
}
