package io.github.eeroom.apiclient;



import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class App {
    public static  void main(String[] arg){
        var ihome= HttpChannelFactory.createChannel("",IHome.class);
        var st=new Student();
        st.setAge(11);
        st.setName("hh");
        var lst= ihome.Search(st);
        var tf=new TypeReference<List<Student>>(){};
        System.out.println("hello world");
    }
}
