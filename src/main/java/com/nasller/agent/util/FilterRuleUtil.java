package com.nasller.agent.util;

import com.janetfilter.core.commons.DebugInfo;

import java.io.*;
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
			if(file.exists()){
				try (FileInputStream stream = new FileInputStream(file)) {
					DebugInfo.debug("load " + name + " stream = " + stream);
					return FilterRuleUtil.readInputStream(stream);
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

	public static byte[] readInputStream(InputStream inputStream) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		try {
			while ((length = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outStream.toByteArray();
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