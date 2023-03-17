package io.mindspice.monitor;

import io.mindspice.Settings;
import io.mindspice.data.NutData;
import io.mindspice.jnut.Client;
import io.mindspice.jnut.Device;
import io.mindspice.jnut.NutException;
import io.mindspice.state.ClientStates;
import io.mindspice.state.NutState;

import java.io.IOException;

public class NutMonitor implements Runnable{
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
        if (device == null || !client.isConnected()) {
            return;
        }

        try {
            var deviceStatus = device.getVariable(settings.nutStatusVar).getValue();
            var deviceCharge =  Integer.parseInt(device.getVariable(settings.nutChargeVar).getValue());
            var data = new NutData(
                    deviceStatus,
                    Integer.parseInt(device.getVariable(settings.nutRuntimeVar).getValue()),
                    deviceCharge,
                    Integer.parseInt(device.getVariable(settings.nutLoadVar).getValue()),
                    Integer.parseInt(device.getVariable(settings.nutPowerDrawVar).getValue()),
                    Float.parseFloat(device.getVariable(settings.nutInputVar).getValue()),
                    Float.parseFloat(device.getVariable(settings.nutOutputVar).getValue()),
                    Float.parseFloat(device.getVariable(settings.nutTempVar).getValue()),
                    device.getVariable(settings.nutTestVar).getValue()
            );
            NutState.get().addData(data);

            if (!deviceStatus.equals(Settings.get().nutOnlineVal)) {
                for (var client : ClientStates.get().getClients()) {
                    if (!client.isConnected() && client.isWakeOnLan()) {
                        if (deviceCharge >= client.getWakeupThreshold()) {
                            //TODO send wakeup packet to client.getAddress()
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error Parsing Nut Server");
            e.printStackTrace();
        }


    }

    private void connect() throws IOException, NutException {
        client = new Client(settings.nutHostAddr, settings.nutHostPort, settings.nutUser, settings.nutPass);
        device = client.getDevice(Settings.get().nutDevice);
    }


}
