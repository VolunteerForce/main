package com.codepath.volunteerhero.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jan_spidlen on 10/21/17.
 */

public class Topics {
    public static List<String> getAvailableTopics() {
        Set<String> topicsSet = new HashSet<>();
        topicsSet.add("Education");
        topicsSet.add("Human rights");
        topicsSet.add("Local help");
        topicsSet.add("Culture & sports");
        topicsSet.add("Refugees & immigrants");
        topicsSet.add("Health");
        topicsSet.add("Disabled");
        topicsSet.add("Socially deprived");
        topicsSet.add("Elderly people");
        topicsSet.add("Development cooperation");
        topicsSet.add("Animal & environmental protection");
        topicsSet.add("General volunteering");

        List<String> topics = new ArrayList<>();
        topics.addAll(topicsSet);
        Collections.sort(topics);

        return topics;
    }
}
