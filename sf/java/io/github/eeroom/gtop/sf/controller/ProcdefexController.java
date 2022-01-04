package io.github.eeroom.gtop.sf.controller;

import io.github.eeroom.gtop.entity.sf.db.procdefex;
import io.github.eeroom.gtop.sf.authen.CurrentUserInfo;
import io.github.eeroom.gtop.sf.MyDbContext;
import io.github.eeroom.nalu.Columns;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ProcdefexController {
    CurrentUserInfo currentUserInfo;

    MyDbContext dbContext;
    public ProcdefexController(CurrentUserInfo currentUserInfo, MyDbContext dbContext){
        this.currentUserInfo =currentUserInfo;
        this.dbContext=dbContext;
    }

    public procdefex add(procdefex entity) throws Throwable {
        var tmp= this.dbContext.dbSet(procdefex.class)
                .select()
                .where(x->x.col(a->a.getprocdefKey()).eq(entity.getprocdefKey()))
                .firstOrDefault();
        if(tmp!=null)
            throw new RuntimeException("指定的procdefKey已经存在："+entity.getprocdefKey());
        //这里还需要增加对procedefkey的校验,必须是存在的key
        this.dbContext.add(entity).setInsertCol();
        this.dbContext.saveChange();
        return entity;
    }

    public List<procdefex> getEntities() {
        return this.dbContext.dbSet(procdefex.class).select().toList();
    }

    public procdefex delete(procdefex entity) {
        this.dbContext.delete(procdefex.class)
                .where(x->x.col(a->a.getprocdefKey()).eq(entity.getprocdefKey()));
        this.dbContext.saveChange();
        return entity;
    }
}
