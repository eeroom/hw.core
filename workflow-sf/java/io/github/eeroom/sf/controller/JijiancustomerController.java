package io.github.eeroom.sf.controller;

import io.github.eeroom.entity.sfdb.biztype;
import io.github.eeroom.entity.sfdb.jijiancustomer;
import io.github.eeroom.nalu.Columns;
import io.github.eeroom.sf.LoginUserInfo;
import io.github.eeroom.sf.SfDbContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class JijiancustomerController {
    SfDbContext dbContext;
    public JijiancustomerController( SfDbContext dbContext){
        this.dbContext=dbContext;
    }

    public jijiancustomer add(jijiancustomer entity) throws Throwable {
        var tmp= this.dbContext.dbSet(jijiancustomer.class)
                .select()
                .where(x->x.col(a->a.getname()).eq(entity.getname()))
                .firstOrDefault();
        if(tmp!=null)
            throw new IllegalArgumentException("指定的大客户已经存在："+entity.getname());
        this.dbContext.add(entity).setInsertAllCol();
        this.dbContext.saveChange();
        return entity;
    }

    public List<jijiancustomer> getEntities() throws Throwable {
        return this.dbContext.dbSet(jijiancustomer.class).select().toList();
    }

    public jijiancustomer delete(jijiancustomer entity) throws Throwable {
        this.dbContext.delete(jijiancustomer.class)
                .where(x->x.col(a->a.getid()).eq(entity.getid()));
        this.dbContext.saveChange();
        return entity;
    }
}