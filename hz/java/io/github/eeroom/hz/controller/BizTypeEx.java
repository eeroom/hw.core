package io.github.eeroom.hz.controller;

import io.github.eeroom.entity.hz.db.biztypeex;
import io.github.eeroom.hz.MyDbContext;
import io.github.eeroom.hz.authen.CurrentUserInfo;
import io.github.eeroom.nalu.Columns;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class BizTypeEx {
    CurrentUserInfo currentUserInfo;

    MyDbContext dbContext;
    public BizTypeEx(CurrentUserInfo currentUserInfo, MyDbContext dbContext){
        this.currentUserInfo =currentUserInfo;
        this.dbContext=dbContext;
    }

    public biztypeex add(biztypeex entity) throws Throwable {
        var tmp= this.dbContext.dbSet(biztypeex.class)
                .select()
                .where(x->x.col(a->a.getbizType()).eq(entity.getbizType()))
                .firstOrDefault();
        if(tmp!=null)
            throw new RuntimeException("指定的流程类型已经存在："+entity.getbizType().name());
        this.dbContext.add(entity)
                .setInsertCol(x-> Columns.of(x.getapproveformId(),x.getprocdefineKey(),x.getcreateformId(),x.getico(),x.getbizType()));
        this.dbContext.saveChange();
        return entity;
    }

    public List<biztypeex> getEntities() {
        return this.dbContext.dbSet(biztypeex.class).select().toList();
    }

    public biztypeex delete(biztypeex entity) {
        this.dbContext.delete(biztypeex.class)
                .where(x->x.col(a->a.getbizType()).eq(entity.getbizType()));
        this.dbContext.saveChange();
        return entity;
    }
}
