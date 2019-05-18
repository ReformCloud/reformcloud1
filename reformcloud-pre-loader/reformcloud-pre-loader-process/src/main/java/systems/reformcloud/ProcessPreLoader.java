/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author _Klaro | Pasqual K. / created on 15.05.2019
 */

public final class ProcessPreLoader implements Serializable {
    public static synchronized void main(String[] args) {
        String preFileToLoad = Arrays.stream(args)
                .filter(e -> e.startsWith("--file=") && e.endsWith(".jar"))
                .findFirst().orElse(null);
        if (preFileToLoad == null) {
            System.exit(1);
            return;
        }

        Matcher matcher = Pattern.compile("--file=(.*)").matcher(preFileToLoad);
        String file = matcher.matches() ? matcher.group(1) : null;
        if (file == null) {
            System.exit(1);
            return;
        }

        List<String> argsList = asList(args);
        argsList.remove(preFileToLoad);
        args = argsList.toArray(new String[0]);

        File target = new File(file);
        run(args, target);
    }

    private static synchronized void run(String[] args, File target) {
        if (args == null || target == null) {
            System.exit(1);
            return;
        }

        LibraryPreLoader.prepareDependencies();
        List<URL> dependencies = LibraryPreLoader.downloadDependencies();
        System.out.println("\nSuccessfully installed all necessary libraries");
        run0(args, target, dependencies);
    }

    private static synchronized void run0(String[] args, File target, List<URL> libs) {
        if (args == null || target == null || libs == null) {
            System.exit(1);
            return;
        }

        try {
            libs.add(target.toURI().toURL());
        } catch (final MalformedURLException ex) {
            ex.printStackTrace();
            System.exit(1);
            return;
        }
        run1(args, target, libs);
    }

    private static synchronized void run1(String[] args, File target, List<URL> libs) {
        if (args == null || target == null || libs == null) {
            System.exit(1);
            return;
        }

        ClassLoader classLoader = CommonLoader.createClassLoader(libs);
        setSystemClassLoaderTo(classLoader);
        Thread.currentThread().setContextClassLoader(classLoader);
        run2(args, target, classLoader);
    }

    private static synchronized void run2(String[] args, File target, ClassLoader classLoader) {
        if (args == null || target == null || classLoader == null) {
            System.exit(1);
            return;
        }

        JarFile jarFile = null;
        try {
            jarFile = new JarFile(target);
            String main = jarFile.getManifest().getMainAttributes().getValue("Main-Class");
            Method mainMethod = classLoader.loadClass(main).getMethod("main", String[].class);
            run3(args, mainMethod);
        } catch (final IOException | ClassNotFoundException | NoSuchMethodException ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            try {
                jarFile.close();
            } catch (final IOException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    private static synchronized void run3(String[] args, Method method) {
        method.setAccessible(true);
        try {
            method.invoke(null, (Object) args);
        } catch (final IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    private static synchronized void setSystemClassLoaderTo(ClassLoader classLoaderTo) {
        if (classLoaderTo == null) {
            System.exit(1);
            return;
        }

        try {
            Field field = ClassLoader.class.getDeclaredField("scl");
            field.setAccessible(true);
            field.set(null, classLoaderTo);
        } catch (final NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    private static List<String> asList(String[] args) {
        List<String> out = new LinkedList<>();
        out.addAll(Arrays.asList(args));
        return out;
    }
}
