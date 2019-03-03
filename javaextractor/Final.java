/*
 * Decompiled with CFR 0_115.
 */
package com.marmoush.jobs.website.extractor;

import com.marmoush.jobs.website.extractor.WebsiteFilter;
import java.util.List;
import java.util.TreeSet;

public class Final {
    public static void main(String[] args) {
        List<String> srcWebsites = WebsiteFilter.readFileToList("allAndFinalWebsites.txt");
        TreeSet<String> emailTree = WebsiteFilter.toTreeSet(srcWebsites);
        emailTree.remove("http://www.abnit.com");
        emailTree.remove("http://www.adamtel.com");
        emailTree.remove("http://www.almosoft.com");
        emailTree.remove("http://www.alshayaegypt.com");
        emailTree.remove("http://www.alsuwaidi.com.sa");
        emailTree.remove("http://www.alwadicom.com");
        emailTree.remove("http://www.amcham.com.eg");
        WebsiteFilter.writeTreeToFile(emailTree, "allAndFinalWebsites.txt");
    }
}
