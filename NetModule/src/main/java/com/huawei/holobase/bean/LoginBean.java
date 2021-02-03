package com.huawei.holobase.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base
 * @ClassName: LoginBean
 * @Description: java类作用描述
 * @CreateDate: 2020-01-07 10:48
 * @Version: 1.0
 */
public class LoginBean implements Serializable {
    private String token;
    private String tiken;
    private int token_expires_in;
    private int tiken_expires_in;
    private int user_role;      //2=普通用户 ; 1=管理员用户
    private String selfPushElbUrl;
    private int surplus_num;
    private int lock_duration;
    private List<LoginEnterprise> enterprise;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTiken() {
        return tiken;
    }

    public void setTiken(String tiken) {
        this.tiken = tiken;
    }

    public int getToken_expires_in() {
        return token_expires_in;
    }

    public void setToken_expires_in(int token_expires_in) {
        this.token_expires_in = token_expires_in;
    }

    public int getTiken_expires_in() {
        return tiken_expires_in;
    }

    public void setTiken_expires_in(int tiken_expires_in) {
        this.tiken_expires_in = tiken_expires_in;
    }



    public String getSelfPushElbUrl() {
        return selfPushElbUrl;
    }

    public int getUser_role() {
        return user_role;
    }

    public void setUser_role(int user_role) {
        this.user_role = user_role;
    }

    public void setSelfPushElbUrl(String selfPushElbUrl) {
        this.selfPushElbUrl = selfPushElbUrl;
    }

    public int getSurplus_num() {
        return surplus_num;
    }

    public void setSurplus_num(int surplus_num) {
        this.surplus_num = surplus_num;
    }

    public int getLock_duration() {
        return lock_duration;
    }

    public void setLock_duration(int lock_duration) {
        this.lock_duration = lock_duration;
    }

    public List<LoginEnterprise> getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(List<LoginEnterprise> enterprise) {
        this.enterprise = enterprise;
    }

    public class LoginEnterprise implements Serializable{
        private String enterprise_id;
        private String enterprise_name;
        private int user_role;
        private int user_status;        //：0:未激活 1已激活

        public String getEnterprise_id() {
            return enterprise_id;
        }

        public void setEnterprise_id(String enterprise_id) {
            this.enterprise_id = enterprise_id;
        }

        public String getEnterprise_name() {
            return enterprise_name;
        }

        public void setEnterprise_name(String enterprise_name) {
            this.enterprise_name = enterprise_name;
        }

        public int getUser_role() {
            return user_role;
        }

        public void setUser_role(int user_role) {
            this.user_role = user_role;
        }

        public int getUser_status() {
            return user_status;
        }

        public void setUser_status(int user_status) {
            this.user_status = user_status;
        }
    }

}


