package com.example.nikolay.conecttotestserver;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class Util {
    private Util() {
    }

    static Properties prop = new Properties();

    static {
        InputStream inputStream = Util.class.getClassLoader().getResourceAsStream("assets/config.properties");

        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFilePathToSave(String property) {
        String value = "";
        value = prop.getProperty(property);
        return value;
    }
}
