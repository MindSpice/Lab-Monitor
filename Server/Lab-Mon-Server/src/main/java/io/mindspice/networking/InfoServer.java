package io.mindspice.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import io.mindspice.Settings;
import io.mindspice.data.DiskInfo;
import io.mindspice.networking.packets.Handshake;
import io.mindspice.networking.packets.NetInfo;
import io.mindspice.networking.packets.Request;
import io.mindspice.networking.packets.Update;
import io.mindspice.state.ClientStates;
import io.mindspice.state.NutState;

import java.io.IOException;

public class InfoServer {
    Server server = new Server();

    public InfoServer() throws IOException {
        var kryo = server.getKryo();
        kryo.register(double[].class);
        kryo.register(String[].class);
        kryo.register(Handshake.class);
        kryo.register(DiskInfo.class);
        kryo.register(NetInfo.class);
        kryo.register(Request.class);
        kryo.register(Update.class);
        kryo.register(DiskInfo.class);
        kryo.register(DiskInfo[].class);
        server.bind(Settings.get().port);
        server.start();


        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {

                if (object instanceof NetInfo info) {
                    ClientStates.get().addClientData(info.client, info);
                } else if (object instanceof Handshake handshake) {
                    ClientStates.get().addClient(connection, handshake);
                    System.out.println("Client Connected: " + handshake.name + "@"
                            + connection.getRemoteAddressTCP().getAddress().getHostAddress());
                }
            }
        });
    }

    public void sendInfoRequest() {
        var update = new Update();
        update.request = Request.INFO;

        for (var connection : server.getConnections()) {
            connection.sendTCP(update);
        }
    }

    public void sendBatteryUpdate() {
        var update = new Update();
        var nutData = NutState.get().getRecentData();
        update.request = Request.BATTERY;
        update.runtime = (int) nutData.runtime();
        update.batteryPct = (int) nutData.charge();

        for (var client : ClientStates.get().getClients()) {
            if (client.isSendShutdown()) {
                client.getConnection().sendTCP(update);
            }
        }
    }
}
