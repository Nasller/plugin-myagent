package com.nasller.myagent.remote;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.plugin.MyTransformer;
import com.nasller.agent.util.FilterRuleUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CommandInnerTransformer implements MyTransformer {
	private static byte[] bytes;

	public CommandInnerTransformer(File classPath) {
		if(bytes == null) {
			File file = new File(classPath, "RemoteToolRunProfile$1.class");
			if(file.exists()){
				try (FileInputStream stream = new FileInputStream(file)) {
					DebugInfo.debug("load RemoteToolRunProfile$1.class stream = " + stream);
					bytes = FilterRuleUtil.readInputStream(stream);
				} catch (IOException e) {
					DebugInfo.debug("Wrong get RemoteToolRunProfile$1.class", e);
				}
			}
		}
	}

	@Override
	public String getHookClassName() {
		return "com/jetbrains/plugins/remotesdk/tools/RemoteToolRunProfile$1";
	}

	@Override
	public byte[] transform(String className, byte[] classBytes, int order) {
		return bytes != null?bytes:classBytes;
	}
}