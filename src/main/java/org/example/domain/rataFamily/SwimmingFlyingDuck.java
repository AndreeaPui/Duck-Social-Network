package org.example.domain.rataFamily;

import org.example.domain.Card;
import org.example.domain.Rata;
import org.example.domain.User;
import org.example.domain.interfete.Inotator;
import org.example.utils.TipRata;

import java.util.List;

public class SwimmingFlyingDuck extends Rata implements Inotator {
    public SwimmingFlyingDuck(String username, String password, String email, List<User> prieteni, TipRata tip, Double viteza, Double rezistenta, Long card) {
        super(username, password, email, prieteni, tip, viteza, rezistenta, card);
    }

    @Override
    public void inoata() {
        System.out.println("Rata inoata si zboara cu viteza " + getViteza());
    }
}