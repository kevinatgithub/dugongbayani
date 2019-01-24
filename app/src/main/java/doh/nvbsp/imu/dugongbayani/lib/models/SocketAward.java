package doh.nvbsp.imu.dugongbayani.lib.models;

public class SocketAward {
    private int id;
    private String agency;
    private int seats;
    private String tableAssignment;
    private int donations;
    private String award;
    private String extraAward;
    private String recipients;
    private String photo;
    private boolean completed;

    public SocketAward(int id, String agency, int seats, String tableAssignment, int donations, String award, String extraAward, String recipients, String photo, boolean completed) {
        this.id = id;
        this.agency = agency;
        this.seats = seats;
        this.tableAssignment = tableAssignment;
        this.donations = donations;
        this.award = award;
        this.extraAward = extraAward;
        this.recipients = recipients;
        this.photo = photo;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getTableAssignment() {
        return tableAssignment;
    }

    public void setTableAssignment(String tableAssignment) {
        this.tableAssignment = tableAssignment;
    }

    public int getDonations() {
        return donations;
    }

    public void setDonations(int donations) {
        this.donations = donations;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getExtraAward() {
        return extraAward;
    }

    public void setExtraAward(String extraAward) {
        this.extraAward = extraAward;
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
