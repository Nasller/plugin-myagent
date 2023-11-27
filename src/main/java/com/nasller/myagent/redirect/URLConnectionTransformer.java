package com.nasller.myagent.redirect;

import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.List;

public class URLConnectionTransformer implements MyTransformer {
	private final List<FilterRule> rules;
	public URLConnectionTransformer(List<FilterRule> rules) {
		this.rules = rules;
	}

	@Override
	public String getHookClassName() {
		return "java/net/URLConnection";
	}

	@Override
	public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
		RedirectUrlUtil.setRules(rules);
		ClassReader reader = new ClassReader(classBytes);
		ClassNode node = new ClassNode(Opcodes.ASM5);
		reader.accept(node, 0);
		for (MethodNode mn : node.methods) {
			if ("<init>".equals(mn.name) && "(Ljava/net/URL;)V".equals(mn.desc)) {
				InsnList list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 1));
				list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/nasller/myagent/redirect/RedirectUrlUtil", "testURL", "(Ljava/net/URL;)Ljava/net/URL;", false));
				list.add(new VarInsnNode(Opcodes.ASTORE,1));
				mn.instructions.insert(list);
			}
		}
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		node.accept(writer);
		return writer.toByteArray();
	}
}