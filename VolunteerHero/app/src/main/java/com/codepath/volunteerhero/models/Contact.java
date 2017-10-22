package com.codepath.volunteerhero.models;

import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by dharinic on 10/21/17.
 */
@IgnoreExtraProperties
@Parcel(analyze={Contact.class})
public class Contact extends BaseModelWithId implements Serializable {

    public String name;

    public String phone;

    public String email;
    public Contact() {
        //required
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
