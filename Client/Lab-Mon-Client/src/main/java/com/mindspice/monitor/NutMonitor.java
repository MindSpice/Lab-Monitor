package com.mindspice.monitor;

import com.mindspice.Settings;
import com.mindspice.jnut.Client;
import com.mindspice.jnut.Device;
import com.mindspice.jnut.NutException;
import com.mindspice.util.Utils;

import java.io.IOException;

public class NutMonitor implements Runnable {
    private Client client;
    private final Settings settings = Settings.get();
    private Device device;

    public NutMonitor() throws IOException, NutException {
        connect();
    }

    private void connect() throws IOException, NutException {
        client = new Client(settings.nutHostAddr, settings.nutHostPort, settings.nutUser, settings.nutPass);
        device = client.getDevice(Settings.get().nutDevice);
    }

    @Override
    public void run() {
        if (client == null || !client.isConnected()) {
            try {
                connect();
            } catch (Exception e) {
                System.out.println("Failed To Re-Connect to NUT Host");
            }
        }
        if (device == null || !client.isConnected()) {
            return;
        }
        try {

            if (settings.isShutDownPct) {
                if (Integer.parseInt(device.getVariable(settings.nutPctString).getValue()) <= settings.shutDownThreshold) {
                    Utils.shutdown();
                }
            } else {
                if (Integer.parseInt(device.getVariable(settings.nutPctString).getValue()) <= settings.shutDownThreshold) {
                    Utils.shutdown();
                }
            }
        } catch (Exception e) {
            System.out.println("Error Parsing Nut Server");
            e.printStackTrace();
        }
    }
}
