package io.mindspice.monitor;

import io.mindspice.Settings;
import io.mindspice.data.NutData;
import io.mindspice.jnut.Client;
import io.mindspice.jnut.Device;
import io.mindspice.jnut.NutException;
import io.mindspice.state.ClientStates;
import io.mindspice.state.NutState;

import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class NutMonitor implements Runnable {
    private Client client;
    private Device device;
    private final Settings settings = Settings.get();


    public NutMonitor() throws IOException, NutException {
        connect();

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
        if (device == null || !client.isConnected()) { return; }

        try {
            var nutInfo = device.getVariableTable();
            var deviceStatus = nutInfo.get(settings.nutStatusVar);
            var deviceCharge = Integer.parseInt(nutInfo.get(settings.nutChargeVar));
            var data = new NutData(
                    isOnline(deviceStatus),
                    deviceStatus,
                    Double.parseDouble(nutInfo.get(settings.nutRuntimeVar)),
                    deviceCharge,
                    Double.parseDouble(nutInfo.get(settings.nutLoadVar)),
                    Double.parseDouble(nutInfo.get(settings.nutPowerDrawVar)),
                    Double.parseDouble(nutInfo.get(settings.nutInputVar)),
                    Double.parseDouble(nutInfo.get(settings.nutOutputVar)),
                    Double.parseDouble(nutInfo.get(settings.nutTempVar)),
                    nutInfo.get(settings.nutTestVar),
                    LocalTime.now().truncatedTo(ChronoUnit.SECONDS)
            );
            NutState.get().addData(data);
            NutState.get().updateInfoList(nutInfo.entrySet()
                    .stream()
                    .map(s -> (s.getKey() + ": " + s.getValue()))
                    .toList()
            );
        } catch (Exception e) {
            System.out.println("Error Parsing Nut Server");
            e.printStackTrace();
        }
    }

    private void connect() throws IOException, NutException {
        client = new Client(settings.nutHostAddr, settings.nutHostPort, settings.nutUser, settings.nutPass);
        device = client.getDevice(Settings.get().nutDevice);
    }

    private boolean isOnline(String status) {
        return status.equals(settings.nutOnlineVal)
                || status.equals(settings.nutReplaceBatVal)
                || status.equals(settings.nutBypassVal);
    }
}
