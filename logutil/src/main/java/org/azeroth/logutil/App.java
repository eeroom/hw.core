package org.azeroth.logutil;

import org.apache.logging.log4j.Logger;

public class App {
    public static void main( String[] args )
    {

        Logger log= org.apache.logging.log4j.LogManager.getLogger();
        Student st=new Student();
        st.Age=11;
        st.Name="张三222";
        log.error(st);

    }
}
