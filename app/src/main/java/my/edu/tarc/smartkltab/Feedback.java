package my.edu.tarc.smartkltab;

public class Feedback {
    private int feedbackID;
    private String feedbackType;
    private String subject;
    private String description;
    private String date;
    private int citizenID;
    private String status;

    public Feedback() {
    }

    public Feedback(int feedbackID, String feedbackType, String subject, String description, String date, int citizenID, String status) {
        this.feedbackID = feedbackID;
        this.feedbackType = feedbackType;
        this.subject = subject;
        this.description = description;
        this.date = date;
        this.citizenID = citizenID;
        this.status = status;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(int citizenID) {
        this.citizenID = citizenID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
