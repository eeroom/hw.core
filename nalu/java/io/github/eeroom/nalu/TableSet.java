package io.github.eeroom.nalu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class TableSet<T> {
    static HashMap<String,HashMap<String,MyAction2<Object,Object>>> dictSetMethod=new HashMap<>();
    static HashMap<String,HashMap<String,MyFunction<Object,Object>>> dictGetMethod=new HashMap<>();

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

    TableSet(Class<T> meta)  {
        this.meta=meta;
        try {
            this.entityIdentity=meta.getConstructor(null).newInstance(null);
        } catch (Throwable e) {
            throw new RuntimeException("通过默认构造函数创建model的实例发生错误,请确保model为纯model,并且有无参构造函数，类型名称："+meta.getName(),e);
        }
        this.handler=new PropertyNameHandler(this,this.entityIdentity);
        //使用cglib创建class的代理对象，java自带的代理类只能创建基于接口的代理对象，
        //这里是创建数据库model的pojo对象的代理，没有接口，所以需要使用cglib的
        this.entityProxy= (T)org.springframework.cglib.proxy.Enhancer.create(meta,this.handler);
        var ant= this.meta.getAnnotation(Table.class);
        if(ant==null){
            this.tableName=this.meta.getSimpleName();
        }else{
            if(ant.schema()==null)
                this.tableName=ant.name()==null?this.meta.getSimpleName():ant.name();
            else
                this.tableName=String.format("%s.%s",ant.schema(),ant.name()==null?this.meta.getSimpleName():ant.name());
        }
        this.tableAlias=this.tableName;
        if(dictSetMethod.get(meta.getName())!=null)
             return;
        HashMap<String, MyAction2<Object,Object>> dictset=new HashMap<>();
        HashMap<String,MyFunction<Object,Object>> dictget=new HashMap<>();
        var lstmethod=meta.getDeclaredMethods();
        for (var mt:lstmethod){
            var pName=mt.getName().substring(3);
            if(mt.getName().startsWith("set")){
                dictset.put(pName,this.createSetHandler(pName,mt));

            }else  if(mt.getName().startsWith("get")){
                dictget.put(pName,this.createGetHandler(pName,mt));
            }
        }
        dictSetMethod.put(meta.getName(),dictset);
        dictGetMethod.put(meta.getName(),dictget);
    }

    private MyAction2< Object, Object> createSetHandler(String pName,Method mt) {
        var pType=mt.getParameterTypes()[0];
        if(!pType.isEnum()){
            return (target,value)-> {
                if(value==null)
                    return;
                try {
                    mt.invoke(target,value);
                } catch (Throwable e) {
                    throw new RuntimeException(String.format("对表%s,属性%s进行赋值发生错误,值为：%s",this.tableName,pName,value.toString()));
                }
            };
        }else {
            try {
                var emethode= pType.getMethod("valueOf", String.class);
                return (target,value)->{
                    if(value==null)
                        return;
                    try {
                        var evalue=emethode.invoke(null,value.toString());
                        mt.invoke(target,evalue);
                    } catch (Throwable e) {
                        throw new RuntimeException(String.format("对表%s,属性%s进行赋值发生错误,值为：%s",this.tableName,pName,value.toString()));
                    }
                };
            } catch (Throwable e) {
                throw new RuntimeException(String.format("反射获取枚举的valueOf方法发生错误,表名称%s,字段名称：%s",this.tableName,pName));
            }


        }
    }

    private MyFunction<Object, Object> createGetHandler(String pName,Method mt) {
        if(!mt.getReturnType().isEnum()){
            return target->mt.invoke(target);
        }
        return target->{
            var tmp= mt.invoke(target);
            if(tmp!=null)
                return tmp.toString();
            return tmp;
        };
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
        this.handler.onInvoked =handler;
        this.lstTarget.clear();
    }
}
