package com.app.mygame.usePre.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<String> userLastName = new MutableLiveData<>();
    private final MutableLiveData<String> userAddress = new MutableLiveData<>();
    private final MutableLiveData<String> userAdhar = new MutableLiveData<>();
    private final MutableLiveData<String> userAge = new MutableLiveData<>();
    private final MutableLiveData<String> userPhone = new MutableLiveData<>();
    private final MutableLiveData<String> userEmail = new MutableLiveData<>();
    private final MutableLiveData<String> userPan = new MutableLiveData<>();
    private final MutableLiveData<String> ifscCode = new MutableLiveData<>();
    private final MutableLiveData<String> bankName = new MutableLiveData<>();
    private final MutableLiveData<String> bankAccountNumber = new MutableLiveData<>();
    private final MutableLiveData<String> upi = new MutableLiveData<>();
    private final MutableLiveData<String> gender = new MutableLiveData<>();

    // Getters and setters for each field
    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public MutableLiveData<String> getUserLastName() {
        return userLastName;
    }

    public MutableLiveData<String> getUserAddress() {
        return userAddress;
    }

    public MutableLiveData<String> getUserAdhar() {
        return userAdhar;
    }

    public MutableLiveData<String> getUserAge() {
        return userAge;
    }

    public MutableLiveData<String> getUserPhone() {
        return userPhone;
    }

    public MutableLiveData<String> getUserEmail() {
        return userEmail;
    }

    public MutableLiveData<String> getUserPan() {
        return userPan;
    }

    public MutableLiveData<String> getIfscCode() {
        return ifscCode;
    }

    public MutableLiveData<String> getBankName() {
        return bankName;
    }

    public MutableLiveData<String> getBankAccountNumber() {
        return bankAccountNumber;
    }

    public MutableLiveData<String> getUpi() {
        return upi;
    }

    public MutableLiveData<String> getGender() { return gender; }
}
