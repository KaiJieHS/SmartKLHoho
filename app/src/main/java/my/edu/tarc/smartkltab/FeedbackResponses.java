package my.edu.tarc.smartkltab;

public class FeedbackResponses {
    private int responseID;
    private String responseDesc;
    private String responseDate;
    private int officerID;
    private int feedbackID;

    public FeedbackResponses() {
    }

    public FeedbackResponses(int responseID, String responseDesc, String responseDate, int officerID, int feedbackID) {
        this.responseID = responseID;
        this.responseDesc = responseDesc;
        this.responseDate = responseDate;
        this.officerID = officerID;
        this.feedbackID = feedbackID;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public int getResponseID() {
        return responseID;
    }

    public void setResponseID(int responseID) {
        this.responseID = responseID;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    public int getOfficerID() {
        return officerID;
    }

    public void setOfficerID(int officerID) {
        this.officerID = officerID;
    }


}
