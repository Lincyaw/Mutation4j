package com.pinjia.attach;

import com.sun.tools.attach.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class InteractiveAgentAttacher {
    public static void main(String[] args) {
        // 获取所有正在运行的 JVM 列表
        List<VirtualMachineDescriptor> jvms;
        try {
            jvms = VirtualMachine.list();
        } catch (Exception e) {
            System.err.println("Failed to retrieve JVM list.");
            e.printStackTrace();
            return;
        }

        // 打印 JVM 列表
        System.out.println("Running JVMs:");
        for (int i = 0; i < jvms.size(); i++) {
            VirtualMachineDescriptor jvm = jvms.get(i);
            System.out.println((i + 1) + ". " + jvm.displayName());
        }

        // 提示用户选择要 attach 的 JVM
        System.out.print("Enter the number of the JVM to attach: ");
        Scanner scanner = new Scanner(System.in);
        int jvmIndex = scanner.nextInt() - 1;

        if (jvmIndex >= 0 && jvmIndex < jvms.size()) {
            VirtualMachineDescriptor targetJvm = jvms.get(jvmIndex);
            System.out.println("Selected JVM: "+targetJvm.displayName());
            String targetPid = targetJvm.id();

            // 提示用户输入要加载的 agent 路径
            System.out.print("Enter the path to the agent JAR file: ");
            String agentJar = scanner.next();

            VirtualMachine vm = null;
            try {
                System.out.printf("Loading agent from: %s, target pid: %s\n", agentJar, targetPid);
                // 创建 VirtualMachine 实例并 Attach
                vm = VirtualMachine.attach(targetPid);
                vm.loadAgent(agentJar);
                System.out.println("Agent attached successfully. Press Ctrl+C to detach.");

                // 等待用户按 Ctrl+C 退出
                VirtualMachine finalVm = vm;
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        if (finalVm != null) {
                            finalVm.detach();
                            System.out.println("Agent detached successfully.");
                        }
                    } catch (IOException e) {
                        System.err.println("Failed to detach the agent.");
                        e.printStackTrace();
                    }
                }));

                while (true) {
                    // 无限循环，直到用户按 Ctrl+C 退出
                }

            } catch (IOException | AgentLoadException | AgentInitializationException | AttachNotSupportedException e) {
                System.err.println("An error occurred while attaching the agent or loading the JVM:");
                System.err.println("Error message: " + e.getMessage()); // 打印异常消息
                e.printStackTrace(); // 打印完整的堆栈跟踪
            } finally {
                // 关闭 VirtualMachine
                try {
                    if (vm != null) {
                        vm.detach();
                    }
                } catch (IOException e) {
                    System.err.println("Failed to detach the VM properly.");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Invalid JVM selection.");
        }
    }
}