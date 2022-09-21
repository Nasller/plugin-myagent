package com.nasller.myagent.remote;

import com.janetfilter.core.plugin.MyTransformer;
import com.nasller.agent.util.FilterRuleUtil;

public class CommandInnerTransformer implements MyTransformer {
	@Override
	public String getHookClassName() {
		return "com/jetbrains/plugins/remotesdk/tools/RemoteToolRunProfile$1";
	}

	@Override
	public byte[] transform(String className, byte[] classBytes, int order) {
		return FilterRuleUtil.getClassOrDefault("RemoteToolRunProfile$1.class",classBytes);
	}
}