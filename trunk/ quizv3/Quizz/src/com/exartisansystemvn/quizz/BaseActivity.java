package com.exartisansystemvn.quizz;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.exartisansystemvn.datamanager.ExamLibraryManager;
import com.exartisansystemvn.util.WorkingWithDocumentExtensions;

public abstract class BaseActivity extends Activity {
	protected static boolean didStart = false;
	// saving setting variables
	public static int checkMethod;
	public static String fontName;
	// data of the application below

	private final String folderName = "test";
	private String[] extensions = {".txt",".doc"};

	private ExamLibraryManager examManager = ExamLibraryManager.getInstance();

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
		final Handler dataHandler = new Handler();
		final ProgressDialog dataProgressDialog = ProgressDialog.show(this, "Loading data", "Plese wait ...");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ArrayList<File> lstQuizFile = new ArrayList<File>();
				WorkingWithDocumentExtensions docWorker = new WorkingWithDocumentExtensions();
				long start = System.currentTimeMillis();
				lstQuizFile = docWorker.scanFilesInAFolderFromSDCard(folderName,
						extensions);
				for (File efile : lstQuizFile) {
					String fileName = efile.getName();
					examManager.addExam(folderName, fileName);
				}
				long end = System.currentTimeMillis();
				dataHandler.post(new Runnable() {
					
					@Override
					public void run() {
						dataProgressDialog.dismiss();
						
					}
				});
				Log.i("Running Time", "" + (end - start) / 1000);
				
			}
		}).start();
	}

	private void getSettings() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		checkMethod = Integer.valueOf(preferences.getString(
				"check_method_list", "0"));
		fontName = preferences.getString("font_list", "default");
	}

	protected void observeFiles() {
		final String directory = Environment.getExternalStorageDirectory()
				.getAbsolutePath().concat("/" + folderName);
		final WorkingWithDocumentExtensions docWorker = new WorkingWithDocumentExtensions();
		FileObserver fileObserver = new FileObserver(directory) {

			@Override
			public void onEvent(int event, String fileName) {
				Log.d("FileObserver", event + ":" + directory + "/" + fileName);
				if (docWorker.hasAValidExtension(fileName, extensions)) {
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