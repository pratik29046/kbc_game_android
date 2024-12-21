package com.app.mygame.userPost.model;

public class Plan {
    private int planId;
    private String purchaseId;
    private String description;
    private String imageUrl;
    private boolean active;
    private String name;
    private double amount;
    private int coin;
    private double discountPercentage;
    private String createTime;
    private String modifyTime;

    public Plan(int planId, String purchaseId, String description, String imageUrl, boolean active, String name, double amount, int coin, double discountPercentage, String createTime, String modifyTime) {
        this.planId = planId;
        this.purchaseId = purchaseId;
        this.description = description;
        this.imageUrl = imageUrl;
        this.active = active;
        this.name = name;
        this.amount = amount;
        this.coin = coin;
        this.discountPercentage = discountPercentage;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}
