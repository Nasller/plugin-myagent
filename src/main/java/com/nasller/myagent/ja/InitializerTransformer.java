package com.nasller.myagent.ja;

import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class InitializerTransformer implements MyTransformer {
	@Override
	public String getHookClassName() {
		return "com/janetfilter/core/Initializer";
	}

	@Override
	public byte[] transform(String className, byte[] classBytes, int order) {
		ClassReader reader = new ClassReader(classBytes);
		ClassNode node = new ClassNode(ASM5);
		reader.accept(node, 0);
		for (MethodNode m : node.methods) {
			if ("<init>".equals(m.name) && "()V".equals(m.desc)) {
				InsnList list = new InsnList();
				list.add(new TypeInsnNode(NEW,"java/lang/UnsupportedOperationException"));
				list.add(new InsnNode(DUP));
				list.add(new MethodInsnNode(INVOKESTATIC, "java/lang/UnsupportedOperationException", "<init>", "()V", false));
				list.add(new InsnNode(ATHROW));
				m.instructions.insert(list);
				break;
			}
		}
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(writer);
		return writer.toByteArray();
	}
}