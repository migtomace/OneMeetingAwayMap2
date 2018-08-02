package com.example.lmnop.onemeetingawaymap1;

public class Meeting {

    private String LOCATION;
    private String ADDRESS;
    private String STARTTIME;
    private String DAY;
    private String CODES;
    private String ENDTIME;
    private String MEETINGNAME;
    private String OC;



    public Meeting(){
        this.OC = getOC();
        this.ADDRESS = getADDRESS();
        this.LOCATION = getLOCATION();
        this.CODES = getCODES();
        this.DAY = getDAY();
        this.MEETINGNAME = getMEETINGNAME();
        this.STARTTIME = getSTARTTIME();
        this.ENDTIME = getENDTIME();
    }
    public Meeting(String LOCATION, String ADDRESS, String CODES, String STARTTIME, String ENDTIME, String DAY, String MEETINGNAME, String OC){
        this.OC = OC;
        this.ADDRESS = ADDRESS;
        this.LOCATION = LOCATION;
        this.CODES = CODES;
        this.DAY = DAY;
        this.MEETINGNAME =MEETINGNAME;
        this.STARTTIME = STARTTIME;
        this.ENDTIME = ENDTIME;
    }


    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getSTARTTIME() {
        return STARTTIME;
    }

    public void setSTARTTIME(String STARTTIME) {
        this.STARTTIME = STARTTIME;
    }

    public String getDAY() {
        return DAY;
    }

    public void setDAY(String DAY) {
        this.DAY = DAY;
    }

    public String getCODES() {
        return CODES;
    }

    public void setCODES(String CODES) {
        this.CODES = CODES;
    }

    public String getENDTIME() {
        return ENDTIME;
    }

    public void setENDTIME(String ENDTIME) {
        this.ENDTIME = ENDTIME;
    }

    public String getMEETINGNAME() {
        return MEETINGNAME;
    }

    public void setMEETINGNAME(String MEETINGNAME) {
        this.MEETINGNAME = MEETINGNAME;
    }

    public String getOC() {
        return OC;
    }

    public void setOC(String OC) {
        this.OC = OC;
    }
}
