package com.example.toni.myapplication.models;

import com.example.toni.myapplication.MyAplication;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by toni on 24/03/2018.
 */

public class Board  extends RealmObject implements RealmModel{
    @PrimaryKey
    private int id;

    @Required
    private String title;

    @Required
    private Date createdAt;


    public Board(){}

    public Board( String title, Date date){
        this.id = MyAplication.BoardId.incrementAndGet();
        this.title = title;
        this.createdAt = date;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


}


