package io.github.eeroom.sf.controller;

import io.github.eeroom.entity.sfdb.*;
import io.github.eeroom.nalu.Columns;
import io.github.eeroom.sf.LoginUserInfo;
import io.github.eeroom.sf.SfDbContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class BizTypeController {
    LoginUserInfo loginUserInfo;

    SfDbContext dbContext;
    public BizTypeController(LoginUserInfo loginUserInfo, SfDbContext dbContext){
        this.loginUserInfo=loginUserInfo;
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
