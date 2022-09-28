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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static File createJar() {
        File jarFile = null;
        try {
            jarFile = File.createTempFile("classTrans-", ".jar", new File(System.getProperty("java.io.tmpdir")));
            JarOutputStream out = new JarOutputStream(new FileOutputStream(jarFile));
            String[] jarNames = new String[]{
                    "cn/langpy/kotime/RedefineClass.class",
                    "cn/langpy/kotime/ClassTransformer.class",
                    "META-INF/maven/cn.langpy/ko-time-retrans/pom.xml",
                    "META-INF/maven/cn.langpy/ko-time-retrans/pom.properties",
                    "MANIFEST.MF",
                    "META-INF/MANIFEST.MF",
            };
            String[] jarFilePaths = new String[]{
                    "retrans/RedefineClass.class",
                    "retrans/ClassTransformer.class",
                    "retrans/META-INF/maven/cn.langpy/ko-time-retrans/pom.xml",
                    "retrans/META-INF/maven/cn.langpy/ko-time-retrans/pom.properties",
                    "retrans/MANIFEST.MF",
                    "retrans/META-INF/MANIFEST.MF"
            };
            buildElement(out, jarNames, jarFilePaths);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jarFile;
    }

    private static void buildElement(JarOutputStream out, String[] fileNames, String[] filePaths) throws IOException {
        for (int i = 0; i < fileNames.length; i++) {
            ClassPathResource classPathResource = new ClassPathResource(filePaths[i]);
            addJarFile(out, fileNames[i], classPathResource.getInputStream());
        }
    }


    private static void addJarFile(JarOutputStream out, String packagePath, InputStream in) {
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
