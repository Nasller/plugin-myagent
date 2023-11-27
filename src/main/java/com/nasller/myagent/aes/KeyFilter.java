package com.nasller.myagent.aes;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class KeyFilter {
	private static final Map<String,Data> map = new HashMap<>();

	public static void setKeyFilter(List<FilterRule> aesFilter){
		for (FilterRule filterRule : aesFilter) {
			String[] split = filterRule.getRule().split(",");
			TypeEnum type = TypeEnum.valueOf(split[0]);
			String key = split[1];
			if(key != null && key.length() > 1){
				String encryptData;
				if(type == TypeEnum.NONE){
					map.put(key,new Data(split[2],type));
				}else if((encryptData = TypeEnum.aesEncrypt(key, type)) != null){
					map.put(filterRule.getRule(), new Data(encryptData,type));
				}
			}
		}
	}

	public static Object testEquals(byte[] keyBytes) {
		if(keyBytes == null || keyBytes.length == 0 || map.isEmpty()){
			return null;
		}
		String key = new String(keyBytes, StandardCharsets.UTF_8);
		Data result = map.get(key);
		if(result != null){
			return result.getTypeEnum().isForceRefresh()?TypeEnum.aesEncrypt(key, result.getTypeEnum()):result.getEncryptData();
		}
		return null;
	}

	static class Data{
		private final String encryptData;
		private final TypeEnum typeEnum;

		public Data(String encryptData, TypeEnum typeEnum) {
			this.encryptData = encryptData;
			this.typeEnum = typeEnum;
		}

		public String getEncryptData() {
			return encryptData;
		}

		public TypeEnum getTypeEnum() {
			return typeEnum;
		}
	}

	enum TypeEnum{
		NONE(false,()->null),
		LEET_CODE(true,()->System.currentTimeMillis()+":"+Boolean.FALSE),
		;
		private final boolean forceRefresh;
		private final Supplier<String> encryptData;

		TypeEnum(boolean forceRefresh,Supplier<String> encryptData) {
			this.forceRefresh = forceRefresh;
			this.encryptData = encryptData;
		}

		public Supplier<String> getEncryptData() {
			return encryptData;
		}

		public boolean isForceRefresh() {
			return forceRefresh;
		}

		private static String aesEncrypt(String stringKey, TypeEnum type){
			try {
				Key key = new SecretKeySpec(stringKey.getBytes(StandardCharsets.UTF_8), "AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(1, key);
				byte[] var7 = cipher.doFinal(type.getEncryptData().get().getBytes(StandardCharsets.UTF_8));
				return Base64.getEncoder().encodeToString(var7);
			}catch (Exception e){
				DebugInfo.error("编码失败",e);
				return null;
			}
		}
	}
}