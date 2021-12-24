package com.nasller.myagent;

import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.util.List;

import static jdk.internal.org.objectweb.asm.Opcodes.ACC_PUBLIC;

public class ProcessInnerTransformer implements MyTransformer {
	private final List<FilterRule> configList;
	public ProcessInnerTransformer(List<FilterRule> configList){
		this.configList = configList;
	}

	@Override
	public String getHookClassName() {
		return "com/jetbrains/plugins/remotesdk/tools/RemoteToolRunProfile$1$1";
	}

	@Override
	public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
		ClassReader reader = new ClassReader(classBytes);
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		reader.accept(writer, 0);
		MethodVisitor methodVisitor = writer.visitMethod(ACC_PUBLIC, "processWillTerminate", "(Lcom/intellij/execution/process/ProcessEvent;Z)V", null, null);
		RemoteAsm.visitProcessTerminated(methodVisitor,this.configList);
		return writer.toByteArray();
	}
}