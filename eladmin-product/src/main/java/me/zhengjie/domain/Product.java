package me.zhengjie.domain;


import java.sql.Timestamp;
import java.util.List;

public class Product {

    private String fAddress;

    private Integer fId;

    private Integer fUseId;

    private String userName;

    private String deptName;

    private Timestamp fDate;

    private String fToday;

    private String fTomorrow;

    private String fProduceDate;

    private Long deptId;

    private List<ProductInfo> productList;

    public String getfAddress(){ return fAddress;  }

    public void setfAddress(String fAddress){ this.fAddress = fAddress; }

    public String getfToday(){ return fToday;}

    public void setfToday(String fToday){ this.fToday = fToday;}

    public String getfTomorrow(){ return fTomorrow;}

    public void setfTomorrow(String fTomorrow){ this.fTomorrow = fTomorrow;}

    public String getfProduceDate(){ return fProduceDate;}

    public void setfProduceDate(String fProduceDate){ this.fProduceDate = fProduceDate;}

    public Integer getfId(){ return fId;}

    public void setfId(Integer fId){ this.fId = fId;}

    public Integer getfUseId(){ return fUseId;}

    public void setfUseId(Integer fUseId){ this.fUseId = fUseId;}

    public Timestamp getfDate(){ return fDate;}

    public void setfDate(Timestamp fDate){ this.fDate = fDate;}

    public List<ProductInfo> getProductList(){ return productList;}

    public void setProductList(List<ProductInfo> productList){ this.productList = productList;}

    public String getUserName(){ return userName ;}

    public void setUserName(String userName){ this.userName = userName ; }

    public String getDeptName(){ return deptName;}

    public void setDeptName(String deptName){ this.deptName = deptName; }

    public Long getDeptId(){ return deptId; }

    public void setDeptId(Long deptId){ this.deptId = deptId ; }

}
