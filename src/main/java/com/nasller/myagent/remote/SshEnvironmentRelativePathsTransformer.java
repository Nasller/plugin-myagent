package com.nasller.myagent.remote;

import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class SshEnvironmentRelativePathsTransformer implements MyTransformer {
	@Override
	public String getHookClassName() {
		return "com/jetbrains/plugins/remotesdk/target/ssh/target/SshEnvironment$uploadWithRsync$relativePaths$1";
	}

	@Override
	public byte[] transform(String className, byte[] classBytes, int order) {
		ClassReader reader = new ClassReader(classBytes);
		ClassNode node = new ClassNode(ASM5);
		reader.accept(node, 0);
		for (MethodNode method : node.methods) {
			if("invoke".equals(method.name) && "(Lkotlin/Pair;)Ljava/lang/String;".equals(method.desc)){
				method.instructions.clear();
				method.visitCode();
				Label label0 = new Label();
				method.visitLabel(label0);
				method.visitLineNumber(168, label0);
				method.visitVarInsn(ALOAD, 0);
				method.visitFieldInsn(GETFIELD, "com/jetbrains/plugins/remotesdk/target/ssh/target/SshEnvironment$uploadWithRsync$relativePaths$1", "$localSharedPath", "Ljava/nio/file/Path;");
				method.visitVarInsn(ALOAD, 1);
				method.visitMethodInsn(INVOKEVIRTUAL, "kotlin/Pair", "getFirst", "()Ljava/lang/Object;", false);
				method.visitTypeInsn(CHECKCAST, "com/intellij/execution/target/TargetEnvironment$UploadableVolume");
				method.visitMethodInsn(INVOKEINTERFACE, "com/intellij/execution/target/TargetEnvironment$UploadableVolume", "getLocalRoot", "()Ljava/nio/file/Path;", true);
				method.visitVarInsn(ALOAD, 1);
				method.visitMethodInsn(INVOKEVIRTUAL, "kotlin/Pair", "getSecond", "()Ljava/lang/Object;", false);
				method.visitTypeInsn(CHECKCAST, "java/lang/String");
				method.visitMethodInsn(INVOKEINTERFACE, "java/nio/file/Path", "resolve", "(Ljava/lang/String;)Ljava/nio/file/Path;", true);
				method.visitMethodInsn(INVOKEINTERFACE, "java/nio/file/Path", "relativize", "(Ljava/nio/file/Path;)Ljava/nio/file/Path;", true);
				method.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
				Label label1 = new Label();
				method.visitJumpInsn(GOTO, label1);
				method.visitLabel(label1);
				method.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/String"});
				method.visitVarInsn(ASTORE, 3);
				method.visitVarInsn(ALOAD, 1);
				method.visitMethodInsn(INVOKEVIRTUAL, "kotlin/Pair", "getFirst", "()Ljava/lang/Object;", false);
				method.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
				method.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
				method.visitLdcInsn("NewSshVolume");
				method.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false);
				Label label2 = new Label();
				method.visitJumpInsn(IFEQ, label2);
				method.visitVarInsn(ALOAD, 3);
				method.visitVarInsn(ALOAD, 1);
				method.visitMethodInsn(INVOKEVIRTUAL, "kotlin/Pair", "getFirst", "()Ljava/lang/Object;", false);
				method.visitTypeInsn(CHECKCAST, "com/jetbrains/plugins/remotesdk/target/ssh/target/NewSshVolume");
				method.visitMethodInsn(INVOKEVIRTUAL, "com/jetbrains/plugins/remotesdk/target/ssh/target/NewSshVolume", "getPlatform", "()Lcom/intellij/execution/Platform;", false);
				method.visitFieldInsn(GETFIELD, "com/intellij/execution/Platform", "fileSeparator", "C");
				method.visitMethodInsn(INVOKESTATIC, "com/intellij/openapi/util/io/FileUtilRt", "toSystemDependentName", "(Ljava/lang/String;C)Ljava/lang/String;", false);
				method.visitInsn(ARETURN);
				method.visitLabel(label2);
				method.visitFrame(F_APPEND, 2, new Object[]{TOP, "java/lang/String"}, 0, null);
				method.visitVarInsn(ALOAD, 3);
				method.visitInsn(ARETURN);
				Label label3 = new Label();
				method.visitLabel(label3);
				method.visitFrame(F_FULL, 2, new Object[]{"com/jetbrains/plugins/remotesdk/target/ssh/target/SshEnvironment$uploadWithRsync$relativePaths$1", "kotlin/Pair"}, 1, new Object[]{"java/lang/Throwable"});
				method.visitInsn(NOP);
				method.visitInsn(ATHROW);
				method.visitLocalVariable("this", "Lcom/jetbrains/plugins/remotesdk/target/ssh/target/SshEnvironment$uploadWithRsync$relativePaths$1;", null, label0, label1, 0);
				method.visitLocalVariable("it", "Lkotlin/Pair;", null, label0, label1, 1);
				method.visitMaxs(5, 4);
				method.visitEnd();
				break;
			}
		}
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		node.accept(writer);
		return writer.toByteArray();
	}
}