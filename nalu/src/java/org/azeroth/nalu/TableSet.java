package org.azeroth.nalu;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class TableSet<T> {
    static HashMap<String,HashMap<String,Method>> dictSetMethod=new HashMap<>();
    static HashMap<String,HashMap<String,Method>> dictGetMethod=new HashMap<>();

    Class<T> meta;
    T entityIdentity;
    PropertyNameHandler handler;
    T entityProxy;
    String tableName;
    String tableAlias;
    //这是为了涉及where条件的时候，执行lambda的时候，得到该lamda对应的表和列
    ArrayList<Tuple.Tuple2<TableSet<?>,String>> lstTarget=new ArrayList<>();

    TableSet(){

    }

    TableSet(Class<T> meta) throws Throwable {
        this.meta=meta;
        this.entityIdentity=meta.getConstructor(null).newInstance(null);
        this.handler=new PropertyNameHandler(this,this.entityIdentity);
        //使用cglib创建class的代理对象，java自带的代理类只能创建基于接口的代理对象，
        //这里是创建数据库model的pojo对象的代理，没有接口，所以需要使用cglib的
        this.entityProxy= (T)org.springframework.cglib.proxy.Enhancer.create(meta,this.handler);
        var ant= this.meta.getAnnotation(Table.class);
        if(ant==null){
            this.tableName=this.meta.getSimpleName();
        }else{
            if(ant.schemal()==null)
                this.tableName=ant.name()==null?this.meta.getSimpleName():ant.name();
            else
                this.tableName=String.format("%s.%s",ant.schemal(),ant.name()==null?this.meta.getSimpleName():ant.name());
        }
        this.tableAlias=this.tableName;
        if(dictSetMethod.get(meta.getName())!=null)
             return;
        HashMap<String, Method> dictset=new HashMap<>();
        HashMap<String,Method> dictget=new HashMap<>();
        var lstmethod=meta.getDeclaredMethods();
        for (var mt:lstmethod){
            if(mt.getName().startsWith("set")){
                dictset.put(mt.getName().substring(3),mt);
            }else  if(mt.getName().startsWith("get")){
                dictget.put(mt.getName().substring(3),mt);
            }
        }
        dictSetMethod.put(meta.getName(),dictset);
        dictGetMethod.put(meta.getName(),dictget);
    }


    //这是为了涉及where条件的时候，执行lambda的时候，得到该lamda对应的表和列
    void onProxyHandlerInvokedHandler(TableSet<?> dbSet, String s) {
        this.lstTarget.add(Tuple.create(dbSet,s));
    }

    /**
     * 这个参数的巧妙之处，join场景下，where条件获取最终目标dbset，
     * 不能不搞参数，直接赋值自己onProxyHandlerInvokedHandler
     * @param handler
     */
    void setProxyHookHandler(MyAction2<TableSet<?>,String> handler){
        this.lstTarget.clear();
        this.handler.onInvoked =handler;
    }
}
