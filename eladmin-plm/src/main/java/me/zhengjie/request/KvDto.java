package me.zhengjie.request;


import java.util.List;

public class KvDto extends Sql{
    public String key = "DIANDUI";
    public String value;
    public String type = "MAIN";
    public String subKey;
    public String subValue;
    public String input;
    public List<KvDto> subList;

    public KvDto() {
    }

    public KvDto(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KvDto(String key, String value, String type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }


    public KvDto(String key, String value, String type, String subKey, String subValue) {
        this.key = key;
        this.value = value;
        this.type = type;
        this.subKey = subKey;
        this.subValue = subValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubKey() {
        return subKey;
    }

    public void setSubKey(String subKey) {
        this.subKey = subKey;
    }

    public String getSubValue() {
        return subValue;
    }

    public void setSubValue(String subValue) {
        this.subValue = subValue;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public List<KvDto> getSubList() {
        return subList;
    }

    public void setSubList(List<KvDto> subList) {
        this.subList = subList;
    }
}
