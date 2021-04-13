package me.zhengjie.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

public class FixRecordInfo {
    //数据库唯一编号
    public long number;
    //报修日期
    public String Fmaintenancedate;
    //基地
    public String Faddress;
    //车辆类型
    public String FCLEIXING;
    //车牌号
    public String Flicensenumber;
    //故障码
    public String Ffaultcode;
    //故障等级
    public String Fdengji;
    //故障大类
    public String Ffailure;
    //故障原因
    public String Ffailure1;
    //解决方案
    public String Fsolution;
    //是否更换零部件
    public String Fsubstitutepart;
    //更换的零部件名称
    public String Fspareparts;
    //是否维修完成
    public String Frepair;
    //未维修完成的原因
    public String Fcause;
    //维修耗时（小时）
    public double Fduration;
    //维修地点
    public String Fplace;
    //维修人员
    public String Fpersonnel;
    //达到现场时间
    public String Fsubmissiondate;
    //填报人姓名
    public String FPname;
    //填报日期
    public String FEstimatedTime;
    //备注
    public String FNote;

    @JsonProperty("Number")
    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
    @JsonProperty("Fmaintenancedate")
    public String getFmaintenancedate() {
        return Fmaintenancedate;
    }

    public void setFmaintenancedate(String fmaintenancedate) {
        Fmaintenancedate = fmaintenancedate;
    }
    @JsonProperty("Faddress")
    public String getFaddress() {
        return Faddress;
    }

    public void setFaddress(String faddress) {
        Faddress = faddress;
    }

    @JsonProperty("FCLEIXING")
    public String getFCLEIXING() {
        return FCLEIXING;
    }

    public void setFCLEIXING(String FCLEIXING) {
        this.FCLEIXING = FCLEIXING;
    }
    @JsonProperty("Flicensenumber")
    public String getFlicensenumber() {
        return Flicensenumber;
    }

    public void setFlicensenumber(String flicensenumber) {
        Flicensenumber = flicensenumber;
    }
    @JsonProperty("Ffaultcode")
    public String getFfaultcode() {
        return Ffaultcode;
    }

    public void setFfaultcode(String ffaultcode) {
        Ffaultcode = ffaultcode;
    }
    @JsonProperty("Fdengji")
    public String getFdengji() {
        return Fdengji;
    }

    public void setFdengji(String fdengji) {
        Fdengji = fdengji;
    }
    @JsonProperty("Ffailure")
    public String getFfailure() {
        return Ffailure;
    }

    public void setFfailure(String ffailure) {
        Ffailure = ffailure;
    }
    @JsonProperty("Ffailure1")
    public String getFfailure1() {
        return Ffailure1;
    }

    public void setFfailure1(String ffailure1) {
        Ffailure1 = ffailure1;
    }
    @JsonProperty("Fsolution")
    public String getFsolution() {
        return Fsolution;
    }

    public void setFsolution(String fsolution) {
        Fsolution = fsolution;
    }
    @JsonProperty("Fsubstitutepart")
    public String getFsubstitutepart() {
        return Fsubstitutepart;
    }

    public void setFsubstitutepart(String fsubstitutepart) {
        Fsubstitutepart = fsubstitutepart;
    }
    @JsonProperty("Fspareparts")
    public String getFspareparts() {
        return Fspareparts;
    }

    public void setFspareparts(String fspareparts) {
        Fspareparts = fspareparts;
    }
    @JsonProperty("Frepair")
    public String getFrepair() {
        return Frepair;
    }

    public void setFrepair(String frepair) {
        Frepair = frepair;
    }
    @JsonProperty("Fcause")
    public String getFcause() {
        return Fcause;
    }

    public void setFcause(String fcause) {
        Fcause = fcause;
    }
    @JsonProperty("Fduration")
    public double getFduration() {
        return Fduration;
    }

    public void setFduration(double fduration) {
        Fduration = fduration;
    }
    @JsonProperty("Fplace")
    public String getFplace() {
        return Fplace;
    }

    public void setFplace(String fplace) {
        Fplace = fplace;
    }
    @JsonProperty("Fpersonnel")
    public String getFpersonnel() {
        return Fpersonnel;
    }

    public void setFpersonnel(String fpersonnel) {
        Fpersonnel = fpersonnel;
    }
    @JsonProperty("Fsubmissiondate")
    public String getFsubmissiondate() {
        return Fsubmissiondate;
    }

    public void setFsubmissiondate(String fsubmissiondate) {
        Fsubmissiondate = fsubmissiondate;
    }
    @JsonProperty("FPname")
    public String getFPname() {
        return FPname;
    }

    public void setFPname(String FPname) {
        this.FPname = FPname;
    }
    @JsonProperty("FEstimatedTime")
    public String getFEstimatedTime() {
        return FEstimatedTime;
    }

    public void setFEstimatedTime(String FEstimatedTime) {
        this.FEstimatedTime = FEstimatedTime;
    }
    @JsonProperty("FNote")
    public String getFNote() {
        return FNote;
    }

    public void setFNote(String FNote) {
        this.FNote = FNote;
    }
}
