package org.azeroth.nalu;

import java.lang.reflect.Method;

class PropertyNameHandler implements org.springframework.cglib.proxy.InvocationHandler{
    DbSet<?> dbSet;
    Object target;
    MyAction2<DbSet<?>,String> invokeCallback;
    public PropertyNameHandler(DbSet<?> dbSet,Object target){
        this.dbSet=dbSet;
        this.target=target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.equals("toString"))
            return;
        this.name = name.substring(3);
    }

    String name;
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        this.setName(method.getName());
        var rt=method.invoke(this.target,objects);
        if(invokeCallback!=null)
            invokeCallback.execute(this.dbSet,this.name);
        return rt;
    }
}
