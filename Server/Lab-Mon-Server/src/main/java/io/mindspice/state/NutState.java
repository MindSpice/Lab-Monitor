package io.mindspice.state;

import io.mindspice.Settings;
import io.mindspice.data.FullNutData;
import io.mindspice.data.LinePoint;
import io.mindspice.data.NutData;
import io.mindspice.data.NutRadialData;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

public class NutState {
    private static NutState instance = null;
    private final ConcurrentLinkedDeque<NutData> nutData = new ConcurrentLinkedDeque<>();
    private volatile List<String> infoList = new ArrayList<>(0);

    private NutState() {
    }


    public static NutState get() {
        if (instance == null) {
            instance = new NutState();
        }
        return instance;
    }


    public void addData(NutData data) {
        if (nutData.size() == Settings.get().nutHistorySize) {
            nutData.poll();
            nutData.add(data);
        } else {
            nutData.add(data);
        }
    }


    public void updateInfoList(List<String> infoList) {
        this.infoList = infoList;
    }

    public boolean isOnline() {
        if (nutData.peekLast() == null) {
            return false;
        }
        return nutData.peekLast().online();
    }

    public List<String> getInfoList() {
        var info = new ArrayList<>(infoList);
        Collections.sort(info);
        return info;
    }


    public FullNutData getFullData(boolean fullList) {
        var data = fullList ? new ArrayList<>(nutData) : Collections.singletonList(nutData.peekLast());
        if (data.isEmpty()) {
            return null;
        }

        var voltageIn = new LinePoint[]{new LinePoint("Input Voltage")};
        var voltageOut = new LinePoint[]{new LinePoint("Output Voltage")};
        var power = new LinePoint[]{new LinePoint("Power")};
        var load = new LinePoint[]{new LinePoint("Load")};
        var charge = new LinePoint[]{new LinePoint("Charge")};
        var runtime = new LinePoint[]{new LinePoint("Runtime")};
        var temperature = new LinePoint[]{new LinePoint("Celsius")};
        var online = new LinePoint[]{new LinePoint("Online")};

        LocalTime lastTime = null;
        for (var dp : data) {
            if (lastTime == null) {
                lastTime = dp.time();
            } else {
                if (!(SECONDS.between(lastTime, dp.time()) >= Settings.get().dashboardInterval)) {
                    continue;
                } else {
                    lastTime = dp.time();
                }
            }

            var time = dp.time().truncatedTo(MINUTES).toString();
            voltageIn[0].addData(new LinePoint.Point(time, dp.inputVoltage()));
            voltageOut[0].addData(new LinePoint.Point(time, dp.outputVoltage()));
            power[0].addData(new LinePoint.Point(time, dp.power()));
            load[0].addData(new LinePoint.Point(time, dp.load()));
            charge[0].addData(new LinePoint.Point(time, dp.charge()));
            runtime[0].addData(new LinePoint.Point(time, dp.runtime()));
            temperature[0].addData(new LinePoint.Point(time, dp.temperature()));
            online[0].addData(new LinePoint.Point(time, dp.online() ? 1d : 0d));
        }

        return new FullNutData(
                data.get(data.size() - 1).time().toEpochSecond(LocalDate.now(), ZoneOffset.UTC),
                voltageIn,
                voltageOut,
                power,
                load,
                charge,
                runtime,
                temperature,
                online
        );
    }


    public NutRadialData getRadialData() {
        var data = nutData.peekLast();
        if (data == null) {
            return null;
        }

        return new NutRadialData(
                data.online(),
                data.testResult(),
                new NutRadialData.RadialData[]{
                        new NutRadialData.RadialData("charge", "", (int) data.charge()),
                        new NutRadialData.RadialData("discharge", "", 100 - (int) data.charge())
                }
        );
    }


    public NutData getRecentData() {
        return nutData.peekLast();
    }

    public double getCharge() {
        return nutData.peekLast().charge();
    }

}
