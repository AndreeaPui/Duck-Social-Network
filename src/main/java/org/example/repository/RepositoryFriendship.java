package org.example.repository;

import org.example.domain.Friendship;
import org.example.exceptii.AlreadyInRepoException;
import org.example.exceptii.ArgumentException;
import org.example.exceptii.NotInListException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/*

public class RepositoryFriendship implements Repository<Friendship> {
    List<Friendship> friendships = new ArrayList<>();

    public RepositoryFriendship(List<Friendship> friendships) {
        this.friendships = friendships;
    }

    @Override
    public void save(Friendship e) {
        if(e==null)
            throw new IllegalArgumentException("Friendship is null");
        for(Friendship f : friendships){
            if(Objects.equals(f.getId(), e.getId()))
                throw new AlreadyInRepoException("Friendship is already in repository");
        }

        friendships.add(e);
    }

    @Override
    public void update(Friendship entity) {

    }

    @Override
    public void delete(Long id) {
        if(id==null)
            throw new ArgumentException("Id is null");

        boolean status = false;
        for(Friendship u : friendships){
            if(Objects.equals(u.getId(), id)){
                friendships.remove(u);
                status = true;
                break;
            }
        }

        if(!status)
            throw new NotInListException("User is not in repository");
    }

    @Override
    public List<Friendship> findAll() {
        return friendships;
    }

    @Override
    public Friendship findOne(Long id) {
        if(id==null)
            throw new ArgumentException("Id is null");

        for(Friendship u : friendships)
            if(Objects.equals(u.getId(), id)) {
                return u;
            }

        throw  new NotInListException("Friendship is not in repository");
    }

}
*/