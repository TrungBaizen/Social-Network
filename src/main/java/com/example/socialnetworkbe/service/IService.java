package com.example.socialnetworkbe.service;

import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface IService<T> {
    T save(T t , BindingResult bindingResult);
    T update(T t,Long id , BindingResult bindingResult);
    T delete(Long id);
    Optional<T> findById(Long id);
}
