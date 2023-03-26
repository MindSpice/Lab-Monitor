package com.mindspice;

import com.mindspice.monitor.LocalMonitor;
import com.mindspice.monitor.NutMonitor;
import com.mindspice.networking.StatusClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) {
        Settings settings = Settings.get();
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        StatusClient statusClient;

        if (Settings.get().printStatusOut) {
            exec.scheduleAtFixedRate(new LocalMonitor(), Settings.get().printFreq, Settings.get().printFreq, TimeUnit.SECONDS);
            System.out.println("Started Local Stt Monitoring To stdout");
        }

        if (Settings.get().isNUTClient && Settings.get().doShutDown) {
            try {
                NutMonitor nutMon = new NutMonitor();
                Runnable nutConMon = () -> {
                    try {
                        if (!nutMon.isConnected()) {
                            var connected = nutMon.connect();
                            if (!connected) {
                                System.out.println("Reconnection To Status Host Failed, Retrying in 30s");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Unknown Exception:" + Arrays.toString(e.getStackTrace()));
                    }
                };
                exec.scheduleAtFixedRate(nutMon, Settings.get().nutPollFreq, Settings.get().nutPollFreq, TimeUnit.SECONDS);
                exec.scheduleAtFixedRate(nutConMon, 0, 30, TimeUnit.SECONDS);
                System.out.println("Started Local Nut Monitoring Of: " + settings.nutDevice + "@" +
                        settings.nutHostAddr +":" + settings.nutHostPort);
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
                            var connected = statusClient.connect();
                            if (!connected) {
                                System.out.println("Reconnection To Status Host Failed, Retrying in 30s");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Unknown Exception:" + Arrays.toString(e.getStackTrace()));
                    }
                };
                exec.scheduleAtFixedRate(statusConMon, 0, 30, TimeUnit.SECONDS);
                System.out.println("Connected And Relaying Performance Information With Status Host: " +
                        settings.statusHostAddr + ":" + settings.statusHostPort);
                System.out.println("Variables: " + "sendDiskInfo:" + settings.sendDiskInfo + ", cpuMonitorPeriod:"
                        + settings.cpuMonitorPeriod +  ", doShutDown:" + settings.doShutDown + ", doWakeOnLan:"
                        + settings.doWakeOnLan + ", isShutDownPct:" + settings.isShutDownPct + ", shutDownThreshold:"
                        + settings.shutDownThreshold + ", shutdownCommand:" + settings.shutdownCommand);
            } catch (Exception e) {
                throw new RuntimeException("Fatal Error starting Status Client: " + Arrays.toString(e.getStackTrace()));
            }
        }

    }
}