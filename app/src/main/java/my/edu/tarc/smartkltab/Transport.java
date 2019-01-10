package my.edu.tarc.smartkltab;

public class Transport {
    private int transportID;
    private String transportType;
    private String transportLine;
    private String transportSchedule;

    public Transport() {
    }

    public Transport(int transportID, String transportLine) {
        this.transportID = transportID;
        this.transportLine = transportLine;
    }


    public Transport(int transportID, String transportLine, String transportSchedule) {
        this.transportID = transportID;
        this.transportLine = transportLine;
        this.transportSchedule = transportSchedule;
    }

    public Transport(String transportType, String transportLine, String transportSchedule) {
        this.transportType = transportType;
        this.transportLine = transportLine;
        this.transportSchedule = transportSchedule;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getTransportSchedule() {
        return transportSchedule;
    }

    public void setTransportSchedule(String transportSchedule) {
        this.transportSchedule = transportSchedule;
    }

    public Transport(String transportLine) {
        this.transportLine = transportLine;
    }

    public String getTransportLine() {
        return transportLine;
    }

    public void setTransportLine(String transportLine) {
        this.transportLine = transportLine;
    }

    public int getTransportID() {
        return transportID;
    }

    public void setTransportID(int transportID) {
        this.transportID = transportID;
    }
}
