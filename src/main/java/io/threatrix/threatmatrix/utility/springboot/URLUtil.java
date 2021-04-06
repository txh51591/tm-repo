package io.threatrix.threatmatrix.utility.springboot;

import org.springframework.util.SystemPropertyUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class URLUtil {

    /*
    *     
    *    
    *    Matching Project: https://spring.io/projects/spring-boot
    *    Matching Release: https://github.com/spring-projects/spring-boot/releases/tag/v2.2.4.RELEASE
    *    Matching Source File: SpringCli.java
    *    
    *    Auto-attribution by Threatrix, Inc.
    */
    private static URL[] getExtensionURLs() {
        List<URL> urls = new ArrayList<>();
        String home = SystemPropertyUtils.resolvePlaceholders("${spring.home:${SPRING_HOME:.}}");
        File extDirectory = new File(new File(home, "lib"), "ext");
        if (extDirectory.isDirectory()) {
            for (File file : extDirectory.listFiles()) {
                if (file.getName().endsWith(".jar")) {
                    try {
                        urls.add(file.toURI().toURL());
                    }
                    catch (MalformedURLException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        }
        return urls.toArray(new URL[0]);
    }
}