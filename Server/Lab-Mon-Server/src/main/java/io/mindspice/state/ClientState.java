package io.mindspice.state;

import com.esotericsoftware.kryonet.Connection;
import io.mindspice.Settings;
import io.mindspice.data.FullClientData;
import io.mindspice.data.LinePoint;
import io.mindspice.data.SimpleClientData;
import io.mindspice.networking.packets.Handshake;
import io.mindspice.networking.packets.NetInfo;
import io.mindspice.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ClientState {
    private String name;
    private String os;
    private boolean isWakeOnLan;
    private boolean isSendShutdown;
    private int wakeupThreshold;
    private String address;
    private DecimalFormat df =  Utils.getFormatter();

    private Connection connection;
    private final ConcurrentLinkedDeque<NetInfo> infoData = new ConcurrentLinkedDeque<>();

    public ClientState(Connection connection, Handshake handshake) {
        name = handshake.name;
        os = handshake.os;
        isWakeOnLan = handshake.sendWakeOnLan;
        isSendShutdown = handshake.sendShutdownInfo;
        wakeupThreshold = handshake.wakeThreshold;
        this.connection = connection;
        address = connection.getRemoteAddressTCP().getAddress().getHostAddress();
    }


    public void onReconnection(Connection connection, Handshake handshake) {
        os = handshake.os;
        isWakeOnLan = handshake.sendWakeOnLan;
        isSendShutdown = handshake.sendShutdownInfo;
        wakeupThreshold = handshake.wakeThreshold;
        this.connection = connection;
        address = connection.getRemoteAddressTCP().getAddress().getHostAddress();
    }

    public void addData(NetInfo data) {
        if (infoData.size() == Settings.get().clientHistorySize) {
            infoData.poll();
            infoData.add(data);
        } else {
            infoData.add(data);
        }
    }

    public FullClientData getFullData() {
        var data = new ArrayList<NetInfo>(infoData);
        if (data.isEmpty()) { return null; }
        var dataSize = data.size();
        var coreCount = data.get(0).cpuSpeeds.length;

        var cpuSpeeds = new LinePoint[coreCount];
        var cpuUsage = new LinePoint[coreCount];
        var memSwap = new LinePoint[2];

        memSwap[0] = new LinePoint("Memory");
        memSwap[1] = new LinePoint("Swap");
        for (int i = 0; i < coreCount; ++i) {
            cpuSpeeds[i] = new LinePoint("Core " + i);
            cpuUsage[i] = new LinePoint("Core " + i);
        }

        for (var dp : data) {
            for (int i = 0; i < coreCount; ++i) {
                cpuSpeeds[i].addData(new LinePoint.Point(dp.time, dp.cpuSpeeds[i]));
                cpuUsage[i].addData((new LinePoint.Point(dp.time, dp.cpuUsage[i])));
            }
            memSwap[0].addData(new LinePoint.Point(dp.time, dp.memoryUsage[0]));
            memSwap[1].addData(new LinePoint.Point(dp.time, dp.swapUsage[0]));
        }

        return new FullClientData(
                name,
                os,
                address,
                cpuSpeeds,
                cpuUsage,
                memSwap
        );
    }

    public SimpleClientData getSimpleData() {
        var data = infoData.peekLast();
        if (data == null) return null;

        return new SimpleClientData(
                isConnected(),
                name,
                address,
                data.cpuTemp + "C",
                df.format(data.cpuAvgSpeed) + "GHz",
                df.format(data.cpuAvgUsage) + "%",
                String.valueOf(data.cpuThreads),
                String.valueOf(data.cpuProcesses),
                df.format(data.memoryUsage[0]) + "/" + df.format(data.memoryUsage[1]) +"GB",
                df.format(data.swapUsage[0]) + "/" + df.format(data.swapUsage[1]) +"GB"
        );
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    public String getAddress() {
        return address;
    }

    public boolean isWakeOnLan() {
        return isWakeOnLan;
    }

    public boolean isSendShutdown() {
        return isSendShutdown;
    }

    public int getWakeupThreshold() {
        return wakeupThreshold;
    }

    public Connection getConnection() {
        return connection;
    }
}
