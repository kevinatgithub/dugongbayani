package doh.nvbsp.imu.dugongbayani.lib.models;

import java.util.ArrayList;

public class WinnerAward {

    private String id;
    private String award_id;
    private String agency_id;
    private String order_no;
    private Award award;
    private Agency agency;
    private String recipients;
    private String photo;
    private String completed;
    private String priority;

    public WinnerAward(String id, String award_id, String agency_id, String order_no, Award award, Agency agency, String recipients, String photo, String completed, String priority) {
        this.id = id;
        this.award_id = award_id;
        this.agency_id = agency_id;
        this.order_no = order_no;
        this.award = award;
        this.agency = agency;
        this.recipients = recipients;
        this.photo = photo;
        this.completed = completed;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAward_id() {
        return award_id;
    }

    public void setAward_id(String award_id) {
        this.award_id = award_id;
    }

    public String getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(String agency_id) {
        this.agency_id = agency_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
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

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
