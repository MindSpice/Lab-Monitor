package io.mindspice.utils;

import io.mindspice.Settings;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DecimalFormat;

public class Utils {
    private final static DecimalFormat df = new DecimalFormat("#.##");


    public static DecimalFormat getFormatter() {
        return df;
    }


    // Credit: https://github.com/jumar
    // Source: https://gist.github.com/jumar/9200840
    public static void wakeOnLan(String macAddr) {
        final int PORT = 9;
        try {
            byte[] macBytes = getMacBytes(macAddr);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }

            InetAddress address = InetAddress.getByName(Settings.get().broadcastAddress);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();

        }
        catch (Exception e) {
            System.out.println("Failed to send Wake-on-LAN packet: "  + Settings.get().broadcastAddress + "|" + macAddr);
        }

    }


    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address for WOL.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address for WOL.");
        }
        return bytes;
    }
}
