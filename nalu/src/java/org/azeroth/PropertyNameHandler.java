package org.azeroth;

import java.lang.reflect.Method;

class PropertyNameHandler implements org.springframework.cglib.proxy.InvocationHandler{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.equals("toString"))
            return;
        this.name = name;
    }

    String name;
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        this.setName(method.getName());
        if(method.getReturnType().getName().equals("java.lang.String"))
             return "";
        return 0;
    }
}
