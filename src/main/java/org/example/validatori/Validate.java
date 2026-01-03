package org.example.validatori;

public class Validate {

    public Validate() {
    }

    public boolean validareId(Long id){
        return id > 0;
    }

    public boolean verificaString(String cv){
        return cv!=null;
    }

    public boolean verificaEmail(String email){
        return email.contains("@");
    }
}
