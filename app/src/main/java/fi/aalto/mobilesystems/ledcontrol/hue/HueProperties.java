package fi.aalto.mobilesystems.ledcontrol.hue;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class HueProperties {
    private HueProperties(){}
    private static final String TAG = "HueController";
    private static Properties props = null;
    private final static String propertiesFile = "";
    private final static String USERNAME_KEY = "username";
    private final static String IP_ADDRESS_KEY = "ip_address";

    public static void setUsername(String username) {
        props.setProperty(USERNAME_KEY, username);
        saveProperties();
    }

    public static String getUsername() {
        return props.getProperty(USERNAME_KEY);
    }

    public static void setIpAddress(String ipAddress) {
        props.setProperty(IP_ADDRESS_KEY, ipAddress);
        saveProperties();
    }

    public static String getIpAddress() {
        return props.getProperty(IP_ADDRESS_KEY);
    }

    public static void loadProperties() {
        props = new Properties();
        FileInputStream in;
        try {
            in = new FileInputStream(propertiesFile);
            try {
                props.load(in);
            } finally {
                in.close();
            }
        } catch (IOException ex) {
            Log.e(TAG, "Error while loading properties", ex);
        }
    }

    public static void saveProperties() {
        FileOutputStream out;
        try {
            out = new FileOutputStream(propertiesFile);
            try {
                props.store(out, null);
            } finally {
                out.close();
            }
        } catch (IOException ex) {
            Log.e(TAG, "Error while saving properties", ex);
        }
    }
}
