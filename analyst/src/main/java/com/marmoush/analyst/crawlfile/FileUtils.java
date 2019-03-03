package com.marmoush.analyst.crawlfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class FileUtils {
	public static void logToFile(String string, String logFile) {
		logToFile(string, logFile, true, true);
	}

	public static void logToFile(String string, String logFile, boolean append,
			boolean consoleprint) {
		if (consoleprint)
			System.out.println(string);
		FileWriter out = null;
		try {
			out = new FileWriter(logFile, append);
			out.write("\n");
			out.write(string);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static TreeSet<String> readAll(String... path) {
		return readFileToTreeSet(FilePath.PRONOUNS, FilePath.PREPOSITIONS,
				FilePath.PERSONAL_MAIL_COMPANIES);
	}

	public static List<String> readFile(String file) {
		List<String> lines = new ArrayList<String>();
		File emailsFile = new File(file);
		try {
			FileReader fread = new FileReader(emailsFile);
			BufferedReader buffReader = new BufferedReader(fread);
			String line = " ";
			while ((line = buffReader.readLine()) != null) {
				lines.add(line);
			}
			buffReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public static TreeSet<String> readFileToTreeSet(String... files) {
		TreeSet<String> lines = new TreeSet<String>();
		for (String file : files) {
			File emailsFile = new File(file);
			try {
				FileReader fread = new FileReader(emailsFile);
				BufferedReader buffReader = new BufferedReader(fread);
				String line = " ";
				while ((line = buffReader.readLine()) != null) {
					lines.add(line);
				}
				buffReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return lines;
	}

	public static void writeFile(Collection<String> emailTree, String destFile) {
		try {
			FileWriter fw = new FileWriter(new File(destFile));
			BufferedWriter bw = new BufferedWriter(fw);
			for (String email : emailTree) {
				bw.write(email + "\n");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}