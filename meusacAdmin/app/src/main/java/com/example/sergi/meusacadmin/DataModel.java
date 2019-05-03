package com.example.sergi.meusacadmin;

/**
 * Created by sergi on 4/24/2019.
 */

public class DataModel {

    String name;
    String state;
    String grade;
    String feature;

    public DataModel(String name, String state, String grade, String feature ) {
        this.name=name;
        this.state=state;
        this.grade=grade;
        this.feature=feature;

    }

    public String getName() {
        return name;
    }

    public String getState() { return state; }

    public String getGrade() {
        return grade;
    }

    public String getFeature() {
        return feature;
    }

}
