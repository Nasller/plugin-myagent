package com.nasller.myagent.aes;

import com.janetfilter.core.models.FilterRule;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyFilter {
	private static final Map<String,String> map = new HashMap<>();

	public static void setKeyFilter(List<FilterRule> aesFilter){
		for (FilterRule filterRule : aesFilter) {
			String[] split = filterRule.getRule().split(",");
			map.put(split[0],split[1]);
		}
	}

	public static Object testEquals(byte[] keyBytes) {
		if(keyBytes == null || keyBytes.length == 0 || map.isEmpty()){
			return null;
		}
		String result = map.get(new String(keyBytes, StandardCharsets.UTF_8));
		return result!=null&&result.length()>0?Base64.getDecoder().decode(result):null;
	}
}