package doh.nvbsp.imu.dugongbayani.lib.models;

public class    Agency {

    private String id;
    private String name;
    private String seats;
    private String tableAssignment;

    public Agency(String id, String name, String seats, String tableAssignment) {
        this.id = id;
        this.name = name;
        this.seats = seats;
        this.tableAssignment = tableAssignment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getTableAssignment() {
        return tableAssignment;
    }

    public void setTableAssignment(String tableAssignment) {
        this.tableAssignment = tableAssignment;
    }
}
