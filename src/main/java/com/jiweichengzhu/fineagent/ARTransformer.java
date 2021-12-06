package com.jiweichengzhu.fineagent;

import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class ARTransformer implements MyTransformer {
    @Override
    public String getHookClassName() {
        return "java/util/Arrays";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);

        for (MethodNode m : node.methods) {
            if ("equals".equals(m.name) && "([B[B)Z".equals(m.desc)) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/jiweichengzhu/fineagent/ArraysFilter", "testEquals", "([B[B)Ljava/lang/Object;", false));
                list.add(new VarInsnNode(ASTORE, 2));
                list.add(new InsnNode(ACONST_NULL));
                list.add(new VarInsnNode(ALOAD, 2));

                LabelNode label1 = new LabelNode();
                list.add(new JumpInsnNode(IF_ACMPEQ, label1));
                list.add(new InsnNode(ICONST_1));
                list.add(new InsnNode(IRETURN));
                list.add(label1);

                m.instructions.insert(list);
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);

        return writer.toByteArray();
    }
}
