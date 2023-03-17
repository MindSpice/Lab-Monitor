package io.mindspice.state;

import com.esotericsoftware.kryonet.Connection;
import io.mindspice.data.FullClientData;
import io.mindspice.data.SimpleClientData;
import io.mindspice.networking.packets.Handshake;
import io.mindspice.networking.packets.NetInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientStates {
    private static ClientStates instance = null;
    private final HashMap<String, ClientState> clients = new HashMap<>();

    private ClientStates() {
    }

    public static ClientStates get() {
        if (instance == null) {
            instance = new ClientStates();
        }
        return instance;
    }

    public void addClient(Connection connection, Handshake handshake) {
        var client = clients.get(handshake.name);
        if (client == null) {
            clients.put(handshake.name, new ClientState(connection, handshake));
        } else {
            client.onReconnection(connection, handshake);
        }
    }

    public void addClientData(String name, NetInfo data) {
        var client = clients.get(name);
        if (client == null) {
            return;
        } else {
            client.addData(data);
        }
    }

    public FullClientData getClientData(String name) {
        var client = clients.get(name);
        if (client == null) {
            return null;
        } else {
            return client.getFullData();
        }
    }

    public boolean isClientConnected(String name) {
        var client = clients.get(name);
        if (client == null) {
            return false;
        } else {
            return client.isConnected();
        }
    }

    public List<ClientState> getClients() {
        return new ArrayList<>(clients.values());
    }

    public List<SimpleClientData> getClientsOverview() {
        var clientData = new ArrayList<SimpleClientData>(clients.size());
        for (var client : clients.values()) {
            clientData.add(client.getSimpleData());
        }
        return clientData;
    }
}
