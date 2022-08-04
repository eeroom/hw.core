package io.github.eeroom.javacore.collection;

import java.util.*;

/**
 * 集合体系(长度可变，不能存储基本数据类型值)
 *     |—--Collection（不同步,单列）
 *     |          |---List（有序，可以重复添加，迭代器遍历得到的数据顺序和数据的写入顺序一致或一一对应）
 *     |          |    |---ArrayList 基于数组结构实现，按下标查询效率高，增删非尾部的数据需要移动数组内的数据，效率不高
 *     |          |    |---LinkedList 基于链表结构，每个节点都包含本身的数据应用和前后两个节点的引用，按下标查询需要遍历节点，效率不高，增删数据效率高
 *     |          |---Set（无序，不能重复添加，迭代器遍历得到的数据顺序和数据的写入顺序一致）
 *     |               |---HashSet 基于HashMap实现，数据本身决定了其位置，把哈希值映射到数组索引值
 *     |               |---TreeSet 基于二叉树结构，数据按照比较接口的接口有序的分布在二叉树节点，这个有序是指数据排序，不是集合数据的添加顺序
 *     |               |---LinkedHashSet 基于hashmap?，并且数据包一层链表结构?
 *     |---Map（不同步，双列，key不能重复，但是value可以重复）
 *          |---HashMap ，基于哈希算法
 *          |---TreeMap
 *
 *
 */
public class App {
    public static void main(String[] args){


        System.out.println("hello world");
        var hs=new TreeSet<Integer>();
        hs.add(9);
        hs.add(10);

        hs.add(5);
        hs.add(33);
        hs.add(33);
        System.out.println(hs.last());


    }
}
