package com.reader.mapper;

public interface Mapper<F, T> {
    T map(F object);

}
