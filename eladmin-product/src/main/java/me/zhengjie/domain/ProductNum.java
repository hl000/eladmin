package me.zhengjie.domain;

public class ProductNum {

  //今日组装
  private int assemblyNum;

  //今日活化
  private int activationNum;

  //电堆型号
  private String modelName;

  private int assemblyBalance;

  private int activationBalance;

  public int getAssemblyNum() {
    return assemblyNum;
  }

  public void setAssemblyNum(int assemblyNum) {
    this.assemblyNum = assemblyNum;
  }

  public int getActivationNum() {
    return activationNum;
  }

  public void setActivationNum(int activationNum) {
    this.activationNum = activationNum;
  }

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public int getAssemblyBalance() {
    return assemblyBalance;
  }

  public void setAssemblyBalance(int assemblyBalance) {
    this.assemblyBalance = assemblyBalance;
  }

  public int getActivationBalance() {
    return activationBalance;
  }

  public void setActivationBalance(int activationBalance) {
    this.activationBalance = activationBalance;
  }
}
