package doh.nvbsp.imu.dugongbayani.lib.models;

import android.graphics.Bitmap;

public class Recipient {

    private String id;
    private String agency_id;
    private String completeName;
    private String tableAssignment;
    private Bitmap image;

    private Agency agency;

    public Recipient(String id, String agency_id, String completeName, String tableAssignment, Bitmap image) {
        this.id = id;
        this.agency_id = agency_id;
        this.completeName = completeName;
        this.tableAssignment = tableAssignment;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(String agency_id) {
        this.agency_id = agency_id;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getTableAssignment() {
        return tableAssignment;
    }

    public void setTableAssignment(String tableAssignment) {
        this.tableAssignment = tableAssignment;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }
}
