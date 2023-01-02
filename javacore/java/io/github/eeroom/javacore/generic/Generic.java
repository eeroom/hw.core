package io.github.eeroom.javacore.generic;

public class Generic {

    public static void main(String[] args){
        var pp=new ProcessHandler<People>();
        var pl=new ProcessHandler<StudentLeader>();

        //类似于c#的协变，仅类型参数作用于返回值位置的场景，才能编译通过
        ProcessHandler<? extends Student> phes=pl;
        //协变后，返回位置的类型是父类，而变量对象实际返回的是子类的实例，所以肯定没有问题
        Student student= phes.get(0);
        //协变后，参数位置的类型是父类，而变量对象实际需要子类的实例，这就不能确保参数能够符合要求，编译不通过
        //phes.SetTarget(new Student());

        //类似于c#的逆变，仅类型参数作用于参数位置的场景，才能编译通过
        ProcessHandler<? super Student> phss=pp;
        //逆变后，返回值位置的类型是子类，而变量对象实际返回的是父类的实例，这就不能确保返回值能够符合要求，编译不通过
        //Student student1= phss.get(0);
        //逆变后，参数位置的类型是子类，而变量对象实际需要的父类的实例，所以肯定没有问题
        phss.SetTarget(new Student());

        /**
         * 总结：
         * 定义的方法中存在泛型参数，
         * 如果方法中，泛型的类型参数仅作用于返回值位置，则可以把类型由<T>调整为<? extends T>
         * 如果方法中，泛型的类型参数仅作用于参数位置，则可以把类型由<T>调整为<? super T>
         */





    }
}
