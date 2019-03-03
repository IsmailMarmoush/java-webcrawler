/*
 * Decompiled with CFR 0_115.
 */
package com.marmoush.jobs.website.extractor;

import com.marmoush.jobs.website.extractor.WebsiteFilter;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class MergeTwoWebsiteFiles {
    public static void main(String[] args) {
        List<String> websites = WebsiteFilter.readFileToList("1allWebsites.txt");
        List<String> websites2 = WebsiteFilter.readFileToList("1allWebsitesFromEmails.txt");
        TreeSet<String> tree = WebsiteFilter.toTreeSet(websites);
        tree.addAll(WebsiteFilter.toTreeSet(websites2));
        WebsiteFilter.writeTreeToFile(tree, "allAndFinalWebsites.txt");
    }
}
