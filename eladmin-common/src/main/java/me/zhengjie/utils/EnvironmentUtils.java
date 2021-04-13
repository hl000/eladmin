package me.zhengjie.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EnvironmentUtils {
    private static ApplicationContext context;
    private static String env = "dev";

    public static void setApplication(ApplicationContext ct) {
        context =ct;
        Environment envs = context.getEnvironment();
        String[] activeProfiles = envs.getActiveProfiles();
        String ip = "";
        try {
             ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
        }
        for (String profile : activeProfiles) {
            if ("dev".equals(profile)) {
                env = "dev";
                break;
            }else if ("test".equals(profile)) {
                env = "test";
                break;
            } else if ("prod".equals(profile)) {
                env = "prod";
                break;
            }
        }
    }

    public static String getEnv () {
        return env;
    }

    public static boolean isProd() {
        return env.equals("prod");
    }
}
