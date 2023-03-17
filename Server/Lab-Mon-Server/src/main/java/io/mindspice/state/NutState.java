package io.mindspice.state;

import io.mindspice.Settings;
import io.mindspice.data.NutData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NutState {
    private static NutState instance = null;
    private final ConcurrentLinkedDeque<NutData> nutData = new ConcurrentLinkedDeque<>();

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

    public List<NutData> getData() {
        return new ArrayList<>(nutData);
    }

    public NutData getRecentData() {
       return nutData.peekLast();
    }
}
