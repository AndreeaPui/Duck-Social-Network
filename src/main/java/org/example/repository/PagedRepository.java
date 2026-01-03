package org.example.repository;

import org.example.repository.dto.Page;
import org.example.repository.dto.Pageable;

public interface PagedRepository<E>  extends Repository<E>{
    Page<E> findAllOnPage(Pageable pageable);
}
