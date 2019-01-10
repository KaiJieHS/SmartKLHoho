package my.edu.tarc.smartkltab;

class HealthCare {
    private int HcID;
    private String HcBranchName;
    private String HcBranchLocation;
    private String HcContactNumber;

    public HealthCare() {
    }

    public HealthCare(int hcID, String hcBranchName, String hcBranchLocation, String hcContactNumber) {
        HcID = hcID;
        HcBranchName = hcBranchName;
        HcBranchLocation = hcBranchLocation;
        HcContactNumber = hcContactNumber;
    }

    public int getHcID() {
        return HcID;
    }

    public void setHcID(int hcID) {
        HcID = hcID;
    }

    public String getHcBranchName() {
        return HcBranchName;
    }

    public void setHcBranchName(String hcBranchName) {
        HcBranchName = hcBranchName;
    }

    public String getHcBranchLocation() {
        return HcBranchLocation;
    }

    public void setHcBranchLocation(String hcBranchLocation) {
        HcBranchLocation = hcBranchLocation;
    }

    public String getHcContactNumber() {
        return HcContactNumber;
    }

    public void setHcContactNumber(String hcContactNumber) {
        HcContactNumber = hcContactNumber;
    }
}
