package com.nasller.agent.util;

import com.janetfilter.core.commons.DebugInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FilterRuleUtil {
	public static File remoteFileLocation;
	private static final Map<String,RuleModel> remoteMap = new HashMap<>();

	public static void put(String jumpServer,RuleModel ruleModel){
		remoteMap.put(jumpServer,ruleModel);
	}

	public static RuleModel get(String jumpServer){
		return remoteMap.get(jumpServer);
	}

	public static byte[] getClassOrDefault(String name,byte[] classBytes){
		if(remoteFileLocation != null){
			File file = new File(remoteFileLocation, name);
			if(file.exists() && file.isFile()){
				try {
					DebugInfo.debug("load " + name);
					return Files.readAllBytes(file.toPath());
				} catch (IOException e) {
					DebugInfo.debug("Wrong get "+name, e);
				}
			}
		}
		return classBytes;
	}

	public static Map<String,RuleModel> getRemoteRules(){
		return new HashMap<>(remoteMap);
	}

	public static class RuleModel{
		private final String instruction;
		private final String dev;
		private final String pro;
		private final String[] devOther;
		private final String[] proOther;

		public RuleModel(String instruction, String dev, String pro,String[] devOther,String[] proOther) {
			this.instruction = instruction;
			this.dev = dev;
			this.pro = pro;
			this.devOther = devOther;
			this.proOther = proOther;
		}

		public String getInstruction() {
			return instruction;
		}

		public String getDev() {
			return dev;
		}

		public String getPro() {
			return pro;
		}

		public String[] getDevOther() {
			return devOther;
		}

		public String[] getProOther() {
			return proOther;
		}
	}
}