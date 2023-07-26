package com.nasller.myagent;

import com.janetfilter.core.Environment;
import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;
import com.janetfilter.core.plugin.PluginConfig;
import com.janetfilter.core.plugin.PluginEntry;
import com.nasller.agent.util.FilterRuleUtil;
import com.nasller.agent.util.FilterRuleUtil.RuleModel;
import com.nasller.myagent.aes.AESCryptTransformer;
import com.nasller.myagent.aes.KeyFilter;
import com.nasller.myagent.ja.InitializerTransformer;
import com.nasller.myagent.remote.CommandInnerTransformer;
import com.nasller.myagent.remote.ProcessInnerTransformer;
import com.nasller.myagent.remote.SshEnvironmentRelativePathsTransformer;
import com.nasller.myagent.vm.VmOptionsTransformer;
import com.nasller.myagent.vm.VmOptionsUtil;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.nio.file.Paths;
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
        List<FilterRule> aesFilter = pluginConfig.getBySection("AES");
        if(aesFilter != null && aesFilter.size() > 0){
            KeyFilter.setKeyFilter(aesFilter);
            transformers.add(new AESCryptTransformer());
        }
        List<FilterRule> vmFilter = pluginConfig.getBySection("VMOPTIONS");
        if(vmFilter != null && vmFilter.size() > 0){
            VmOptionsUtil.setFakeFile(Paths.get(vmFilter.get(0).getRule()));
            transformers.add(new VmOptionsTransformer());
        }
//        transformers.add(new SshVolumeBaseTransformer());
        transformers.add(new SshEnvironmentRelativePathsTransformer());
        transformers.add(new InitializerTransformer());
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

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}