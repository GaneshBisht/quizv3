package com.exartisansystemvn.datamanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.os.Environment;

import com.exartisansystemvn.bean.Exam;
import com.exartisansystemvn.bean.Quiz;
import com.exartisansystemvn.util.WorkingWithDocumentExtensions;

public class ExamLibraryManager {
	private static HashMap<String, Exam> examLibrary;

	public ExamLibraryManager() {
		if (examLibrary == null)
			examLibrary = new HashMap<String, Exam>();
	}

	public ArrayList<String> getListExamName() {
		ArrayList<String> listExamName = new ArrayList<String>();
		for (String key : examLibrary.keySet()) {
			listExamName.add(examLibrary.get(key).getExamName());
		}
		return listExamName;
	}

	public ArrayList<Quiz> getExamContent(String examName) {
		String fileName = getFileNameFrom(examName, null);
		if (fileName != null)
			return examLibrary.get(fileName).getContent();
		else
			return null;
	}

	public String getFileNameFrom(String examName, String defaultValue) {
		String fileName = defaultValue;
		for (String key : examLibrary.keySet()) {
			if (examLibrary.get(key).getExamName().equals(examName)) {
				fileName = key;
				break;
			}
		}
		return fileName;
	}

	public String getExamNameFrom(String fileName) {
		return fileName.substring(0, fileName.indexOf("."));
	}

	public boolean deleteExam(String fileName) {
		if (examLibrary.containsKey(fileName)) {
			examLibrary.remove(fileName);
			return true;
		} else
			return false;
	}

	/**
	 * luc khac viet lai phan kiem tra xem luc nao thi add thanh cong hay ko
	 * @param folderName
	 * @param fileName
	 */
	public void addExam(String folderName, String fileName) {
		WorkingWithDocumentExtensions docWorker = new WorkingWithDocumentExtensions();
		Exam examToAdd = new Exam();
		File examFile = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/".concat(folderName)
				+ "/".concat(fileName));
		examToAdd.setExamName(getExamNameFrom(fileName));
		examToAdd.setContent(docWorker.handleContentOfTextFile(examFile));
		examLibrary.put(fileName, examToAdd);
	}

	/**
	 * luc khac viet lai phan kiem tra xem luc nao thi update thanh cong hay ko
	 * @param folderName
	 * @param fileName
	 */
	public void updateExam(String folderName, String fileName) {
		deleteExam(fileName);
		addExam(folderName, fileName);
	}
	
	public boolean containsKey(String fileName){
		if(examLibrary.containsKey(fileName)) return true;
		else return false;
	}
}
