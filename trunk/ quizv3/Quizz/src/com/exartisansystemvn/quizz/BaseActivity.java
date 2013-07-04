package com.exartisansystemvn.quizz;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.preference.PreferenceManager;
import android.util.Log;

import com.exartisansystemvn.datamanager.ExamLibraryManager;
import com.exartisansystemvn.util.WorkingWithDocumentExtensions;

public abstract class BaseActivity extends Activity {
	protected static boolean didStart = false;
	// saving setting variables
	public static int checkMethod;
	// data of the application below

	private final String folderName = "test";
	private String extension = ".txt";

	private ExamLibraryManager examManager = new ExamLibraryManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSettings();
		if (didStart == false) {
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
		WorkingWithDocumentExtensions docWorker = new WorkingWithDocumentExtensions();
		long start = System.currentTimeMillis();
		lstQuizFile = docWorker.scanFilesInAFolderFromSDCard(folderName,
				extension);
		for (File efile : lstQuizFile) {
			String fileName = efile.getName();
			examManager.addExam(folderName, fileName);
		}
		long end = System.currentTimeMillis();
		Log.i("Running Time", "" + (end - start) / 1000);
	}

	private void getSettings() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		checkMethod = Integer.valueOf(preferences.getString(
				"check_method_list", "0"));
	}

	protected void observeFiles() {
		final String directory = Environment.getExternalStorageDirectory()
				.getAbsolutePath().concat("/" + folderName);
		final WorkingWithDocumentExtensions docWorker = new WorkingWithDocumentExtensions();
		FileObserver fileObserver = new FileObserver(directory) {

			@Override
			public void onEvent(int event, String fileName) {
				Log.d("FileObserver", event + ":" + directory + "/" + fileName);
				if (docWorker.matchExtension(fileName, extension)) {
					switch (event) {
					case FileObserver.DELETE:
						examManager.deleteExam(fileName);
						break;
					case FileObserver.CLOSE_WRITE:
						if (examManager.containsKey(fileName)) {
							examManager.updateExam(folderName, fileName);
						} else {
							examManager.addExam(folderName, fileName);
						}
						break;
					case FileObserver.CLOSE_NOWRITE:

					case FileObserver.MOVED_TO:
						examManager.addExam(folderName, fileName);
						break;
					case FileObserver.MOVED_FROM:
						examManager.deleteExam(fileName);
						break;
					}
				}

			}
		};
		fileObserver.startWatching(); // START OBSERVING
	}

}
