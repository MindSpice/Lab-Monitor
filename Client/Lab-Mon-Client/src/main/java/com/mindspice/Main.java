package com.mindspice;

import com.mindspice.monitor.LocalMonitor;
import com.mindspice.monitor.NutMonitor;
import com.mindspice.networking.StatusClient;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        StatusClient statusClient;

        if (Settings.get().printStatusOut) {
            exec.scheduleAtFixedRate(new LocalMonitor(), Settings.get().printFreq, Settings.get().printFreq, TimeUnit.SECONDS);
        }

        if (Settings.get().isNUTClient && Settings.get().doShutDown) {
            exec = Executors.newSingleThreadScheduledExecutor();
            try {
                exec.scheduleAtFixedRate(new NutMonitor(), Settings.get().nutPollFreq, Settings.get().nutPollFreq, TimeUnit.SECONDS);
            } catch (Exception e) {
                System.out.println("Failed To Initiate Nut Monitor, Connection Could Not Be Made");
            }
        }

        if (Settings.get().isStatusClient) {
            try {
                statusClient = new StatusClient();
                Runnable statusConMon = () -> {
                    try {
                        if (!statusClient.isConnected()) {
                            statusClient.reconnect();
                        }
                    } catch (Exception e) {
                        System.out.println("Failed To Reconnect To Status Host");
                    }
                };
                exec.scheduleAtFixedRate(statusConMon,30, 30, TimeUnit.SECONDS);
            } catch (IOException e) {
                System.out.println("Failed To Connect To Status Host");
            }
        }

    }
}