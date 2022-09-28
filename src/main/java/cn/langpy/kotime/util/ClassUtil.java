package cn.langpy.kotime.util;

import net.bytebuddy.agent.VirtualMachine;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.logging.Logger;

/**
 * zhangchang
 */
public class ClassUtil {

    private static Logger log = Logger.getLogger(ClassUtil.class.toString());

    public static void updateClass(String jarPath, String className, String classPath) {
        try {
            VirtualMachine virtualMachine = VirtualMachine.ForHotSpot.attach(Context.getPid());
            virtualMachine.loadAgent(jarPath, className + "-" + classPath);
            Thread.sleep(500);
            virtualMachine.detach();
        } catch (IOException e) {
            e.printStackTrace();
            log.severe("Fail to update class:" + className);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static File createJar() {
        File jarFile = null;
        try {
            jarFile = File.createTempFile("classTrans-", ".jar", new File(System.getProperty("java.io.tmpdir")));
            JarOutputStream out = new JarOutputStream(new FileOutputStream(jarFile));
            ClassPathResource redefineClassPath = new ClassPathResource("retrans/RedefineClass.class");
            ClassPathResource classTransformerPath = new ClassPathResource("retrans/ClassTransformer.class");
            ClassPathResource pomPath = new ClassPathResource("retrans/META-INF/maven/cn.langpy/ko-time-retrans/pom.xml");
            ClassPathResource propertyPath = new ClassPathResource("retrans/META-INF/maven/cn.langpy/ko-time-retrans/pom.properties");
            ClassPathResource manifestPath = new ClassPathResource("retrans/MANIFEST.MF");
            ClassPathResource manifestInPath = new ClassPathResource("retrans/META-INF/MANIFEST.MF");
            addClassFile(out, "cn/langpy/kotime/RedefineClass.class", redefineClassPath.getInputStream());
            addClassFile(out, "cn/langpy/kotime/ClassTransformer.class", classTransformerPath.getInputStream());
            addClassFile(out, "META-INF/maven/cn.langpy/ko-time-retrans/pom.xml", pomPath.getInputStream());
            addClassFile(out, "META-INF/maven/cn.langpy/ko-time-retrans/pom.properties", propertyPath.getInputStream());
            addClassFile(out, "MANIFEST.MF", manifestPath.getInputStream());
            addClassFile(out, "META-INF/MANIFEST.MF", manifestInPath.getInputStream());
            out.closeEntry();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jarFile;
    }


    public static void addClassFile(JarOutputStream out, String packagePath, InputStream in) {
        try {
            out.putNextEntry(new JarEntry(packagePath));
            byte[] buffer = new byte[1024];
            int n = in.read(buffer);
            while (n != -1) {
                out.write(buffer, 0, n);
                n = in.read(buffer);
            }
            in.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
