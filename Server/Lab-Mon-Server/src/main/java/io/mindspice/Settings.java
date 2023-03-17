package io.mindspice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Settings {
    private static Settings instance = null;
    public int port;
    public boolean isNUTMonitor = true;
    public int clientPollRate = 30;
    public int nutPollRate = 20;
    public String nutHostAddr = "127.0.0.1";
    public int nutHostPort = 3493;
    public String nutDevice = "eaton";
    public String nutUser = "user";
    public String nutPass = "pass";
    public String nutRuntimeVar = "battery.runtime";
    public String nutChargeVar = "battery.charge";
    public String nutStatusVar = "ups.status";
    public String nutOnlineVal = "OL";
    public String nutReplaceBatVal = "RB";
    public String nutPowerDrawVar = "ups.realpower";
    public String nutLoadVar = "ups.load";
    public String nutOutputVar = "output.voltage";
    public String nutInputVar = "input.voltage";
    public String nutTempVar = "ups.temperature";
    public String nutTestVar = "ups.test.result";
    public int nutHistorySize = 120;
    public int clientHistorySize = 240;


    public static Settings get() {
        writeEmptyConfig();
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
            instance = new Settings();
            writeEmptyConfig();
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
}



