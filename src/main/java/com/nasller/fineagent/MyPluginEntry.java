package com.nasller.fineagent;

import com.janetfilter.core.Environment;
import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;
import com.janetfilter.core.plugin.PluginEntry;

import java.util.ArrayList;
import java.util.List;

public class MyPluginEntry implements PluginEntry {
    private final List<MyTransformer> transformers = new ArrayList<>();

    @Override
    public void init(Environment environment, List<FilterRule> filterRules) {
        transformers.add(new BITransformer());
        transformers.add(new StrTransformer());
    }

    @Override
    public String getName() {
        return "FindAgent";
    }

    @Override
    public String getAuthor() {
        return "Nasller";
    }

    @Override
    public String getVersion() {
        return "v1.0.2";
    }

    @Override
    public String getDescription() {
        return "FindAgent plugin for ja-netfilter";
    }

    @Override
    public List<MyTransformer> getTransformers() {
        return transformers;
    }
}