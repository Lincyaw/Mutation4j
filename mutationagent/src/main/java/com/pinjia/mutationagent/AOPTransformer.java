package com.pinjia.mutationagent;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Random;

import org.objectweb.asm.*;

public class AOPTransformer implements ClassFileTransformer {

    private final String className;
    private static final Random random = new Random();

    public AOPTransformer(String className) {
        this.className = className;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals(this.className.replace('.', '/'))) {
            System.out.println("Trigger rewrite for class: " + className);
            return transformClass(classfileBuffer);
        }
        return classfileBuffer;
    }

    private byte[] transformClass(byte[] classfileBuffer) {
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM9, writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM9, mv) {
                    @Override
                    public void visitInsn(int opcode) {
                        // Randomly replace arithmetic operations
                        switch (opcode) {
                            case Opcodes.IADD:
                            case Opcodes.ISUB:
                            case Opcodes.IMUL:
                            case Opcodes.IDIV:
                                int newOpcode = randomArithmeticOpcode();
                                super.visitInsn(newOpcode);
                                break;
                            default:
                                super.visitInsn(opcode);
                                break;
                        }
                    }
                };
            }
        };
        reader.accept(visitor, 0);
        return writer.toByteArray();
    }

    private int randomArithmeticOpcode() {
        switch (random.nextInt(4)) {
            case 0:
                return Opcodes.IADD;
            case 1:
                return Opcodes.ISUB;
            case 2:
                return Opcodes.IMUL;
            case 3:
                return Opcodes.IDIV;
            default:
                return Opcodes.IADD; // Default case to add, though it should never happen.
        }
    }
}