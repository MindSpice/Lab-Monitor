package com.mindspice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    private static Settings instance = null;
    public String clientName = "changeMe";
    public String statusHostAddr = "127.0.0.1";
    public int statusHostPort = 2288;
    public String nutHostAddr = "127.0.0.1";
    public int nutHostPort = 3493;
    public boolean doShutDown = true;
    public boolean doWakeOnLan = true;
    public boolean isNUTClient = false;
    public boolean isStatusClient = true;
    public boolean printStatusOut = false;
    public boolean isShutDownPct = true;
    public int shutDownThreshold = 50;
    public int wakeUpThreshold = 80;
    public String nutUser = "user";
    public String nutPass = "pass";
    public int nutPollFreq = 20;
    public int printFreq = 60;
    public String nutRuntimeString = "";
    public String nutPctString = "";
    public String shutdownCommand = "";
    public String nutDevice = "";
    public boolean sendDiskInfo = false;

    public static Settings get(){
        if (instance == null) {
            loadConfig();
        }
        return instance;
    }

    private static void loadConfig() {
        var yaml = new ObjectMapper(new YAMLFactory());
        yaml.enable((DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
        var userDir = System.getProperty("user.dir") + File.separator;
        var configFile = new File((userDir + "config.yaml"));

        try (var fr = new FileReader(configFile)) {
            instance = yaml.readValue(fr, Settings.class);
        } catch (IOException e) {
            // Add dialog
            throw new RuntimeException(e);
        }
    }


    // Used when building to gen default config, set to read only, users can just copy it if needed
    private static void writeEmptyConfig() {
        var yaml = new ObjectMapper(new YAMLFactory());
        var userDir = System.getProperty("user.dir") + File.separator;
        var defaultConfig = new File(userDir + "default.yaml");
        try (final FileWriter fw = new FileWriter(defaultConfig)) {
            fw.write(yaml.writeValueAsString(instance));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Settings{");
        sb.append("clientName='").append(clientName).append('\'');
        sb.append(",\n statusHostAddr='").append(statusHostAddr).append('\'');
        sb.append(",\n statusHostPort=").append(statusHostPort);
        sb.append(",\n nutHostAddr='").append(nutHostAddr).append('\'');
        sb.append(",\n nutHostPort=").append(nutHostPort);
        sb.append(",\n doShutDown=").append(doShutDown);
        sb.append(",\n doWakeOnLan=").append(doWakeOnLan);
        sb.append(",\n isNUTClient=").append(isNUTClient);
        sb.append(",\n isStatusClient=").append(isStatusClient);
        sb.append(",\n printStatusOut=").append(printStatusOut);
        sb.append(",\n isShutDownPct=").append(isShutDownPct);
        sb.append(",\n shutDownThreshold=").append(shutDownThreshold);
        sb.append(",\n wakeUpThreshold=").append(wakeUpThreshold);
        sb.append(",\n nutUser='").append(nutUser).append('\'');
        sb.append(",\n nutPass='").append(nutPass).append('\'');
        sb.append(",\n nutPollFreq=").append(nutPollFreq);
        sb.append(",\n printFreq=").append(printFreq);
        sb.append(",\n nutRuntimeString='").append(nutRuntimeString).append('\'');
        sb.append(",\n nutPctString='").append(nutPctString).append('\'');
        sb.append(",\n shutdownCommand='").append(shutdownCommand).append('\'');
        sb.append(",\n nutDevice='").append(nutDevice).append('\'');
        sb.append(",\n sendDiskInfo=").append(sendDiskInfo);
        sb.append('}');
        return sb.toString();
    }
}
