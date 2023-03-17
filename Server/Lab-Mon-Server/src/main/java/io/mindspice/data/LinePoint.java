package io.mindspice.data;

import java.util.ArrayList;
import java.util.List;

public class LinePoint {
    public String id;
    public List<Point> data = new ArrayList<>();


    public LinePoint(String id) {
        this.id = id;
    }

    public void addData(Point point) {
        data.add(point);
    }


    public record Point(String x, Double y) {
    }
}

