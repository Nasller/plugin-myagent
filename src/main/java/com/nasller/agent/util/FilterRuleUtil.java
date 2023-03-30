package com.nasller.agent.util;

import com.janetfilter.core.commons.DebugInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterRuleUtil {
	public static File remoteFileLocation;
	private static final List<RuleModel> remoteRules = new ArrayList<>();

	public static void add(RuleModel... ruleModels){
		remoteRules.addAll(Arrays.asList(ruleModels));
	}

	public static RuleModel get(int index){
		return index >= 0 && remoteRules.size() > index ? remoteRules.get(index) : new RuleModel("","");
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

	public static List<RuleModel> getRemoteRules(){
		return new ArrayList<>(remoteRules);
	}

	public static class RuleModel{
		private String ruleType;
		private String rule;

		public RuleModel(String ruleType, String rule) {
			this.ruleType = ruleType;
			this.rule = rule;
		}

		public String getRuleType() {
			return ruleType;
		}

		public void setRuleType(String ruleType) {
			this.ruleType = ruleType;
		}

		public String getRule() {
			return rule;
		}

		public void setRule(String rule) {
			this.rule = rule;
		}
	}
}