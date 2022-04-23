package cn.langpy.kotime.staaop;


import cn.langpy.kotime.util.Context;
import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class AgentHandler implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/",".");
        /**/

            try {
                CtClass ctClass= ClassPool.getDefault().get(className);
                CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
                for (CtMethod method : declaredMethods) {
                    if (Modifier.isStatic(method.getModifiers())) {
                        method.insertBefore("System.out.println(\"前置--\");");
                        method.insertAfter("System.out.println(\"后置--\");");
                    }
                }
                ctClass.detach();
                return ctClass.toBytecode();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return classfileBuffer;
    }
}
