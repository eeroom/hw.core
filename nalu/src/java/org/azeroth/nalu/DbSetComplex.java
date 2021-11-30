package org.azeroth.nalu;

public class DbSetComplex<T,B,C> extends DbSet<C> {
    DbSet<T> left;
    DbSet<B> rigth;
    C entity;
    public DbSetComplex(DbSet<T> left,DbSet<B> rigth,C entity){

    }
}
