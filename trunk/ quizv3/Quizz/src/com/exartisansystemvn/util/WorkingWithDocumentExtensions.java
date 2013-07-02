package com.exartisansystemvn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.exartisansystemvn.bean.Quiz;

import android.os.Environment;
import android.util.Log;

public class WorkingWithDocumentExtensions {
	
	/**
	 * @param folderName - A Folder Name which has files you want to scan 
	 * @param extension - extension or file type.
	 * @return ArrayList< File > - List of files you've just scanned
	 * @Example <br>
	 * <blockquote>
	 * String folderName = "test";<br>
	 * String extension = ".txt";<br>
	 *  List< File > lstFiles = new ArrayList< File >(); <br>
	 * lstFiles = scanFilesInAFolderFromSDCard(folderName,extension);<br>
	 * </blockquote>
	 */
	public ArrayList<File> scanFilesInAFolderFromSDCard(String folderName, final String extension) {
		ArrayList<File> alFile = new ArrayList<File>();
		File folder = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/".concat(folderName));
		// String[] listQuizz;
		// ArrayList<String> listQuizz = new ArrayList<String>();
		folder.mkdirs();
		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getAbsolutePath().matches(".*\\".concat(extension));
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
	 * @param aFile - A quiz file
	 * @return ArrayList< Quiz > - List of quiz in the Quiz File
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
			String question = null;
			boolean isFirstLine = true;
//			String regex = "//w+[.|)|,|/|\\|>]";
			// reading a line is a loop. End loop if the text file has no more
			// lines.
			do {
				line = br.readLine();
				if(isFirstLine) {
					line = line.replaceAll("\\p{C}", "");//replace all hidden/invisible/non-printable characters
					isFirstLine = false;
				}
				if (line != null) {
//					line = line.replaceAll(regex, "").trim();//replace all prefix of each lines
					//Log.e("count" , " " + Integer.valueOf(line.charAt(0)));
					for (int index = 0; index < line.length(); index++) {
						char chartemp = line.charAt(index);
						if(chartemp=='.'||chartemp==')'||chartemp==','||chartemp=='/'||chartemp=='\\'||chartemp=='>'){
							line = line.substring(index+1);
							break;
						}
					}
				}
				if (line == null || line.equals("")) {
					if (isPriviousEmptyLine||question==null)
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
						answers.add(new String(line.substring(1).trim()));
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
}
