package io.mindspice.monitor;

import io.mindspice.networking.InfoServer;
import io.mindspice.state.ClientStates;
import io.mindspice.state.NutState;
import io.mindspice.utils.Utils;

import java.io.IOException;

public class ClientMonitor implements Runnable{
    private static InfoServer infoServer;

    public ClientMonitor() throws IOException {
        infoServer = new InfoServer();
    }

    @Override
    public void run() {
        try {
            infoServer.sendInfoRequest();
            if (!NutState.get().isOnline()) {
                infoServer.sendBatteryUpdate();
            } else {
                var offlineClients = ClientStates.get().getOfflineClients();
                if (offlineClients.isEmpty()) { return; }

                for (var client : offlineClients) {
                    if (client.isWakeOnLan() && NutState.get().getCharge() >= client.getWakeupThreshold()) {
                        // No easy way to get primary network adapter on the client, so clients sends the MAC of all adapters
                        // Will lead to some pointless packets sent, but shouldn't be an issue.
                        for (var mac : client.getMACAddresses()) {
                            Utils.wakeOnLan(mac);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed To Send Update Request To Clients");
            e.printStackTrace();
        }
    }
}
