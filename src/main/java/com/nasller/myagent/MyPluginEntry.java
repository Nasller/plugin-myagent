package com.nasller.myagent;

import com.janetfilter.core.Environment;
import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;
import com.janetfilter.core.plugin.PluginConfig;
import com.janetfilter.core.plugin.PluginEntry;
import com.nasller.myagent.aes.AESCryptTransformer;
import com.nasller.myagent.redirect.URLConnectionTransformer;
import com.nasller.myagent.remote.CommandInnerTransformer;
import com.nasller.myagent.remote.ProcessInnerTransformer;
import com.nasller.myagent.remote.SshEnvironmentRelativePathsTransformer;
import com.thirdpart.janetfilter.plugins.my.agent.util.FilterRuleUtil;
import com.thirdpart.janetfilter.plugins.my.agent.util.FilterRuleUtil.RuleModel;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyPluginEntry implements PluginEntry {
    private final List<MyTransformer> transformers = new ArrayList<>();
    private static volatile Instrumentation instrumentation;

    @Override
    public void init(Environment environment, PluginConfig pluginConfig) {
        instrumentation = environment.getInstrumentation();
        List<FilterRule> filterRules = pluginConfig.getBySection("REMOTE");
        if(filterRules.size() > 1){
            String classPathRule = filterRules.remove(0).getRule();
            File classPath = new File(classPathRule);
            if(classPath.exists() && classPath.isDirectory()){
                DebugInfo.debug("load remote classPath " + classPath);
                FilterRuleUtil.remoteFileLocation = classPath;
                transformers.add(new CommandInnerTransformer());
                transformers.add(new ProcessInnerTransformer());
            }
            //1.jumpServerUrl,2.指令,3.默认对应dev资源，4.默认对应pre或pro资源，5.其他指令
            for (FilterRule filterRule : filterRules) {
                String[] split = filterRule.getRule().split(",");
                if(split.length > 3){
                    FilterRuleUtil.put(split[0],new RuleModel(split[1],split[2],split[3],
                            Arrays.stream(split).skip(4).filter(o->o.startsWith("dev:")).map(o->o.replaceFirst("dev:","")).toArray(String[]::new),
                            Arrays.stream(split).skip(4).filter(o->o.startsWith("pro:")).map(o->o.replaceFirst("pro:","")).toArray(String[]::new))
                    );
                }
            }
        }
        filterRules = pluginConfig.getBySection("AES");
        if(!filterRules.isEmpty()){
            transformers.add(new AESCryptTransformer());
        }
        filterRules = pluginConfig.getBySection("URL");
        if(!filterRules.isEmpty()){
            transformers.add(new URLConnectionTransformer(filterRules));
        }
        transformers.add(new SshEnvironmentRelativePathsTransformer());
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
    public String getDescription() {
        return "MyAgent plugin for ja-netfilter";
    }

    @Override
    public List<MyTransformer> getTransformers() {
        return transformers;
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}