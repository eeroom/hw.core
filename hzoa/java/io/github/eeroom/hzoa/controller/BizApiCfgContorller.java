package io.github.eeroom.hzoa.controller;

import io.github.eeroom.hzcore.hzoa.db.bizapicfg;
import io.github.eeroom.hzoa.MyDbContext;
import io.github.eeroom.hzoa.authen.CurrentUserInfo;
import io.github.eeroom.nalu.Columns;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class BizApiCfgContorller {
    CurrentUserInfo currentUserInfo;

    MyDbContext dbContext;
    public BizApiCfgContorller(CurrentUserInfo currentUserInfo, MyDbContext dbContext){
        this.currentUserInfo =currentUserInfo;
        this.dbContext=dbContext;
    }

    public bizapicfg add(bizapicfg entity) throws Throwable {
        var tmp= this.dbContext.dbSet(bizapicfg.class)
                .select()
                .where(x->x.col(a->a.getapi()).eq(entity.getapi()))
                .firstOrDefault();
        if(tmp!=null)
            throw new RuntimeException("指定的bizapicfg已经存在："+entity.getapi().name());
        this.dbContext.add(entity)
                .setInsertCol(x-> Columns.of(x.getapi(),x.getprocdefKey()));
        this.dbContext.saveChange();
        return entity;
    }

    public List<bizapicfg> getEntities() {
        return this.dbContext.dbSet(bizapicfg.class).select().toList();
    }

    public bizapicfg delete(bizapicfg entity) {
        this.dbContext.delete(bizapicfg.class)
                .where(x->x.col(a->a.getapi()).eq(entity.getapi()));
        this.dbContext.saveChange();
        return entity;
    }
}