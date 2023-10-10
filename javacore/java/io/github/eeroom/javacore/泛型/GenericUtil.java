package io.github.eeroom.javacore.泛型;

public class GenericUtil {

    public static void main(String[] args) throws Throwable{

        /**
         * 泛型通配符限定,逆变，A<? super T>
         * ProcessHandler<People>ProcessHandler<Animal>没有继承关系
         * People是Animal的子类，ProcessHandler<People>的变量指向ProcessHandler<Animal>的实例
         * 形式上类似于：子类变量指向父类实例，所以是逆变
         * 场景：类型参数仅作用于参数位置，当子类变量调用方法的时候，传入的参数值类型要求是子类型，而父类实例的参数值类型是父类型，所以调用肯定没问题
         *   参数值仍然遵循父类变量指向子类实例
         * 场景：类型参数仅作用于返回值位置，当子类变量调用方法的时候，返回值的类型要求是子类，而父类实例返回的是父类的实例，所以编译不通过
         *   返回值出现子类变量指向父类实例，所以该场景编译不通过
         */
        ProcessHandler<Animal> pha=null;
        ProcessHandler<? super People> phpIn=pha;

        /**
         * 泛型通配符限定,协变，A<? extends T>
         * ProcessHandler<People>ProcessHandler<Student>没有继承关系
         * People是Student的父类，ProcessHandler<People>的变量指向ProcessHandler<Student>的实例，
         * 形式上类似于：父类变量指向子类实例，所以是协变
         * 场景：类型参数仅作用于参数位置，当父类变量调用方法的时候，传入的参数值类型要求是父类型，而子类实例实际的参数值类型是子类型，所以编译不通过
         *   参数值出现子类变量指向父类实例，所以该场景编译不通过
         * 场景：类型参数仅作用于返回值位置，当父类变量调用方法的时候，返回值的类型要求是父类，而子类实例返回的是子类的实例，所以调用肯定没问题
         *   返回值仍然遵循父类变量指向子类实例
         */
        ProcessHandler<Student> phs=null;
        ProcessHandler<? extends People> phpOut=phs;

        /**
         * 总结：
         * 定义的方法中存在泛型参数，
         * 如果方法中，泛型的类型参数仅作用于返回值位置，则可以把类型由<T>调整为<? extends T>
         * 如果方法中，泛型的类型参数仅作用于参数位置，则可以把类型由<T>调整为<? super T>
         * java的泛型类型参数既能逆变，也能协变，支持泛型类和泛型接口，由调用者决定，编译器根据调用者的代码是否符合父类变量指向子类实例这一规则，判定是否允许编译通过
         * c#的泛型类型参数只能逆变或协变或者不可变，支持泛型接口和泛型委托，由类型定义者决定，in和out关键字已经提前确保调用者的代码一定符合父类变量指向子类实例这一规则
         */
    }
}
