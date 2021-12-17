package io.github.eeroom.nalu;

import java.util.ArrayList;

public class PagingList<T> {
    ArrayList<T> lst;

    public ArrayList<T> getLst() {
        return lst;
    }

    public void setLst(ArrayList<T> lst) {
        this.lst = lst;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    int count;
}
