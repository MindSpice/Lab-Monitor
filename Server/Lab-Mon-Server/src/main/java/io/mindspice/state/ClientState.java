package io.mindspice.state;

import com.esotericsoftware.kryonet.Connection;
import io.mindspice.Settings;
import io.mindspice.data.*;
import io.mindspice.networking.packets.Handshake;
import io.mindspice.networking.packets.NetInfo;
import io.mindspice.utils.Utils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ClientState {
    private String name;
    private String os;
    private boolean isWakeOnLan;
    private boolean isSendShutdown;
    private int wakeupThreshold;
    private String address;
    private String[] macAddresses;
    private DecimalFormat df =  Utils.getFormatter();

    private Connection connection;
    private final ConcurrentLinkedDeque<NetInfo> infoData = new ConcurrentLinkedDeque<>();

    public ClientState(Connection connection, Handshake handshake) {
        name = handshake.name;
        os = handshake.os;
        address = connection.getRemoteAddressTCP().getAddress().getHostAddress();
        isWakeOnLan = handshake.sendWakeOnLan;
        isSendShutdown = handshake.sendShutdownInfo;
        wakeupThreshold = handshake.wakeThreshold;
        macAddresses = handshake.macAddress;
        this.connection = connection;
    }


    public void onReconnection(Connection connection, Handshake handshake) {
        os = handshake.os;
        address = connection.getRemoteAddressTCP().getAddress().getHostAddress();
        isWakeOnLan = handshake.sendWakeOnLan;
        isSendShutdown = handshake.sendShutdownInfo;
        wakeupThreshold = handshake.wakeThreshold;
        macAddresses = handshake.macAddress;
        this.connection = connection;
    }


    public void addData(NetInfo data) {
        if (infoData.size() == Settings.get().clientHistorySize) {
            infoData.poll();
            infoData.add(data);
        } else {
            infoData.add(data);
        }
    }


    public FullClientData getFullData(boolean fullList) {
        var data = fullList ? new ArrayList<>(infoData) : Collections.singletonList(infoData.peekLast());
        if (data.isEmpty()) { return null; }
        var coreCount = data.get(0).cpuSpeeds.length;
        var diskCount = data.get(data.size() -1).diskInfo.length;
        var diskData = new DiskData[diskCount];

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

        for (int i = 0; i < diskCount; ++i) {
            var disk = data.get(data.size() -1).diskInfo[i];
            diskData[i] = new DiskData(
                    disk.mount,
                    Double.parseDouble(df.format(disk.totalSpace - disk.usableSpace)),
                    Double.parseDouble(df.format(disk.usableSpace))
            );
        }

        return new FullClientData(
                LocalTime.parse(data.get(data.size() - 1).time).toEpochSecond(LocalDate.now(), ZoneOffset.UTC),
                name,
                address,
                cpuSpeeds,
                cpuUsage,
                memSwap,
                diskData
        );
    }


    public BriefClientData getSimpleData() {
        var data = infoData.peekLast();
        if (data == null) { return null; }

        return new BriefClientData(
                isConnected(),
                name,
                address,
                os,
                data.cpuTemp + "C",
                df.format(data.cpuAvgSpeed) + "GHz",
                df.format(data.cpuAvgUsage) + "%",
                String.valueOf(data.cpuThreads),
                String.valueOf(data.cpuProcesses),
                df.format(data.memoryUsage[0]) + "/" + df.format(data.memoryUsage[1]) +"GB",
                df.format(data.swapUsage[0]) + "/" + df.format(data.swapUsage[1]) +"GB"
        );
    }


    public ClientBarData getBarData() {
        var data = infoData.peekLast();
        if (data == null) { return null; }

        return new ClientBarData(
                name,
                Float.parseFloat(df.format(data.cpuAvgUsage)),
                Float.parseFloat(df.format((data.memoryUsage[0]/ data.memoryUsage[1]) * 100)),
                Float.parseFloat(df.format((data.swapUsage[0]/ data.swapUsage[1]) * 100))
        );
    }


    public boolean isConnected() {
        return connection.isConnected();
    }

    public String getAddress() {
        return address;
    }

    public String[] getMACAddresses() {
        return macAddresses;
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
