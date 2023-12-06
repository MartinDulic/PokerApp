package hr.dulic.pokerapp.model;

import javax.naming.Context;
import javax.naming.NamingException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

public class ConfigurationReader {

    private static Properties props = new Properties();

    static {
        Hashtable<String, String> environment= new Hashtable<>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
        environment.put(Context.PROVIDER_URL, "file:C:/Users/PREDATOR/Desktop/Projects/Java 2 Projekt/PokerApp/src/main/resources");

        try (InitialDirContextCloseable context = new InitialDirContextCloseable(environment)) {
            Object object = context.lookup("conf.properties");
            props.load(new FileReader(object.toString()));
        } catch (NamingException | IOException e) {
            e.printStackTrace();
        }

    }

    public static String getStringValueForKey(ConfigurationKey key) {
        return props.getProperty(key.getKey());
    }

    public static Integer getIntegerValueForKey(ConfigurationKey key) {
        return Integer.parseInt(getStringValueForKey(key));
    }

}
