package io.mindspice.monitor;

import io.mindspice.networking.InfoServer;

import java.io.IOException;

public class InfoMonitor implements Runnable{
    private static InfoServer infoServer;

    public InfoMonitor() throws IOException {
        infoServer = new InfoServer();
    }

    @Override
    public void run() {
        try {
            infoServer.sendInfoRequest();
        } catch (Exception e) {
            System.out.println("Failed To Send Update Request To Clients");
            e.printStackTrace();
        }
    }
}
