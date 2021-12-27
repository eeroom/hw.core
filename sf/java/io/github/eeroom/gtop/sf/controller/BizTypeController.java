package io.github.eeroom.gtop.sf.controller;

import io.github.eeroom.gtop.entity.sf.db.biztype;
import io.github.eeroom.gtop.sf.authen.CurrentUserInfo;
import io.github.eeroom.gtop.sf.MyDbContext;
import io.github.eeroom.nalu.Columns;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class BizTypeController {
    CurrentUserInfo currentUserInfo;

    MyDbContext dbContext;
    public BizTypeController(CurrentUserInfo currentUserInfo, MyDbContext dbContext){
        this.currentUserInfo = currentUserInfo;
        this.dbContext=dbContext;
    }

    public biztype add(biztype entity) throws Throwable {
        var tmp= this.dbContext.dbSet(biztype.class)
                .select()
                .where(x->x.col(a->a.getname()).eq(entity.getname()))
                .firstOrDefault();
        if(tmp!=null)
            throw new IllegalArgumentException("指定的流程类型已经存在："+entity.getname());
        this.dbContext.add(entity)
                .setInsertCol(x-> Columns.of(x.getapproveformId(),x.getcamundaKey(),x.getcreateformId(),x.getico(),x.getname()));
        this.dbContext.saveChange();
        return entity;
    }

    public List<biztype> getEntities() throws Throwable {
        return this.dbContext.dbSet(biztype.class).select().toList();
    }

    public biztype delete(biztype entity) throws Throwable {
        this.dbContext.delete(biztype.class)
                .where(x->x.col(a->a.getid()).eq(entity.getid()));
        this.dbContext.saveChange();
        return entity;
    }
}
