package net.boruvka.idea.tunnellij;

import java.io.*;
import java.util.Properties;

/**
 * @author pingd
 */
public class TunnelConfig {

    private static final String PROPERTIES_FILE_NAME = "tunnelliJ.properties";
    private static File propertiesFile;
    public static final int BUFFER_LENGTH = 4096;
    public static final String DST_HOST = "tunnelliJ.dst.hostname";
    public static final String DST_PORT = "tunnelliJ.dst.port";
    public static final String SRC_PORT = "tunnelliJ.src.port";
    public static Properties properties;

    static {
        propertiesFile = new File(System.getProperty("user.home"), PROPERTIES_FILE_NAME);
        properties = new Properties();
    }

    /**
     * 加载配置文件
     */
    public static void loadProperties() {
        if (!propertiesFile.exists()) {
            return;
        }
        try(InputStream is = new FileInputStream(propertiesFile)) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储配置文件
     */
    public static void saveProperties() {
        try(OutputStream os = new FileOutputStream(propertiesFile)) {
            properties.store(os, "TunnelliJ plugin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDestinationString() {
        return properties.getProperty(DST_HOST, "localhost");
    }

    public static String getDestinationPort() {
        return properties.getProperty(DST_PORT, "8080");
    }

    public static String getSourcePort() {
        return properties.getProperty(SRC_PORT, "80");
    }

}