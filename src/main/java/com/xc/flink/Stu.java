package com.xc.flink;

import java.io.Serializable;

public class Stu implements Serializable {
    int id;
    String myname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    @Override
    public String toString() {
        return "Stu{" +
                "id=" + id +
                ", myname='" + myname + '\'' +
                '}';
    }
}
