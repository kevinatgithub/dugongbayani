package doh.nvbsp.imu.dugongbayani.lib.JsonResponse;

import java.util.ArrayList;

import doh.nvbsp.imu.dugongbayani.lib.models.SocketAward;

public class SocketQueue {
    private ArrayList<SocketAward> data;
    private SocketAward last;
    private ArrayList<SocketAward> nextInLine;

    public SocketQueue(ArrayList<SocketAward> data, SocketAward last, ArrayList<SocketAward> nextInLine) {
        this.data = data;
        this.last = last;
        this.nextInLine = nextInLine;
    }

    public ArrayList<SocketAward> getData() {
        return data;
    }

    public void setData(ArrayList<SocketAward> data) {
        this.data = data;
    }

    public SocketAward getLast() {
        return last;
    }

    public void setLast(SocketAward last) {
        this.last = last;
    }

    public ArrayList<SocketAward> getNextInLine() {
        return nextInLine;
    }

    public void setNextInLine(ArrayList<SocketAward> nextInLine) {
        this.nextInLine = nextInLine;
    }
}
