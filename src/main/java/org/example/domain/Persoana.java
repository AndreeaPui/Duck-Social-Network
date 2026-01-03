package org.example.domain;

import java.time.LocalDate;
import java.util.List;

public class Persoana extends User{
    private String nume;
    private String prenume;
    private LocalDate dataNasterii;
    private String ocupatie;
    private Integer nivelEmpatie;

    public Persoana(String username, String password, String email, List<User> prieteni, String nume, String prenume, LocalDate dataNasterii, String ocupatie, Integer nivelEmpatie) {
        super(username, password, email, prieteni);
        this.nume = nume;
        this.prenume = prenume;
        this.dataNasterii = dataNasterii;
        this.ocupatie = ocupatie;
        this.nivelEmpatie = nivelEmpatie;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public LocalDate getDataNasterii() {
        return dataNasterii;
    }

    public void setDataNasterii(LocalDate dataNasterii) {
        this.dataNasterii = dataNasterii;
    }

    public String getOcupatie() {
        return ocupatie;
    }

    public void setOcupatie(String ocupatie) {
        this.ocupatie = ocupatie;
    }

    public Integer getNivelEmpatie() {
        return nivelEmpatie;
    }

    public void setNivelEmpatie(Integer nivelEmpatie) {
        this.nivelEmpatie = nivelEmpatie;
    }

    //comportament -> implementeaza functiile din user

    //creaza si administreaza evenimente

    //poate trimite mesaje altor utilizatori

    @Override
    void login() {
        System.out.println("Login Persoana");
    }

    @Override
    void logout() {
        System.out.println("Logout Persoana");
    }

//    @Override
//    void sendMessage(Message message) {
//        User u= message.getReceiver();
//        u.receiveMessage(message);
//    }
//
//    @Override
//    void receiveMessage(Message message) {
//        System.out.println(message.getContent());
//    }

    @Override
    public String toString() {
        return super.toString()+
                "Persoana{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", dataNasterii=" + dataNasterii +
                ", ocupatie='" + ocupatie + '\'' +
                ", nivelEmpatie=" + nivelEmpatie +
                '}';
    }

}
