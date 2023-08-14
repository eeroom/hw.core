package io.github.eeroom.javacore.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Lambda {
    public static void main(String[] args){
        var lst2=new ArrayList<String>(){{add("1");add("2");}};
    }

    String tos(Integer pp){
        return pp.toString();
    }

    class   Student{
        final static int age=300;
    }

}


