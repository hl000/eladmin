package me.zhengjie.request;

public class Sql {
    public String table;
    public String extra = "";
    public long fromIndex;
    public long endIndex;
    public String sql = " select DISTINCT\n" +
            "        t1.增湿器 as zsq,\n" +
            "        t1.循环泵 as xhb,\n" +
            "        t1.循环泵控制器 as xhbcontroller,\n" +
            "        t1.空气泵 as kqb,\n" +
            "        t1.空气泵控制器 as kqbcontroller,\n" +
            "        t1.电堆 as diandui,\n" +
            "        t1.系统 as system,\n" +
            "        t2.cinvcode zsqcode,\n" +
            "        t2.cinvname zsqname,\n" +
            "        T2.cinvstd zsqguige,\n" +
            "        t3.cvencode zsqgyscode,\n" +
            "        t4.cvenname zsqgysname,\n" +
            "        t5.cinvcode xhbcode,\n" +
            "        t5.cinvname xhbname,\n" +
            "        T5.cinvstd xhbguige,\n" +
            "        t6.cvencode xhbgyscode,\n" +
            "        t7.cvenname xhbgysname,\n" +
            "        t8.cinvcode kqbcode,\n" +
            "        t8.cinvname kqbname,\n" +
            "        t8.cinvstd kqbguige,\n" +
            "        t9.cvencode kqbgyscode,\n" +
            "        t10.cvenname kqbgysname,\n" +
            "        t11.fsjb sjb,\n" +
            "        T11.FMEA  MEA,\n" +
            "        t12.cinvcode jxcode  ,\n" +
            "        t12.cinvname jxname,\n" +
            "        t12.cinvstd jxguige,\n" +
            "        T13.cvencode jxgyscode,\n" +
            "        t14.cvenname jxgysname,\n" +
            "        t15.cinvcode gbcode  ,\n" +
            "        t15.cinvname gbname,\n" +
            "        t15.cinvstd gbguige,\n" +
            "        T16.cvencode gbgyscode,\n" +
            "        t17.cvenname gbgysname,\n" +
            "        T18.CINVCODE meacode  ,\n" +
            "        T18.CINVNAME meaname,\n" +
            "        T18.CINVSTD meaguige,\n" +
            "        T19.cvencode  meagyscode,\n" +
            "        t20.cvenname meagysname,\n" +
            "        T21.FNUMBER chepai,\n" +
            "        T22.FPingjunV csavrdy,\n" +
            "        T22.FZuidi1 cslowestdy1,\n" +
            "        T22.FZuidi2 cslowestdy2,\n" +
            "        T22.FZuidi3 cslowestdy3,\n" +
            "        T23.Fmaintenancedate baoxiudate,\n" +
            "        T23.Fplace baoxiulocation,\n" +
            "        Ffaultcode errorcode,\n" +
            "        Ffailure1 errormsg,\n" +
            "        Fsolution errormethod,\n" +
            "        Fsubstitutepart isrelacepart,\n" +
            "        Fspareparts repartname,\n" +
            "        Frepair isfixed,\n" +
            "        Fcause notfixcause,\n" +
            "        Faddress base,\n" +
            "        Ffailure errorcate,\n" +
            "        FCLEIXING carcate\n" +
            "        from (\n" +
            "        select FZSQ 增湿器 ,\n" +
            "        FXHB 循环泵,\n" +
            "        FXHBK 循环泵控制器,\n" +
            "        FKQB 空气泵,\n" +
            "        FKQBK 空气泵控制器,\n" +
            "        FDD 电堆,\n" +
            "        FXT 系统 from  [CEMT1]..[ADMXT] )t1\n" +
            "        left join\n" +
            "        ( select t1.fzsq,\n" +
            "        t2.cinvcode,\n" +
            "        t2.cinvname,\n" +
            "        cinvstd\n" +
            "        from [CEMT1]..[ADMZSQ] t1\n" +
            "        left join [UFDATA_004_2016]..[Inventory] t2\n" +
            "        on t1.fzcode=t2.cinvcode   ) t2 on t1.增湿器=t2.fzsq\n" +
            "        left join\n" +
            "        (select max(t1.cvencode)cvencode,\n" +
            "        t2.cinvcode from [UFDATA_004_2016]..PO_Pomain t1\n" +
            "        inner  join [UFDATA_004_2016]..PO_Podetails T2\n" +
            "        on t1.poid=t2.poid group by t2.cinvcode)t3  on t3.cinvcode=t2.cinvcode\n" +
            "        left join\n" +
            "        (select * from [UFDATA_004_2016]..[Vendor])t4 on t3.cvencode=t4.cvencode\n" +
            "        left join\n" +
            "        ( select t1.fZCODE,\n" +
            "        t2.cinvcode,\n" +
            "        t2.cinvname,\n" +
            "        cinvstd from [CEMT1]..[ADMXHB] t1\n" +
            "        left join [UFDATA_004_2016]..[Inventory] t2\n" +
            "        on t1.fZSQ=t2.cinvcode   ) t5 on t1.循环泵=t5.fZCODE\n" +
            "        left join\n" +
            "        (select max(t1.cvencode)cvencode,\n" +
            "        t2.cinvcode from [UFDATA_004_2016]..PO_Pomain t1\n" +
            "        inner  join [UFDATA_004_2016]..PO_Podetails T2\n" +
            "        on t1.poid=t2.poid group by t2.cinvcode)t6  on t6.cinvcode=t5.cinvcode\n" +
            "        left join\n" +
            "        (select * from [UFDATA_004_2016]..[Vendor])t7 on t7.cvencode=t6.cvencode\n" +
            "        left join\n" +
            "        ( select t1.fZCODE,\n" +
            "        t2.cinvcode,\n" +
            "        t2.cinvname,\n" +
            "        cinvstd from [CEMT1]..[ADMKQB] t1\n" +
            "        left join [UFDATA_004_2016]..[Inventory] t2\n" +
            "        on t1.fZSQ=t2.cinvcode   ) t8 on t1.空气泵=t8.fZCODE\n" +
            "        left join\n" +
            "        (select max(t1.cvencode)cvencode,\n" +
            "        t2.cinvcode from [UFDATA_004_2016]..PO_Pomain t1\n" +
            "        inner  join [UFDATA_004_2016]..PO_Podetails T2\n" +
            "        on t1.poid=t2.poid group by t2.cinvcode)t9  on t9.cinvcode=t8.cinvcode\n" +
            "        left join\n" +
            "        (select * from [UFDATA_004_2016]..[Vendor])t10 on t10.cvencode=t9.cvencode\n" +
            "        left join (select * from [CEMT1]..[ADMDD]) t11 on t1.电堆=t11.fdd\n" +
            "        left join (select t1.fxh,\n" +
            "        t2.cinvcode,\n" +
            "        t2.cinvname,\n" +
            "        cinvstd\n" +
            "        from [CEMT1]..[ADMSJB] t1\n" +
            "        left join [UFDATA_004_2016]..[Inventory] t2\n" +
            "        on t1.fjx=t2.cinvcode ) t12 on t12.fxh=t11.fsjb\n" +
            "        left join\n" +
            "        (select max(t1.cvencode)cvencode,\n" +
            "        t2.cinvcode from [UFDATA_004_2016]..PO_Pomain t1\n" +
            "        inner  join [UFDATA_004_2016]..PO_Podetails T2\n" +
            "        on t1.poid=t2.poid group by t2.cinvcode)t13  on t13.cinvcode=t12.cinvcode\n" +
            "        left join\n" +
            "        (select * from [UFDATA_004_2016]..[Vendor])t14 on t14.cvencode=t13.cvencode\n" +
            "        left join (select t1.fXH,\n" +
            "        t2.cinvcode,\n" +
            "        t2.cinvname,\n" +
            "        cinvstd from [CEMT1]..[ADMSJB] t1\n" +
            "        left join [UFDATA_004_2016]..[Inventory] t2\n" +
            "        on t1.fGB=t2.cinvcode ) t15 on t15.fXH=t11.fsjb\n" +
            "        left join\n" +
            "        (select max(t1.cvencode)cvencode,\n" +
            "        t2.cinvcode from [UFDATA_004_2016]..PO_Pomain t1\n" +
            "        inner  join [UFDATA_004_2016]..PO_Podetails T2\n" +
            "        on t1.poid=t2.poid group by t2.cinvcode)t16  on t16.cinvcode=t15.cinvcode\n" +
            "        left join\n" +
            "        (select * from [UFDATA_004_2016]..[Vendor])t17 on t17.cvencode=t16.cvencode\n" +
            "        --------MEA\n" +
            "        LEFT JOIN\n" +
            "        (SELECT T1.FZHIJIAN,\n" +
            "        T2.CINVCODE,\n" +
            "        T2.CINVNAME,\n" +
            "        T2.CINVSTD FROM   [CEMT1]..[ADMMEA] T1\n" +
            "        Left join [UFDATA_004_2016]..[Inventory] t2\n" +
            "        on t1.FZIJIAN=t2.cinvcode) T18 ON LEFT(T11.FMEA,3)=T18.FZHIJIAN\n" +
            "        left join\n" +
            "        (select max(t1.cvencode)cvencode,\n" +
            "        t2.cinvcode from [UFDATA_004_2016]..PO_Pomain t1\n" +
            "        inner  join [UFDATA_004_2016]..PO_Podetails T2\n" +
            "        on t1.poid=t2.poid group by t2.cinvcode)t19  on t19.cinvcode=t18.cinvcode\n" +
            "        left join\n" +
            "        (select * from [UFDATA_004_2016]..[Vendor])t20 on t20.cvencode=t19.cvencode\n" +
            "        left join\n" +
            "        (select * from [CEMT]..[ADMCLXTBH])t21 on t21.fxitong=t1.系统\n" +
            "\n" +
            "        left join\n" +
            "        (select * from [CEMT]..[ADMhuohua])t22 on t22.fnumber=t1.电堆\n" +
            "        LEFT JOIN\n" +
            "        (select * from [CEMT]..[ADMSHWX])t23 on t23.Flicensenumber=t21.FNUMBER";

    public String getSql() {
        return sql;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public long getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(long fromIndex) {
        this.fromIndex = fromIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }
}
