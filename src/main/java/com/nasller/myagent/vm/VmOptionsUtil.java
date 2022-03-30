package com.nasller.myagent.vm;

import com.nasller.myagent.MyPluginEntry;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VmOptionsUtil {
	private static Class<?> pluginClassLoad;
	private static volatile boolean once = true;
	private static Path fakeFile;
	private static Map<String,Boolean> fakeResult;

	public static Object testResult(){
		init();
		if(pluginClassLoad != null){
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			if(stackTrace.length > 0){
				StackTraceElement element = stackTrace[3];
				return getPathByClassName(element.getClassName());
			}
		}
		return null;
	}

	private static void  init(){
		if(once){
			try {
				pluginClassLoad = Thread.currentThread().getContextClassLoader().loadClass("com.intellij.ide.plugins.cl.PluginClassLoader");
			}catch (ClassNotFoundException e){
				pluginClassLoad = null;
			}
			once = false;
		}
	}

	private static Path getPathByClassName(String className) {
		if (className != null && !className.isEmpty()) {
			Boolean fake = fakeResult.computeIfAbsent(className,key->{
				Class<?>[] loadedClasses = MyPluginEntry.getInstrumentation().getAllLoadedClasses();
				if(loadedClasses != null && loadedClasses.length > 0){
					return Arrays.stream(loadedClasses)
							.anyMatch(o -> o.getName().equals(className) && pluginClassLoad.isInstance(o.getClassLoader()));
				}
				return false;
			});
			return fake ? fakeFile : null;
		}
		return null;
	}

	public static void setFakeFile(Path fakeFile) {
		fakeResult = new HashMap<>();
		VmOptionsUtil.fakeFile = fakeFile;
	}
}