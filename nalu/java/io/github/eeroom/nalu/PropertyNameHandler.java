package io.github.eeroom.nalu;

import java.lang.reflect.Method;

class PropertyNameHandler implements org.springframework.cglib.proxy.InvocationHandler{
    TableSet<?> dbSet;
    Object target;
    MyAction2<TableSet<?>,String> onInvoked;
    PropertyNameHandler(TableSet<?> dbSet,Object target){
        this.dbSet=dbSet;
        this.target=target;
    }

    String getName() {
        return this.name;
    }

    void setName(String thename) {
        if(thename.equals("toString"))
            return;
        if(DbContext.ColNameGetter!=null)
            this.name=DbContext.ColNameGetter.apply(thename);
        else
            this.name = thename.substring(3);
    }

    String name;
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        this.setName(method.getName());
        var rt=method.invoke(this.target,objects);
        if(onInvoked !=null)
            onInvoked.execute(this.dbSet,this.name);
        return rt;
    }
}
