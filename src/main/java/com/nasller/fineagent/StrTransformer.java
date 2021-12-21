package com.nasller.fineagent;

import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class StrTransformer implements MyTransformer {
    @Override
    public String getHookClassName() {
        return "java/lang/String";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);
        for (MethodNode m : node.methods) {
            if ("<init>".equals(m.name) && "([CIILjava/lang/Void;)V".equals(m.desc)) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/nasller/crack/idea/agent/StringFilter", "testEquals", "([C)Ljava/lang/Object;", false));
                list.add(new VarInsnNode(ASTORE, 5));
                list.add(new VarInsnNode(ALOAD, 5));
                LabelNode label1 = new LabelNode();
                list.add(new JumpInsnNode(IFNULL, label1));
                list.add(new VarInsnNode(ALOAD, 5));
                list.add(new VarInsnNode(ASTORE,1));
                list.add(label1);
                m.instructions.insert(list);
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}