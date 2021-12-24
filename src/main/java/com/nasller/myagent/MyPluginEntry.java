package com.nasller.myagent;

import com.janetfilter.core.Environment;
import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;
import com.janetfilter.core.plugin.PluginEntry;
import com.nasller.myagent.crack.BITransformer;
import com.nasller.myagent.crack.StrTransformer;
import com.nasller.myagent.remote.CommandInnerTransformer;
import com.nasller.myagent.remote.ProcessInnerTransformer;

import java.util.ArrayList;
import java.util.List;

public class MyPluginEntry implements PluginEntry {
    private final List<MyTransformer> transformers = new ArrayList<>();

    @Override
    public void init(Environment environment, List<FilterRule> filterRules) {
        transformers.add(new BITransformer());
        transformers.add(new StrTransformer());
        if(filterRules != null && filterRules.size() > 4 && filterRules.get(0).test("REMOTE")){
            //1.jumpServerUrl,2.指令,3.默认对应dev资源，4.默认对应pre或pro资源
            List<FilterRule> rules = filterRules.subList(1, 5);
            transformers.add(new CommandInnerTransformer(rules));
            transformers.add(new ProcessInnerTransformer(rules));
        }
    }

    @Override
    public String getName() {
        return "MyAgent";
    }

    @Override
    public String getAuthor() {
        return "Nasller";
    }

    @Override
    public String getVersion() {
        return "v1.0.0";
    }

    @Override
    public String getDescription() {
        return "MyAgent plugin for ja-netfilter";
    }

    @Override
    public List<MyTransformer> getTransformers() {
        return transformers;
    }
}