package io.github.eeroom.javacore.obs;

import java.util.Date;

public class FileInfo {

    public  FileInfo(String fullPath, Long size, Date lastmodifyDate){
        this.fullPath=fullPath;
        this.size=size;
        this.lastmodifyDate=lastmodifyDate;
    }

    String fullPath;

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getLastmodifyDate() {
        return lastmodifyDate;
    }

    public void setLastmodifyDate(Date lastmodifyDate) {
        this.lastmodifyDate = lastmodifyDate;
    }

    Long size;
    Date lastmodifyDate;

}