package io.github.eeroom.jdbc;

import java.util.Date;

public class Panda {
//    `Id`  int(11) NOT NULL ,
//`Name`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL ,
//`GroupId`  bigint(20) NOT NULL ,
//`LocalAddr`  varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL ,
//`Category`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL ,
//`CreateTime`  datetime NOT NULL ,
    int id;
    String name;

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

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getLocalAddr() {
        return localAddr;
    }

    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    long groupId;
    String localAddr;
    String category;
    Date createTime;

}
