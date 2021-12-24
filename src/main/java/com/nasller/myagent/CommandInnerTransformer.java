package com.nasller.myagent;

import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.List;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

public class CommandInnerTransformer implements MyTransformer {
	private final List<FilterRule> configList;
	public CommandInnerTransformer(List<FilterRule> filterRules){
		this.configList = filterRules;
	}

	@Override
	public String getHookClassName() {
		return "com/jetbrains/plugins/remotesdk/tools/RemoteToolRunProfile$1";
	}

	@Override
	public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
		ClassReader reader = new ClassReader(classBytes);
		ClassNode node = new ClassNode(ASM5);
		reader.accept(node, 0);
		for (MethodNode methodNode : node.methods) {
			if ("startProcess".equals(methodNode.name) && "()Lcom/intellij/execution/process/ProcessHandler;".equals(methodNode.desc)) {
				methodNode.instructions.clear();
				RemoteEditAsm.visitStartProcess(methodNode,this.configList);
			}
		}
		ClassWriter writer = new ClassWriter(0);
		node.accept(writer);
		return writer.toByteArray();
	}
}