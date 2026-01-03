package org.example.repository;

import org.example.exceptii.ArgumentException;

import java.util.List;
import java.util.Optional;

/**
 * CRUD operations repository interface
 * @param <E> -  type of entities saved in repository
 */

public interface Repository<E> {

    /**
     * @param e - utilizatorul care trebuie adaugat in repository
     * @return un {@code Optional} - null daca entitatea a fost salvata,
     *                             - entitatea daca deja exista in lista
     * @throws ArgumentException daca e este null
     **/
    Optional<E> save(E e);

    /**
     * @param id - id-ul utilizatorului pe care dorim sa-l stergem
     * @return un {@code Optional}
     *                - null daca nu exista o entitate cu id-ul dat,
     *                - entitatea eliminata, in caz contrar
     * @throws ArgumentException daca e este null
     */
    Optional<E> delete(Long id);

    /**
     * @return toti utilizatorii
     * */
    Iterable<E> findAll();

    /**
     * @param id - id-ul utilizatorului cautat
     * @return un {@code Optional} care incapsuleaza entitatea cu respectivul id
     * @throws ArgumentException daca id este null
     * */
    Optional<E> findOne(Long id);


    /**
     *
     * @param entity
     *          entity must not be null
     * @return  un {@code Optional}
     *             - null daca entitatea a fost actualizata
     *             - in caz contrar (e.g. id nu exista) returneaza entitatea
     * @throws IllegalArgumentException daca entitatea data este null.
     */
    Optional<E> update(E entity);

}
