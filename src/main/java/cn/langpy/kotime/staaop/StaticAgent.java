package cn.langpy.kotime.staaop;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class StaticAgent {
    public static void agentmain(String agentArgs, Instrumentation inst){
        System.out.println("修改静态方法");
        /*true 必须  */
        inst.addTransformer(new AgentHandler(),true);
        Class[] classes = inst.getAllLoadedClasses();
        for (Class clazz : classes) {
            if (clazz.getName().equals("com.example.demo.controller.Index2Controller")) {
                try {
                    inst.retransformClasses(clazz);
                } catch (UnmodifiableClassException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
