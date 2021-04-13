package me.zhengjie.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ExpSystemInfo {

    public long FID;

    //提交日期，后端生成
    public Long FSubmitDate;

    //基地，暂时默认值，可不填
    public String FBase = "嘉善";

    //提交者，后端根据账户会自动获取
    public String FSubmitter;

    //修改信息，后台根据日期和账户自动填充
    public String FModifier;
    //活化日期
    public String FDate;
    //电堆编号
    public String FNumber;
    //电堆节数
    public String FJieshu;
    //测试员
    public String FEmp;

    //电密电压
    public double F100V;
    public double F200V;
    public double F300V;
    public double F400V;
    public double F500V;
    public double F600V;
    public double F700V;
    public double F800V;
    public double F900V;
    public double F1000V;



    //最低节数
    public String FZuidiID;
    //最低电压
    public String FZuidiV;
    //用途
    public String Fusage;

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

    @JsonProperty("FDate")
    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
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
    public String getFJieshu() {
        return FJieshu;
    }

    public void setFJieshu(String fjieshu) {
        FJieshu = fjieshu;
    }

    @JsonProperty("FEmp")
    public String getFEmp() {
        return FEmp;
    }

    public void setFEmp(String FEmp) {
        this.FEmp = FEmp;
    }

    @JsonProperty("F100V")
    public double getF100V() {
        return F100V;
    }

    public void setF100V(double f100V) {
        F100V = f100V;
    }

    @JsonProperty("F200V")
    public double getF200V() {
        return F200V;
    }

    public void setF200V(double f200V) {
        F200V = f200V;
    }

    @JsonProperty("F300V")
    public double getF300V() {
        return F300V;
    }

    public void setF300V(double f300V) {
        F300V = f300V;
    }

    @JsonProperty("F400V")
    public double getF400V() {
        return F400V;
    }

    public void setF400V(double f400V) {
        F400V = f400V;
    }

    @JsonProperty("F500V")
    public double getF500V() {
        return F500V;
    }

    public void setF500V(double f500V) {
        F500V = f500V;
    }

    @JsonProperty("F600V")
    public double getF600V() {
        return F600V;
    }

    public void setF600V(double f600V) {
        F600V = f600V;
    }

    @JsonProperty("F700V")
    public double getF700V() {
        return F700V;
    }

    public void setF700V(double f700V) {
        F700V = f700V;
    }

    @JsonProperty("F800V")
    public double getF800V() {
        return F800V;
    }

    public void setF800V(double f800V) {
        F800V = f800V;
    }

    @JsonProperty("F900V")
    public double getF900V() {
        return F900V;
    }

    public void setF900V(double f900V) {
        F900V = f900V;
    }

    @JsonProperty("F1000V")
    public double getF1000V() {
        return F1000V;
    }

    public void setF1000V(double f1000V) {
        F1000V = f1000V;
    }

    @JsonProperty("FNote")
    public String getFNote() {
        return FNote;
    }

    public void setFNote(String FNote) {
        this.FNote = FNote;
    }

    @JsonProperty("FZuidiID")
    public String getFZuidiID() {
        return FZuidiID;
    }

    public void setFZuidiID(String FZuidiID) {
        this.FZuidiID = FZuidiID;
    }

    @JsonProperty("FZuidiV")
    public String getFZuidiV() {
        return FZuidiV;
    }

    public void setFZuidiV(String FZuidiV) {
        this.FZuidiV = FZuidiV;
    }

    @JsonProperty("Fusage")
    public String getFusage() {
        return Fusage;
    }

    public void setFusage(String fusage) {
        Fusage = fusage;
    }


}
