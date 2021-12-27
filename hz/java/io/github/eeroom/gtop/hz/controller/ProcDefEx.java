package io.github.eeroom.gtop.hz.controller;

import io.github.eeroom.gtop.entity.hz.db.procdefex;
import io.github.eeroom.gtop.hz.authen.CurrentUserInfo;
import io.github.eeroom.gtop.hz.MyDbContext;
import io.github.eeroom.nalu.Columns;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ProcDefEx {
    CurrentUserInfo currentUserInfo;

    MyDbContext dbContext;
    public ProcDefEx(CurrentUserInfo currentUserInfo, MyDbContext dbContext){
        this.currentUserInfo =currentUserInfo;
        this.dbContext=dbContext;
    }

    public procdefex add(procdefex entity) throws Throwable {
        var tmp= this.dbContext.dbSet(procdefex.class)
                .select()
                .where(x->x.col(a->a.getbizName()).eq(entity.getbizName()))
                .firstOrDefault();
        if(tmp!=null)
            throw new RuntimeException("指定的流程名称已经存在："+entity.getbizName());
        this.dbContext.add(entity)
                .setInsertCol(x-> Columns.of(x.getapproveformId(),x.getprocdefKey(),x.getcreateformId(),x.getico(),x.getbizName()));
        this.dbContext.saveChange();
        return entity;
    }

    public List<procdefex> getEntities() {
        return this.dbContext.dbSet(procdefex.class).select().toList();
    }

    public procdefex delete(procdefex entity) {
        this.dbContext.delete(procdefex.class)
                .where(x->x.col(a->a.getbizName()).eq(entity.getbizName()));
        this.dbContext.saveChange();
        return entity;
    }
}
