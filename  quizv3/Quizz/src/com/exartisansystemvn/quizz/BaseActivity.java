package com.exartisansystemvn.quizz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.exartisansystemvn.bean.Quiz;

public abstract class BaseActivity extends Activity {
	private boolean didInit = false;
	// saving setting variables
	public static int checkMethod;
	// data of the application below
	public Map<String, ArrayList<Quiz>> examinationLibrary = new HashMap<String, ArrayList<Quiz>>();
	public ArrayList<String> lstExaminationName = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSettings();
		doPreparationProcessOfApp();
		displayActivity();
		if (didInit == false) {
			didInit = true;
			initVariables();
		}
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
		lstQuizFile = scanForQizzFileFromSDCard("/Test");
		for (File efile : lstQuizFile) {
			String filename = efile.getName();
			String examname = filename.substring(0, filename.indexOf("."));
			lstExaminationName.add(examname);
			lstQuiz = readContentOfQuizFile(efile);
			examinationLibrary.put(examname, lstQuiz);
		}
	}

	public ArrayList<File> scanForQizzFileFromSDCard(String dir) {
		ArrayList<File> alFile = new ArrayList<File>();
		File folder = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + dir);
		// String[] listQuizz;
		// ArrayList<String> listQuizz = new ArrayList<String>();
		folder.mkdirs();
		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getAbsolutePath().matches(".*\\.txt");
			}
		};
		// for (int i = 0; i < folder.listFiles(filter).length; i++)
		// listQuizz.add(folder.listFiles(filter)[i].getName());
		alFile.addAll(Arrays.asList(folder.listFiles(filter)));
		return alFile;
	}

	/**
	 * Read line by line of the Quiz File and handle each line by following
	 * determind it is question or answer or the end of quiz. Then, the value
	 * them are set into properties of the Quiz class. After all, we've got a
	 * list of quizs.
	 * 
	 * @param strDir
	 *            - Directory: String
	 * @param strFileName
	 *            - File name: String
	 * @return List of quiz in the Quiz File
	 */
	public ArrayList<Quiz> readContentOfQuizFile(File aFile) {
		ArrayList<Quiz> alQuiz = new ArrayList<Quiz>();
		ArrayList<String> answers = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(aFile));
			String line;
			int i = -1;
			int correctAnswer = 0;
			boolean isPriviousEmptyLine = false;
			String question = "";
			// reading a line is a loop. End loop if the text file has no more
			// lines.
			do {
				line = br.readLine().trim();
				if (line == null || line.equals("")) {
					if (isPriviousEmptyLine)
						continue;
					alQuiz.add(new Quiz(question, answers, correctAnswer));
					i = -1;
					answers = new ArrayList<String>();
					isPriviousEmptyLine = true;
				} else if (i == -1) {
					isPriviousEmptyLine = false;
					question = line;
					i++;
				} else {
					isPriviousEmptyLine = false;
					if (line.startsWith("*")) {
						answers.add(new String(line.substring(1)));
						correctAnswer = i;
					} else
						answers.add(line);
					i++;
				}
			} while (line != null);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			Log.e("NullPointerException", "because of method: trim()");
		}
		return alQuiz;
	}

	private void getSettings() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		checkMethod = Integer.valueOf(preferences.getString(
				"check_method_list", "0"));
	}

}
