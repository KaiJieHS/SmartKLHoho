package my.edu.tarc.smartkltab;

public class Organization {
    private int OrganizationID;
    private String OrgName;
    private String OrgBranchLocation;
    private String OrgContactNum;

    public Organization() {

    }

    public Organization(int organizationID, String orgName, String orgBranchLocation, String orgContactNum) {
        OrganizationID = organizationID;
        OrgName = orgName;
        OrgBranchLocation = orgBranchLocation;
        OrgContactNum = orgContactNum;
    }

    public int getOrganizationID() {
        return OrganizationID;
    }

    public void setOrganizationID(int organizationID) {
        OrganizationID = organizationID;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getOrgBranchLocation() {
        return OrgBranchLocation;
    }

    public void setOrgBranchLocation(String orgBranchLocation) {
        OrgBranchLocation = orgBranchLocation;
    }

    public String getOrgContactNum() {
        return OrgContactNum;
    }

    public void setOrgContactNum(String orgContactNum) {
        OrgContactNum = orgContactNum;
    }
}
