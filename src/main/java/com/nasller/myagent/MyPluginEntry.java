package com.nasller.myagent;

import com.janetfilter.core.Environment;
import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;
import com.janetfilter.core.plugin.PluginConfig;
import com.janetfilter.core.plugin.PluginEntry;
import com.nasller.myagent.aes.AESCryptTransformer;
import com.nasller.myagent.aes.KeyFilter;
import com.nasller.myagent.crack.BITransformer;
import com.nasller.myagent.crack.StrTransformer;
import com.nasller.myagent.remote.CommandInnerTransformer;
import com.nasller.myagent.remote.ProcessInnerTransformer;

import java.util.ArrayList;
import java.util.List;

public class MyPluginEntry implements PluginEntry {
    private final List<MyTransformer> transformers = new ArrayList<>();

    @Override
    public void init(Environment environment, PluginConfig pluginConfig) {
        List<FilterRule> enabledCrack = pluginConfig.getBySection("CRACK");
        if(enabledCrack.size() == 1 && enabledCrack.get(0).test("ENABLED")){
            transformers.add(new BITransformer());
            transformers.add(new StrTransformer());
        }
        List<FilterRule> filterRules = pluginConfig.getBySection("REMOTE");
        if(filterRules.size() > 3){
            //1.jumpServerUrl,2.指令,3.默认对应dev资源，4.默认对应pre或pro资源
            transformers.add(new CommandInnerTransformer(filterRules));
            transformers.add(new ProcessInnerTransformer(filterRules));
        }
        List<FilterRule> aesFilter = pluginConfig.getBySection("AES");
        if(aesFilter != null && aesFilter.size() > 0){
            KeyFilter.setKeyFilter(aesFilter);
            transformers.add(new AESCryptTransformer());
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