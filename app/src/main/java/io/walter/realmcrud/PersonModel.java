package io.walter.realmcrud;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by walter on 10/23/17.
 */

public class PersonModel extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private  String email;
    private  String address;
    private int age;

    public PersonModel() {
    }

    public PersonModel(int id, String name, String email, String address, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
