package com.exartisansystemvn.quizz;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.preference.PreferenceManager;
import android.util.Log;

import com.exartisansystemvn.bean.Quiz;
import com.exartisansystemvn.datamanager.ExamLibraryManager;
import com.exartisansystemvn.util.WorkingWithDocumentExtensions;

public abstract class BaseActivity extends Activity {
	protected static boolean didStart = false;
	// saving setting variables
	public static int checkMethod;
	// data of the application below
	public static Map<String, ArrayList<Quiz>> examinationLibrary = new HashMap<String, ArrayList<Quiz>>();
	public static ArrayList<String> lstExaminationName = new ArrayList<String>();
	
	private ExamLibraryManager examManager = new ExamLibraryManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSettings();
		if(didStart==false){
			didStart = true;
			doPreparationProcessOfApp();
		}
		observeFiles();
		displayActivity();			
		initVariables();
		initViews();
		initActions();
	}

	/**
	 * SetContentView, display setting and so on..
	 */
	protected abstract void displayActivity();

	/**
	 * initialize Views
	 */
	protected abstract void initViews();

	/**
	 * initialize global variables
	 */
	protected abstract void initVariables();

	/**
	 * beginning actions of the Activity
	 */
	protected abstract void initActions();

	/**
	 * Scan files from sdcard then read content of those.</br> Finally, save
	 * result of those processes into some collections.</br> Those are data of
	 * the application. Thus, write once, use the data everywhere.
	 */
	private void doPreparationProcessOfApp() {
		ArrayList<File> lstQuizFile = new ArrayList<File>();
		ArrayList<Quiz> lstQuiz = new ArrayList<Quiz>();
		WorkingWithDocumentExtensions docWorker = new WorkingWithDocumentExtensions();
		long start = System.currentTimeMillis();
		lstQuizFile = docWorker.scanFilesInAFolderFromSDCard("test", ".txt");
		for (File efile : lstQuizFile) {
			String fileName = efile.getName();
			examManager.addExam("test", fileName);
			/*String examname = filename.substring(0, filename.indexOf("."));
			lstExaminationName.add(examname);
			lstQuiz = docWorker.handleContentOfTextFile(efile);
			examinationLibrary.put(examname, lstQuiz);*/
		}
		long end = System.currentTimeMillis();
		Log.i("Running Time", ""+(end-start)/1000);
	}

	private void getSettings() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		checkMethod = Integer.valueOf(preferences.getString(
				"check_method_list", "0"));
	}
	
	protected void observeFiles() {
		final String directory = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/test");
		FileObserver fileObserver = new FileObserver(directory) {
			
			@Override
			public void onEvent(int event, String fileName) {
//				  Log.d("FileObserver", event+":" + directory+ "/" + fileName);
				if(event==FileObserver.DELETE){
					examManager.deleteExam(fileName);
				}
			}
		};
		fileObserver.startWatching(); //START OBSERVING 
	}

}
