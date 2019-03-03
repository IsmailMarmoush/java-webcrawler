/*
 * Decompiled with CFR 0_115.
 */
package com.marmoush.jobs.website.extractor;

import com.marmoush.jobs.website.extractor.Website;
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
public class WebsiteFilter {
    public static List<String> filterFishyWebsites(List<String> srcWebsites) {
        ArrayList<String> websites = new ArrayList<String>(srcWebsites.size());
        for (String em : srcWebsites) {
            if (em.contains("gmail") || em.contains("yahoo") || em.contains("hotmail")) continue;
            websites.add(em);
        }
        return websites;
    }

    public static List<String> filterNonWebsites(List<String> srcWebsites) {
        ArrayList<String> websites = new ArrayList<String>(srcWebsites.size());
        for (String em : srcWebsites) {
            if (!Website.isWebsite2(em)) continue;
            websites.add(em);
        }
        return websites;
    }

    public static List<String> readFileToList(String file) {
        ArrayList<String> lines = new ArrayList<String>();
        File websitesFile = new File(file);
        try {
            FileReader fread = new FileReader(websitesFile);
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

    public static List<String> removeNoise(List<String> srcWebsites) {
        ArrayList<String> websites = new ArrayList<String>(srcWebsites.size());
        for (String em : srcWebsites) {
            String[] tokens = (em = em.replaceAll("[,;&\\s]+", "\n")).split("[\\r\\n]+");
            if (tokens.length <= 0) continue;
            String[] arrstring = tokens;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String emToken = arrstring[n2];
                emToken = emToken.trim();
                emToken = emToken.toLowerCase();
                websites.add(emToken);
                ++n2;
            }
        }
        return websites;
    }

    public static TreeSet<String> toTreeSet(List<String> srcWebsites) {
        TreeSet<String> websites = new TreeSet<String>();
        for (String em : srcWebsites) {
            websites.add(em);
        }
        return websites;
    }

    public static void writeTreeToFileAsHTMLLinks(TreeSet<String> emailTree, String destFile) {
        try {
            FileWriter fw = new FileWriter(new File(destFile));
            BufferedWriter bw = new BufferedWriter(fw);
            for (String website : emailTree) {
                bw.write(" <li><a href=\"" + website + "\" target=\"_blank\"> " + website + "</a></li>" + "\n");
            }
            bw.flush();
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeTreeToFile(TreeSet<String> emailTree, String destFile) {
        try {
            FileWriter fw = new FileWriter(new File(destFile));
            BufferedWriter bw = new BufferedWriter(fw);
            for (String website : emailTree) {
                bw.write(String.valueOf(website) + "\n");
            }
            bw.flush();
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
