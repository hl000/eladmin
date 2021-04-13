package me.zhengjie.base;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserMoreDetail extends UserDetails {

    String getUserNickName();

    String getDeptName();

    long getUserId();
}
