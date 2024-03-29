package com.nasller.myagent.aes;

import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class AESCryptTransformer implements MyTransformer {
	@Override
	public String getHookClassName() {
		return "com/sun/crypto/provider/AESCrypt";
	}

	@Override
	public byte[] transform(String className, byte[] classBytes, int order) {
		ClassReader reader = new ClassReader(classBytes);
		ClassNode node = new ClassNode(ASM5);
		reader.accept(node, 0);
		for (MethodNode m : node.methods) {
			if ("decryptBlock".equals(m.name) && "([BI[BI)V".equals(m.desc)) {
				InsnList list = new InsnList();
				list.add(new VarInsnNode(ALOAD, 0));
				list.add(new FieldInsnNode(GETFIELD, getHookClassName(), "lastKey", "[B"));
				list.add(new MethodInsnNode(INVOKESTATIC, "com/nasller/myagent/aes/KeyFilter", "testEquals", "([B)Ljava/lang/Object;", false));
				list.add(new VarInsnNode(ASTORE, 5));
				LabelNode label1 = new LabelNode();
				list.add(new VarInsnNode(ALOAD, 5));
				list.add(new JumpInsnNode(IFNULL, label1));
				list.add(new VarInsnNode(ALOAD, 0));
				list.add(new VarInsnNode(ALOAD, 5));
				list.add(new TypeInsnNode(CHECKCAST, "[B"));
				list.add(new VarInsnNode(ASTORE, 1));
				list.add(label1);
				m.instructions.insert(list);
				break;
			}
		}
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(writer);
		return writer.toByteArray();
	}
}