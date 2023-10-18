package io.github.eeroom.springcore.H06BeanFactory后处理;

@FunctionalInterface
public interface MySupplier<T>  {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws  Throwable;
}
