package me.zhengjie.domain;

import lombok.Data;

@Data
public class MachineOriginAction {
    public int number;
    public long dayId;
    public int hour;
    public String ip;
    public String location;
    public String deviceName;
    public long count;
    public String addTime;


}
