package me.zhengjie.constant;

import me.zhengjie.utils.EnvironmentUtils;

public class PlmConstant {
    public static final String K_MAIN = "MAIN";
    public static final String K_MEA = "MEA";
    public static final String K_GZ_PAPER = "GZ_PAPER";
    public static final String K_SHOUHOU = "SHOUHOU";
    public static final String K_CHANPIN = "CHANPIN";
    public static final String K_SB_PAPER = "SB_PAPER";
    public static final String K_XITONG = "SYSTEM";
    public static final String K_BIP = "BIP";
    public static final String K_CESHI = "CESHI";
    public static final String K_STACK_REPLACE = "STACK_REPLACE";
    public static final String K_CAR_MILEAGE = "CAR_MILEAGE";
    public static final String K_CAR_CONSUME = "CAR_CONSUME";



    public static final String K_NO_READ_WARM_INFO = "您暂时没有权限查看";


    public static String get_K_MAIN_TABLE() {
        return EnvironmentUtils.isProd() ? "[CEMT1]..[ADMPLM]" : "[CEMT_TEST]..[ADMPLM]";
    }

    public static String get_K_GZ_PAPER_TABLE() {
        return EnvironmentUtils.isProd() ? "[CEMT]..[ADMgongZhuang]" : "[CEMT_TEST]..[ADMgongZhuang]";
    }

    public static String get_K_MEA_TABLE() {
        return EnvironmentUtils.isProd() ? "[CEMT1]..[ADMPLMMEA]" : "[CEMT_TEST]..[ADMPLMMEA]";
    }

    public static String get_K_SHOUHOU_TABLE() {
        return EnvironmentUtils.isProd() ? "[CEMT]..[ADMSHWX]" : "[CEMT_TEST]..[ADMSHWX]";
    }

    public static String get_K_SELETITEM_TABLE() {
        return EnvironmentUtils.isProd() ? "[CEMT1]..[ADMPLM_FIELD]" : "[CEMT_TEST]..[ADMPLM_FIELD]";
    }

    public static String get_K_CHANPIN_TABLE() {
        return EnvironmentUtils.isProd() ? "[CEMT]..[ADMCHANGPING]" : "[CEMT_TEST]..[ADMCHANGPING]";
    }

    public static String get_K_SB_PAPER_TABLE() {
        return EnvironmentUtils.isProd() ? "[CEMT]..[ADMshebei]" : "[CEMT_TEST]..[ADMshebei]";
    }

    public static String get_K_KNOWLEDGE_FILE() {
        return EnvironmentUtils.isProd() ? "[CEMT1]..[ADMKNFILE]" : "[CEMT_TEST]..[ADMKNFILE]";
    }

    public static String get_K_KNOWLEDGE_DIR_TABLE() {
        return EnvironmentUtils.isProd() ? "[CEMT1]..[ADMKNDIR]" : "[CEMT_TEST]..[ADMKNDIR]";
    }

    public static String get_K_KNOWLEDGE_LOG_TABLE() {
        return EnvironmentUtils.isProd() ? "[CEMT1]..[ADMKNLOG]" : "[CEMT_TEST]..[ADMKNLOG]";
    }

    public static String get_K_PLM_USER_ALLOWED() {
        return EnvironmentUtils.isProd() ? "[CEMT1]..[ADMPLM_MG]" : "[CEMT_TEST]..[ADMPLM_MG]";
    }

    public static String get_K_PLM_STACK_EXP() {
        return  EnvironmentUtils.isProd() ? "[CEMT]..[ADMhuohua]" : "[CEMT_TEST]..[ADMhuohua]";
    }

    public static String get_K_SHOUHOU_GENHUAN() {
        return  EnvironmentUtils.isProd() ? "[CEMT]..[ADMXTGH]" : "[CEMT_TEST]..[ADMXTGH]";
    }

    public static String get_K_PLM_CAR_MILEAGE() {
        return  EnvironmentUtils.isProd() ? "[CEMT]..[ADMlicheng]" : "[CEMT_TEST]..[ADMlicheng]";
    }

    public static String get_K_PLM_CAR_CONSUME() {
        return  EnvironmentUtils.isProd() ? "[CEMT]..[ADMJiaqing]" : "[CEMT_TEST]..[ADMJiaqing]";
    }
}
