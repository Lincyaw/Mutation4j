package com.pinjia.mutationagent;

import java.lang.instrument.Instrumentation;

public class AgentMain {
    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("entering premain...");
        instrument(agentOps, inst);
    }

    public static void agentmain(String agentOps, Instrumentation inst) {
        System.out.println("entering agentmain...");
        instrument(agentOps, inst);
    }

    private static void instrument(String agentOps, Instrumentation inst) {
        System.out.println("instrumenting...");
        inst.addTransformer(new AOPTransformer(agentOps));
    }
}

