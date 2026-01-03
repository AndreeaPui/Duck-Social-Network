package org.example.domain.rataFamily;

import org.example.domain.Card;
import org.example.domain.Rata;
import org.example.domain.User;
import org.example.domain.interfete.Zburator;
import org.example.utils.TipRata;

import java.util.List;

public class FlyingDuck extends Rata implements Zburator {

    public FlyingDuck(String username, String password, String email, List<User> prieteni, TipRata tip, Double viteza, Double rezistenta, Long card) {
        super(username, password, email, prieteni, tip, viteza, rezistenta, card);
    }

    @Override
    public void zboara() {
        System.out.println("Zboara cu viteza " + getViteza());
    }
}
