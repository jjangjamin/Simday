package com.example.q.simday.Fragments;

class Contact {
    public String name, phone, profileImage;

    public Contact(String objName, String obj_phone, String obj_name) {
        this.name = name;
        this.phone = "";
        this.profileImage = "";
    }

    public Contact(String name) {
        this.name = name;
        this.phone = phone;
        this.profileImage = profileImage;
    }

    public Contact setName(String name) {
        this.name = name;
        return this;
    }

    public Contact setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Contact setProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }
}