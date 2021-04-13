package me.zhengjie.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ExpStackInfo {

    public long FID;

    //提交日期，后端生成
    public Long FSubmitDate;

    //基地，暂时默认值，可不填
    public String FBase = "嘉善";

    //提交者，后端根据账户会自动获取
    public String FSubmitter;

    //修改信息，后台根据日期和账户自动填充
    public String FModifier;

    //电堆编号
    public String FNumber;
    //电堆节数
    public String FJieshu;

    //BIP编号
    public String FBIP;
    //MEA型号
    public String FMEA;
   //MEA日期
    public String FMEADate;
    //碳纸类型
    public String FTanzhi;


    //活化日期
    public String FDate;
    //活化次数
    public int FCishu;
    //测试员
    public String FEmp;
    //是否加压
    public String FJiaya;
    //组装力
    public String FZuzhaung;
    //组装人员
    public String FZuzhaungEmp;
    //500电密总电压
    public double F500V;
    //500电密总功率
    public double F500KW;

    public double F600V;

    public double F600KW;

    public double F700V;

    public double F700KW;

    public double F800V;

    public double F800KW;

    public double F900V;

    public double F900KW;

    public double F1000V;

    public double F1000KW;

    //平均电压
    public String FPingjunV;

    //最低节数 最低电压1
    public String FZuidi1;
    //最低节数 最低电压2
    public String FZuidi2;
    //最低节数 最低电压3
    public String FZuidi3;

    //备注
    public String FNote;

    @JsonProperty("FID")
    public long getFID() {
        return FID;
    }

    public void setFID(long FID) {
        this.FID = FID;
    }
    @JsonProperty("FSubmitDate")
    public Long getFSubmitDate() {
        return FSubmitDate;
    }

    public void setFSubmitDate(Long FSubmitDate) {
        this.FSubmitDate = FSubmitDate;
    }
    @JsonProperty("FBase")
    public String getFBase() {
        return FBase;
    }

    public void setFBase(String FBase) {
        this.FBase = FBase;
    }
    @JsonProperty("FSubmitter")
    public String getFSubmitter() {
        return FSubmitter;
    }

    public void setFSubmitter(String FSubmitter) {
        this.FSubmitter = FSubmitter;
    }
    @JsonProperty("FModifier")
    public String getFModifier() {
        return FModifier;
    }

    public void setFModifier(String FModifier) {
        this.FModifier = FModifier;
    }
    @JsonProperty("FNumber")
    public String getFNumber() {
        return FNumber;
    }

    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }
    @JsonProperty("FJieshu")
    public String getFjieshu() {
        return FJieshu;
    }

    public void setFjieshu(String fjieshu) {
        FJieshu = fjieshu;
    }
    @JsonProperty("FBIP")
    public String getFBIP() {
        return FBIP;
    }

    public void setFBIP(String FBIP) {
        this.FBIP = FBIP;
    }
    @JsonProperty("FMEA")
    public String getFMEA() {
        return FMEA;
    }

    public void setFMEA(String FMEA) {
        this.FMEA = FMEA;
    }
    @JsonProperty("FMEADate")
    public String getFMEADate() {
        return FMEADate;
    }

    public void setFMEADate(String FMEADate) {
        this.FMEADate = FMEADate;
    }
    @JsonProperty("FTanzhi")
    public String getFTanzhi() {
        return FTanzhi;
    }

    public void setFTanzhi(String FTanzhi) {
        this.FTanzhi = FTanzhi;
    }
    @JsonProperty("FDate")
    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }
    @JsonProperty("FCishu")
    public int getFCishu() {
        return FCishu;
    }

    public void setFCishu(int FCishu) {
        this.FCishu = FCishu;
    }
    @JsonProperty("FEmp")
    public String getFEmp() {
        return FEmp;
    }

    public void setFEmp(String FEmp) {
        this.FEmp = FEmp;
    }
    @JsonProperty("FJiaya")
    public String getFJiaya() {
        return FJiaya;
    }

    public void setFJiaya(String FJiaya) {
        this.FJiaya = FJiaya;
    }
    @JsonProperty("FZuzhaung")
    public String getFZuzhaung() {
        return FZuzhaung;
    }

    public void setFZuzhaung(String FZuzhaung) {
        this.FZuzhaung = FZuzhaung;
    }
    @JsonProperty("FZuzhaungEmp")
    public String getFZuzhaungEmp() {
        return FZuzhaungEmp;
    }

    public void setFZuzhaungEmp(String FZuzhaungEmp) {
        this.FZuzhaungEmp = FZuzhaungEmp;
    }
    @JsonProperty("F500V")
    public double getF500V() {
        return F500V;
    }

    public void setF500V(double f500V) {
        F500V = f500V;
    }
    @JsonProperty("F500KW")
    public double getF500KW() {
        return F500KW;
    }

    public void setF500KW(double f500KW) {
        F500KW = f500KW;
    }
    @JsonProperty("F600V")
    public double getF600V() {
        return F600V;
    }

    public void setF600V(double f600V) {
        F600V = f600V;
    }
    @JsonProperty("F600KW")
    public double getF600KW() {
        return F600KW;
    }

    public void setF600KW(double f600KW) {
        F600KW = f600KW;
    }
    @JsonProperty("F700V")
    public double getF700V() {
        return F700V;
    }

    public void setF700V(double f700V) {
        F700V = f700V;
    }
    @JsonProperty("F700KW")
    public double getF700KW() {
        return F700KW;
    }

    public void setF700KW(double f700KW) {
        F700KW = f700KW;
    }
    @JsonProperty("F800V")
    public double getF800V() {
        return F800V;
    }

    public void setF800V(double f800V) {
        F800V = f800V;
    }
    @JsonProperty("F800KW")
    public double getF800KW() {
        return F800KW;
    }

    public void setF800KW(double f800KW) {
        F800KW = f800KW;
    }
    @JsonProperty("F900V")
    public double getF900V() {
        return F900V;
    }

    public void setF900V(double f900V) {
        F900V = f900V;
    }
    @JsonProperty("F900KW")
    public double getF900KW() {
        return F900KW;
    }

    public void setF900KW(double f900KW) {
        F900KW = f900KW;
    }
    @JsonProperty("F1000V")
    public double getF1000V() {
        return F1000V;
    }

    public void setF1000V(double f1000V) {
        F1000V = f1000V;
    }
    @JsonProperty("F1000KW")
    public double getF1000KW() {
        return F1000KW;
    }

    public void setF1000KW(double f1000KW) {
        F1000KW = f1000KW;
    }
    @JsonProperty("FPingjunV")
    public String getFPingjunV() {
        return FPingjunV;
    }

    public void setFPingjunV(String FPingjunV) {
        this.FPingjunV = FPingjunV;
    }
    @JsonProperty("FZuidi1")
    public String getFZuidi1() {
        return FZuidi1;
    }

    public void setFZuidi1(String FZuidi1) {
        this.FZuidi1 = FZuidi1;
    }
    @JsonProperty("FZuidi2")
    public String getFZuidi2() {
        return FZuidi2;
    }

    public void setFZuidi2(String FZuidi2) {
        this.FZuidi2 = FZuidi2;
    }
    @JsonProperty("FZuidi3")
    public String getFZuidi3() {
        return FZuidi3;
    }

    public void setFZuidi3(String FZuidi3) {
        this.FZuidi3 = FZuidi3;
    }
    @JsonProperty("FNote")
    public String getFNote() {
        return FNote;
    }

    public void setFNote(String FNote) {
        this.FNote = FNote;
    }
}
