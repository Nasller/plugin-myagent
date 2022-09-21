package com.nasller.agent.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterRuleUtil {
	private static final List<RuleModel> ruleList = new ArrayList<>();

	public static void add(RuleModel... ruleModels){
		ruleList.addAll(Arrays.asList(ruleModels));
	}

	public static RuleModel get(int index){
		return index >= 0 && ruleList.size() > index ? ruleList.get(index) : new RuleModel("","");
	}

	public static List<RuleModel> getRuleList(){
		return new ArrayList<>(ruleList);
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