/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.marmoush.jobs.email.extractor.EmailFilter
 */
package com.marmoush.jobs;

import com.marmoush.jobs.email.extractor.EmailFilter;
import java.util.List;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        List srcEmails = EmailFilter.readFileToList((String)"ResourcesFiltered/Emails.txt");
        String[] keywords = new String[]{"hr", "career", "cv", "resume", "apply", "employ", "job", "recruit", "work"};
        srcEmails = EmailFilter.getEmailsByKeywords((List)srcEmails, (String[])keywords);
        TreeSet emailTree = EmailFilter.toTreeSet((List)srcEmails);
        EmailFilter.writeTreeToFile((TreeSet)emailTree, (String)"ResourcesFiltered/Emails2.txt");
    }
}
