package com.mine.ecomm.accountservice.entity;

public interface AccountEntity {
    public void setEmailId(String emailId);

    public void setName(String name);

    public void setPassword(String password);

    public void setPhoneNumber(String phoneNumber);
    public String getEmailId();

    public String getName();

    public String getPassword();

    public String getPhoneNumber();
}
