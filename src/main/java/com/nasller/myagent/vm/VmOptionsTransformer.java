package com.nasller.myagent.vm;

import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class VmOptionsTransformer implements MyTransformer {
	@Override
	public String getHookClassName() {
		return "com/intellij/diagnostic/VMOptions";
	}

	@Override
	public byte[] transform(String className, byte[] classBytes, int order) {
		ClassReader reader = new ClassReader(classBytes);
		ClassNode node = new ClassNode(ASM5);
		reader.accept(node, 0);
		for (MethodNode m : node.methods) {
			if ("getUserOptionsFile".equals(m.name) && "()Ljava/nio/file/Path;".equals(m.desc)) {
				InsnList list = new InsnList();
				list.add(new MethodInsnNode(INVOKESTATIC, "com/nasller/myagent/vm/VmOptionsUtil", "testResult", "()Ljava/lang/Object;", false));
				list.add(new VarInsnNode(ASTORE, 1));
				list.add(new VarInsnNode(ALOAD, 1));
				LabelNode label1 = new LabelNode();
				list.add(new JumpInsnNode(IFNULL, label1));
				list.add(new VarInsnNode(ALOAD, 1));
				list.add(new InsnNode(ARETURN));
				list.add(label1);
				m.instructions.insert(list);
			}
		}
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		node.accept(writer);
		return writer.toByteArray();
	}
}