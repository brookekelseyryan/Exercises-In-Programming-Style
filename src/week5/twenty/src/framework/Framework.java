package week5.twenty.src.framework;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.stream.Stream;

public class Framework {

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();

        properties.load(new FileInputStream("config.properties"));
        String app = properties.getProperty("app");

        TFApplication applicationInstance = (TFApplication) new URLClassLoader(new URL[]{new File(app + ".jar").toURI().toURL()})
                .loadClass(app)
                .getDeclaredConstructor()
                .newInstance();

        Stream<String> words = applicationInstance.extractWords(args[0]);
        applicationInstance.top25Words(words).forEach(stringLongEntry -> System.out.println(stringLongEntry.getKey() + " - " + stringLongEntry.getValue()));

    }
}
