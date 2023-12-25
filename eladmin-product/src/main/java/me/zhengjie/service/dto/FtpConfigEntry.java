package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @author HL
 * @create 2022/9/27 10:35
 */
@Data
public class FtpConfigEntry {
    private String serverAddress;

    private String serverport;

    private String userName;

    private String passWord;

    private String storePath;

    public FtpConfigEntry(String address, String port, String userName, String password, String storePath) {
        this.serverAddress = address;
        this.serverport = port;
        this.userName = userName;
        this.passWord = password;
        this.storePath = storePath;
    }
}
