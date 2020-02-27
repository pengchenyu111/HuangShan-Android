package com.example.huangshan.admin.bean;

import java.io.Serializable;

/**
 * 管理员
 */
public class Admin implements Serializable {
    private long id;
    private String account;
    private String password;
    private String name;
    private String roleName;
    private String phone;
    private int workYear;
    private String birth;
    private int age;
    private String sex;
    private String introduction;
    private String headIcon;

    public Admin() {
    }

    public Admin(long id, String account, String password, String name, String roleName, String phone, int workYear, String birth, int age, String sex, String introduction, String headIcon) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.name = name;
        this.roleName = roleName;
        this.phone = phone;
        this.workYear = workYear;
        this.birth = birth;
        this.age = age;
        this.sex = sex;
        this.introduction = introduction;
        this.headIcon = headIcon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getWorkYear() {
        return workYear;
    }

    public void setWorkYear(int workYear) {
        this.workYear = workYear;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", roleName='" + roleName + '\'' +
                ", phone='" + phone + '\'' +
                ", workYear=" + workYear +
                ", birth='" + birth + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", introduction='" + introduction + '\'' +
                ", headIcon='" + headIcon + '\'' +
                '}';
    }
}
