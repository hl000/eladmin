package me.zhengjie.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

public class StackReplaceInfo {
    //数据库唯一编号
    public long FID;
    //更换日期
    public String FDATE;
    //车牌号
    public String FCHEPAI;
    //基地
    public String Fjidi;
    //车辆类型
    public String Fleixing;
    //故障类别
    public String FCATE;
    //故障码
    public String Fdaima;
    //故障等级
    public String Fjibie;
    //故障原因
    public String FYUANYIN;
    //原电堆编号
    public String FYBIANHAO;
    //原系统编号
    public String Fyuanxitong;
    //原电堆行驶里程数
    public double FGONGLI;
    //新电堆编号
    public String FGBIANHAO;
    //新系统编号
    public String Fxinxitong;
    //维修耗时（小时）
    public double FGONGSHI;
    //维修人员
    public String FRENYUAN;
    //填报人姓名
    public String FPname;
    //填报日期
    public String FEstimatedTime;
    //备注
    public String FNOTE1;
    //时间
    public double FSHIJIAN;

    @JsonProperty("FID")
    public long getFID() {
        return FID;
    }
    @JsonProperty("FDATE")
    public String getFDATE() {
        return FDATE;
    }
    @JsonProperty("FCHEPAI")
    public String getFCHEPAI() {
        return FCHEPAI;
    }
    @JsonProperty("Fjidi")
    public String getFjidi() {
        return Fjidi;
    }
    @JsonProperty("Fleixing")
    public String getFleixing() {
        return Fleixing;
    }
    @JsonProperty("FCATE")
    public String getFCATE() {
        return FCATE;
    }
    @JsonProperty("Fdaima")
    public String getFdaima() {
        return Fdaima;
    }
    @JsonProperty("Fjibie")
    public String getFjibie() {
        return Fjibie;
    }
    @JsonProperty("FYUANYIN")
    public String getFYUANYIN() {
        return FYUANYIN;
    }
    @JsonProperty("FYBIANHAO")
    public String getFYBIANHAO() {
        return FYBIANHAO;
    }
    @JsonProperty("Fyuanxitong")
    public String getFyuanxitong() {
        return Fyuanxitong;
    }
    @JsonProperty("FGONGLI")
    public double getFGONGLI() {
        return FGONGLI;
    }
    @JsonProperty("FGBIANHAO")
    public String getFGBIANHAO() {
        return FGBIANHAO;
    }
    @JsonProperty("Fxinxitong")
    public String getFxinxitong() {
        return Fxinxitong;
    }
    @JsonProperty("FGONGSHI")
    public double getFGONGSHI() {
        return FGONGSHI;
    }
    @JsonProperty("FRENYUAN")
    public String getFRENYUAN() {
        return FRENYUAN;
    }
    @JsonProperty("FNOTE1")
    public String getFNOTE1() {
        return FNOTE1;
    }
    @JsonProperty("FSHIJIAN")
    public double getFSHIJIAN() {
        return FSHIJIAN;
    }

    public String getFPname() {
        return FPname;
    }

    public void setFID(long FID) {
        this.FID = FID;
    }

    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    public void setFCHEPAI(String FCHEPAI) {
        this.FCHEPAI = FCHEPAI;
    }

    public void setFjidi(String fjidi) {
        Fjidi = fjidi;
    }

    public void setFleixing(String fleixing) {
        Fleixing = fleixing;
    }

    public void setFCATE(String FCATE) {
        this.FCATE = FCATE;
    }

    public void setFdaima(String fdaima) {
        Fdaima = fdaima;
    }

    public void setFjibie(String fjibie) {
        Fjibie = fjibie;
    }

    public void setFYUANYIN(String FYUANYIN) {
        this.FYUANYIN = FYUANYIN;
    }

    public void setFYBIANHAO(String FYBIANHAO) {
        this.FYBIANHAO = FYBIANHAO;
    }

    public void setFyuanxitong(String fyuanxitong) {
        Fyuanxitong = fyuanxitong;
    }

    public void setFGONGLI(double FGONGLI) {
        this.FGONGLI = FGONGLI;
    }

    public void setFGBIANHAO(String FGBIANHAO) {
        this.FGBIANHAO = FGBIANHAO;
    }

    public void setFxinxitong(String fxinxitong) {
        Fxinxitong = fxinxitong;
    }

    public void setFGONGSHI(double FGONGSHI) {
        this.FGONGSHI = FGONGSHI;
    }

    public void setFRENYUAN(String FRENYUAN) {
        this.FRENYUAN = FRENYUAN;
    }

    public void setFPname(String FPname) {
        this.FPname = FPname;
    }

    public void setFEstimatedTime(String FEstimatedTime) {
        this.FEstimatedTime = FEstimatedTime;
    }

    public void setFNOTE1(String FNOTE1) {
        this.FNOTE1 = FNOTE1;
    }

    public String getFEstimatedTime() {
        return FEstimatedTime;
    }

    public void setFSHIJIAN(double FSHIJIAN) {
        this.FSHIJIAN = FSHIJIAN;
    }
}
