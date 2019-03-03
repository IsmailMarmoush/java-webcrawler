/*
 * Decompiled with CFR 0_115.
 */
package com.marmoush.jobs.email.extractor;

import com.marmoush.jobs.email.extractor.Email;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EmailFilter {
    public static /* varargs */ List<String> getEmailsByKeywords(List<String> srcEmails, String ... keywords) {
        ArrayList<String> emails = new ArrayList<String>(srcEmails.size());
        for (String em : srcEmails) {
            String[] arrstring = keywords;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String word = arrstring[n2];
                if (em.split("@")[0].contains(word)) {
                    emails.add(em);
                }
                ++n2;
            }
        }
        return emails;
    }

    public static List<String> filterFishyEmails(List<String> srcEmails) {
        ArrayList<String> emails = new ArrayList<String>(srcEmails.size());
        for (String em : srcEmails) {
            if (em.contains("gmail") || em.contains("yahoo") || em.contains("hotmail")) continue;
            emails.add(em);
        }
        return emails;
    }

    public static List<String> filterNonEmails(List<String> srcEmails) {
        ArrayList<String> emails = new ArrayList<String>(srcEmails.size());
        for (String em : srcEmails) {
            if (!Email.isEmail(em)) continue;
            emails.add(em);
        }
        return emails;
    }

    public static List<String> readFileToList(String file) {
        ArrayList<String> lines = new ArrayList<String>();
        File emailsFile = new File(file);
        try {
            FileReader fread = new FileReader(emailsFile);
            BufferedReader buffReader = new BufferedReader(fread);
            String line = " ";
            while ((line = buffReader.readLine()) != null) {
                lines.add(line);
            }
            buffReader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static List<String> removeNoise(List<String> srcEmails) {
        ArrayList<String> emails = new ArrayList<String>(srcEmails.size());
        for (String em : srcEmails) {
            String[] tokens = (em = em.replaceAll("[,;:&\\s]+", "\n")).split("[\\r\\n]+");
            if (tokens.length <= 0) continue;
            String[] arrstring = tokens;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String emToken = arrstring[n2];
                emToken = emToken.trim();
                emToken = emToken.toLowerCase();
                emails.add(emToken);
                ++n2;
            }
        }
        return emails;
    }

    public static TreeSet<String> toTreeSet(List<String> srcEmails) {
        TreeSet<String> emails = new TreeSet<String>();
        for (String em : srcEmails) {
            emails.add(em);
        }
        return emails;
    }

    public static void writeTreeToFile(TreeSet<String> emailTree, String destFile) {
        try {
            FileWriter fw = new FileWriter(new File(destFile));
            BufferedWriter bw = new BufferedWriter(fw);
            for (String email : emailTree) {
                bw.write(String.valueOf(email) + "\n");
            }
            bw.flush();
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
