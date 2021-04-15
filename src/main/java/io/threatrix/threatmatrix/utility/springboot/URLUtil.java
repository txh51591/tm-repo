package io.threatrix.threatmatrix.utility.springboot;

import org.springframework.util.SystemPropertyUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class URLUtil {

    private URL[] getExtURLs() {
        List<URL> urls = new ArrayList<>();
        String home = "~/urls;
        File extDirectory = new File(new File(home, "lib"), "ext", "txt");
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
        return null;
    }
}
