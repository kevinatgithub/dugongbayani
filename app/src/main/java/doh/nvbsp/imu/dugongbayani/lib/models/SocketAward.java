package doh.nvbsp.imu.dugongbayani.lib.models;

public class SocketAward {
    private String id;
    private String agency;
    private String seats;
    private String tableAssignment;
    private String award;
    private String recipients;
    private String photo;
    private boolean completed;

    public SocketAward(String id, String agency, String seats, String tableAssignment, String award, String recipients, String photo, boolean completed) {
        this.id = id;
        this.agency = agency;
        this.seats = seats;
        this.tableAssignment = tableAssignment;
        this.award = award;
        this.recipients = recipients;
        this.photo = photo;
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
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

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
