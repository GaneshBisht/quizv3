package com.exartisansystemvn.datamanager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Environment;

import com.exartisansystemvn.bean.Exam;
import com.exartisansystemvn.bean.Quiz;
import com.exartisansystemvn.util.WorkingWithDocumentExtensions;

/**
 *  This class is manager of exam library. Which contains data of the application.<br>
 *  It provides some methods to work with the data such as: add, delete, update,...
 */
public class ExamLibraryManager {
	
	private static ExamLibraryManager singletonManager;
	private HashMap<String, Exam> examLibrary;

	private ExamLibraryManager() {
//		if (examLibrary == null)
			examLibrary = new HashMap<String, Exam>();
	}
	
	/**
	 * Singleton pattern's method to get the unique instance of the class 
	 * which has declared for whole application. It's just like a global object.
	 * @return the singleton object of ExamLibrary
	 */
	public static synchronized ExamLibraryManager getInstance(){
		if(singletonManager==null){
			singletonManager = new ExamLibraryManager();
		}
		return singletonManager;
	}

	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException();
	}
	
	/**
	 * Get The list of Exam's Name.
	 * @return ArrayList< String >
	 */
	public ArrayList<String> getListExamName() {
		ArrayList<String> listExamName = new ArrayList<String>();
		for (String key : examLibrary.keySet()) {
			listExamName.add(examLibrary.get(key).getExamName());
		}
		return listExamName;
	}
	
	/**
	 * Get content of the Exam. That is a list of quizs.
	 * @param examName
	 * @return ArrayList< Quiz >
	 */
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
	
	/**
	 * Get an exam name from a file name.
	 * @param fileName
	 * @return String
	 */
	public String getExamNameFrom(String fileName) {
		return fileName.substring(0, fileName.indexOf("."));
	}
	
	/**
	 * Delete the exam from Exam Library.
	 * @param fileName - key of the Library. The library manages data by key.<br> Eg: "test.txt", "document.doc",...
	 * @return true - if adding success <br>
	 * false - if adding failure
	 */
	public boolean deleteExam(String fileName) {
		if (examLibrary.containsKey(fileName)) {
			examLibrary.remove(fileName);
			return true;
		} else
			return false;
	}

	/**
	 * Add An Exam into Exam Library
	 * @param folderName <br> eg: "Test", "Folder",...
	 * @param fileName - key of the Library. The library manages data by key.<br> Eg: "test.txt", "document.doc",...
	 * @return true - if adding success <br>
	 * false - if adding failure
	 */
	public boolean addExam(String folderName, String fileName) {
		boolean added;
		WorkingWithDocumentExtensions docWorker = new WorkingWithDocumentExtensions();
		Exam examToAdd = new Exam();
		File examFile = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/".concat(folderName)
				+ "/".concat(fileName));
		examToAdd.setExamName(getExamNameFrom(fileName));
		if(docWorker.containsExtension(fileName, ".doc")) {
			  examToAdd.setContent(docWorker.handleContentOfWordFile(examFile, ".doc"));
			  added = true;
			  }	
		else if(docWorker.containsExtension(fileName, ".txt")){
			examToAdd.setContent(docWorker.handleContentOfTextFile(examFile));
			added = true;
		} else added = false;	
		
		examLibrary.put(fileName, examToAdd);
		return added;
	}

	/**
	 * Call this method if you want to modify content of an exam.
	 * @param folderName <br> eg: "Test", "Folder",...
	 * @param fileName - key of the Library. The library manages data by key.<br> Eg: "test.txt", "document.doc",...
	 * @return true - if adding success <br>
	 * false - if adding failure
	 */
	public boolean updateExam(String folderName, String fileName) {
		if (containsKey(fileName)==true) {
			deleteExam(fileName);
			addExam(folderName, fileName);
			return true;
		} else return false;
		
	}
	
	/**
	 * Check whether the library contains the key or not.
	 * @param fileName
	 * @return
	 */
	public boolean containsKey(String fileName) {
		if (examLibrary.containsKey(fileName))
			return true;
		else
			return false;
	}
}
