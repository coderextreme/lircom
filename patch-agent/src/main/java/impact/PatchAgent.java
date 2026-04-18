package impact;

import java.lang.instrument.*;
import java.security.ProtectionDomain;
import javassist.*;

public class PatchAgent {
    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                    Class<?> classBeingRedefined, ProtectionDomain pd,
                    byte[] bytes) {
                if (!"com/jogamp/nativewindow/awt/JAWTWindow".equals(className))
                    return null;
                try {
                    ClassPool pool = ClassPool.getDefault();
                    CtClass cc = pool.makeClass(new java.io.ByteArrayInputStream(bytes));
                    cc.getDeclaredMethod("determineIfApplet").setBody("{ return false; }");
                    return cc.toBytecode();
                } catch (Exception e) {
                    System.err.println("PatchAgent failed: " + e.getMessage());
                    return null;
                }
            }
        });
    }
}
