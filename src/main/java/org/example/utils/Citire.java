//package org.example.utils;
//
//import org.example.domain.Rata;
//import org.example.domain.User;
//import org.example.domain.Persoana;
//import org.example.domain.rataFamily.FlyingDuck;
//import org.example.domain.rataFamily.SwimmingDuck;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Citire {
//
//    /**
//     * Metoda ce se ocupa de citirea datelor dintr-un fisier de tip CSV
//     * @param filePath - path-ul catre fisierul din care se citesc date
//     * */
//    public static List<User> citeste(String filePath) {
//        List<User> users = new ArrayList<>();
//
//        try {
//            var inputStream = Citire.class.getClassLoader().getResourceAsStream(filePath);
//            if (inputStream == null) {
//                throw new IOException("Fișierul " + filePath + " nu a fost găsit în resources!");
//            }
//
//            BufferedReader br = new BufferedReader(new java.io.InputStreamReader(inputStream));
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                //if (firstLine) { firstLine = false; continue; } // skip header
//
//                String[] t = line.split(",", -1); // keep empty fields
//
//                //Long id = Long.parseLong(t[0]);
//                TipUtilizator tip = TipUtilizator.valueOf(t[0].trim().toUpperCase());
//                String username = t[1];
//                String password = t[2];
//                String email = t[3];
//
//                if (tip == TipUtilizator.PERSOANA) {
//                    String nume = t[4];
//                    String prenume = t[5];
//                    LocalDate dataNasterii = LocalDate.parse(t[6]);
//                    String ocupatie = t[7];
//                    Integer nivelEmpatie = Integer.parseInt(t[8]);
//
//                    Persoana p = new Persoana(username, password, email,
//                             new ArrayList<>(),
//                            nume, prenume, dataNasterii, ocupatie, nivelEmpatie
//                    );
//                    users.add(p);
//
//                } else if (tip == TipUtilizator.RATA) {
//                    TipRata tipRata = TipRata.valueOf(t[4]);
//                    Double viteza = Double.parseDouble(t[5]);
//                    Double rezistenta = Double.parseDouble(t[6]);
//
//                    Rata r = null;
//                    if(tipRata == TipRata.SWIMMING)
//                        r = new SwimmingDuck(username, password, email,
//                                new ArrayList<>(),
//                                tipRata, viteza, rezistenta, null);
//                    else if (tipRata == TipRata.FLYING)
//                        r = new FlyingDuck(username, password, email,
//                                new ArrayList<>(), new ArrayList<>(),
//                                tipRata, viteza, rezistenta, null);
//
//                    /*Rata r = new Rata(
//                            id, username, password, email,
//                            new ArrayList<>(), new ArrayList<>(),
//                            tipRata, viteza, rezistenta, null
//                    );*/
//
//                    users.add(r);
//                }
//            }
//
//        } catch (IOException e) {
//            System.err.println("Eroare la citirea fișierului: " + e.getMessage());
//        } catch (Exception e) {
//            System.err.println("Eroare la parsare: " + e.getMessage());
//        }
//
//        return users;
//    }
//}
