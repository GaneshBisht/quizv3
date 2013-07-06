package com.exartisansystemvn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.textmining.text.extraction.WordExtractor;

import android.os.Environment;
import android.util.Log;

import com.exartisansystemvn.bean.Quiz;

public class WorkingWithDocumentExtensions {
	
	/**
	 * @param folderName - A Folder Name which has files you want to scan 
	 * @param extensions[] - String Array of extensions or file types.
	 * @return ArrayList< File > - List of files you've just scanned
	 * @Example <br>
	 * <blockquote>
	 * String folderName = "test";<br>
	 * String extension = ".txt";<br>
	 *  List< File > lstFiles = new ArrayList< File >(); <br>
	 * lstFiles = scanFilesInAFolderFromSDCard(folderName,extension);<br>
	 * </blockquote>
	 */
	public ArrayList<File> scanFilesInAFolderFromSDCard(String folderName, final String[] extensions) {
		ArrayList<File> alFile = new ArrayList<File>();
		File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/".concat(folderName));
		// String[] listQuizz;
		// ArrayList<String> listQuizz = new ArrayList<String>();
		folder.mkdirs();
		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
//				return pathname.getAbsolutePath().matches(".*\\"+extension);
				return hasAValidExtension(pathname.getAbsolutePath(), extensions);
			}
		};
		// for (int i = 0; i < folder.listFiles(filter).length; i++)
		// listQuizz.add(folder.listFiles(filter)[i].getName());
		alFile.addAll(Arrays.asList(folder.listFiles(filter)));
		return alFile;
	}
	
	/**
	 * For only reading text files, This reads content and handle it. 
	 * In other words, This tranforms a raw data to organized data.
	 * @param aFile - must be a text file. The program will not run correctly if it's passed wrong values
	 * @return ArrayList< Quiz >
	 */
	public ArrayList<Quiz> handleContentOfTextFile(File aFile){
		BufferedReader bufReader = null;
		try {
			bufReader = new BufferedReader(new FileReader(aFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return handleContentWithFormat(bufReader);
	}
	
	/**
	 * For only reading word files, This reads content and handle it. 
	 * In other words, This tranforms a raw data to organized data.
	 * @param aFile - must be a word file(which contain the extension is ".doc" or ".docx").
	 * The program will not run correctly if it's passed wrong values
	 * @param extension - must be ".doc" or ".docx"
	 * @return ArrayList< Quiz >
	 */
	public ArrayList<Quiz> handleContentOfWordFile(File aFile, String extension) {
		BufferedReader bufReader = null;
		String content = "";
		try {
			FileInputStream finStream = new FileInputStream(aFile);
			if (extension.equals(".doc")) {
				WordExtractor wordExtractor = new WordExtractor();
				content = wordExtractor.extractText(finStream);
				bufReader = new BufferedReader(new StringReader(content));
				finStream.close();
			} else if (extension.equals(".docx")) {

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return handleContentWithFormat(bufReader);
	}

	/**
	 * Read line by line and handle each line by following
	 * determind it is question or answer or the end of quiz. Then, the value
	 * them are set into properties of Quiz Objects. After all, we've got a
	 * list of quizs.
	 * @param aFile - A quiz file
	 * @return ArrayList< Quiz > - List of quiz in the Quiz File
	 */
	public ArrayList<Quiz> handleContentWithFormat(BufferedReader bufReader) {
		ArrayList<Quiz> alQuiz = new ArrayList<Quiz>();
		ArrayList<String> answers = new ArrayList<String>();
		try {
			String line;
			int i = -1;
			int correctAnswer = 0;
			boolean isPriviousEmptyLine = false;
			String question = null;
			boolean isFirstLine = true;
			boolean markedLine = false;
			do {
				line = bufReader.readLine();
				
				// replace all hidden/invisible/non-printable characters
				if (isFirstLine) {
					line = line.replaceFirst("\\p{C}", "");
					isFirstLine = false;
				}
				if (line != null) {
					//cut the prefix of each line. Eg: "1.", "A,",...
					for (int index = 0; index < line.length(); index++) {
						char chartemp = line.charAt(index);
						if (chartemp == '*')
							markedLine = true;
						if (chartemp == '.' || chartemp == ')'
								|| chartemp == ',' || chartemp == '/'
								|| chartemp == '\\' || chartemp == '>') {
							line = line.substring(index + 1).trim();
							// take back character '*' for each correct answers
							if (markedLine) {
								line = new String("*" + line);
								markedLine = false;
							}
							break;
						}
					}
				}
				//jump into here if already read over a quiz. The content separates each quiz by line "" 
				if (line == null || line.equals("")) {
					if (isPriviousEmptyLine || question == null)
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
			bufReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			Log.e("NullPointerException", "because of method: trim()");
		}
		return alQuiz;
	}
	
	/**
	 * Check whether a file name contains an extension or not.
	 * @param fileName eg: "foo.bar" 
	 * @param extension eg: ".bar"
	 * @return true - if it contains<br>
	 * false - if it's not
	 */
	public boolean containsExtension(String fileName, String extension) {
		Pattern pattern = Pattern.compile(extension);
		Matcher matcher = pattern.matcher(fileName);
		if (matcher.find()) return true;
		else return false;
	}
	
	/**
	 * Check whether a file name has a valid extension or not 
	 * by following few extensions the application allows.
	 * @param fileName
	 * @param extensions - String[]. All of extensions the application allows
	 * @return true - if it has. <br>
	 * false - if it hasn't 
	 */
	public boolean hasAValidExtension(String fileName, String[] extensions){
		String regex="";
		for (int i = 0; i < extensions.length; i++) {
			regex = regex + extensions[i].substring(1);
			if(i!= extensions.length-1) regex = regex+"|";
		}
		regex = "((\\.(?i)("+regex+"))$)";
		Log.d("regex", regex);
		if(containsExtension(fileName, regex)==true) return true;
		else return false;
	}
}
