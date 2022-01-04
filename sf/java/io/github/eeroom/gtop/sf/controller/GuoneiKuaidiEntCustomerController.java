package io.github.eeroom.gtop.sf.controller;

import io.github.eeroom.gtop.entity.sf.db.kuaidientcustomer;
import io.github.eeroom.gtop.sf.MyDbContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GuoneiKuaidiEntCustomerController {
    MyDbContext dbContext;
    public GuoneiKuaidiEntCustomerController(MyDbContext dbContext){
        this.dbContext=dbContext;
    }

    public kuaidientcustomer add(kuaidientcustomer entity) throws Throwable {
        var tmp= this.dbContext.dbSet(kuaidientcustomer.class)
                .select()
                .where(x->x.col(a->a.getname()).eq(entity.getname()))
                .firstOrDefault();
        if(tmp!=null)
            throw new RuntimeException("指定的大客户已经存在："+entity.getname());
        this.dbContext.add(entity).setInsertAllCol();
        this.dbContext.saveChange();
        return entity;
    }

    public List<kuaidientcustomer> getEntities() throws Throwable {
        return this.dbContext.dbSet(kuaidientcustomer.class).select().toList();
    }

    public kuaidientcustomer delete(kuaidientcustomer entity) throws Throwable {
        this.dbContext.delete(kuaidientcustomer.class)
                .where(x->x.col(a->a.getid()).eq(entity.getid()));
        this.dbContext.saveChange();
        return entity;
    }
}
