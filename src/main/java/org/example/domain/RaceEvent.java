//package org.example.domain;
//
//public class RaceEvent extends Event{
//    Integer nrBalize;
//
//    public RaceEvent(Integer nrBalize) {
//        super();
//        this.nrBalize = nrBalize;
//    }
//
//    public Integer getNrBalize() {
//        return nrBalize;
//    }
//
//    public void setNrBalize(Integer nrBalize) {
//        this.nrBalize = nrBalize;
//    }
//
//    //    /**
////     * Verifica daca doua intervale de timp respecta conditia ca @param timpRata sa fie mai mic decat @param time
////     * @return True -> daca se realizeaza conditia
////     *         False -> in caz contrar
////     * */
////    public boolean validareRataCuloar(Double timpRata,Double time) {
////        return  timpRata <= time;
////    }
////
////
////    /**
////     * Alege si afiseaza ratele care participa la RaceEvent, tocmai pt a scoate cel mai bun timp
////     * @throws ArgumentException daca nr de candidati este mai mic decat nr de balize
////     * */
////    public void execute(){
////        if(candidati.size() < balize.size())
////            throw new ArgumentException("Nu se poate realiza cursa! Nr de candidati este prea mic!");
////
////        //se determina rata cu viteza cea mai mica
////        Rata r = candidati.getFirst();
////        for(int index1 = 1; index1 < candidati.size(); index1++)
////            if(candidati.get(index1).getViteza() < r.getViteza())
////                r = candidati.get(index1);
////
////        Double culoar = balize.getLast();
////
////        Double left = (double) 0;
////        Double right = ((double)2*culoar)/r.getViteza() + 1;
////
////        Integer[] availability = new Integer[candidati.size()];
////        Double timpMinim = (double) -1;
////
////        SwimmingDuck[] listaRateRapide = new SwimmingDuck[balize.size()];
////
////        while(right - left >= 0.001){
////            for(int index = 0; index < candidati.size(); index++)
////                availability[index] = 0;
////
////            Double time = ((double)(left+right))/2;
////
////            int indexCuloar = 0;
////            Double timpParcurgere = (double) 0;
////
////            SwimmingDuck[] listaRateLoc = new SwimmingDuck[balize.size()];
////
////            for(int i = 0; i < candidati.size(); i++){
////                if(availability[i] == 0 && indexCuloar < balize.size()){
////                    //putem folosi rata
////                    Double timpRata = 2*balize.get(indexCuloar)/((double) candidati.get(i).getViteza());
////                    if(this.validareRataCuloar(timpRata,time)){
////                        //chiar folosim rata
////                        availability[i] = 1;
////                        listaRateLoc[indexCuloar] = candidati.get(i);
////                        indexCuloar++;
////                        if(timpParcurgere < timpRata)
////                            timpParcurgere = timpRata;
////                    }
////                }
////            }
////
////            if(timpParcurgere <= time && indexCuloar == balize.size()) {
////                right = time;
////                timpMinim = timpParcurgere;
////                listaRateRapide = listaRateLoc;
////            }
////            else{
////                left = time;
////            }
////        }
////
////        System.out.println("Timpul minim de parcurgere este: " + timpMinim);
////        for(int index = 0; index < listaRateRapide.length; index++){
////            double time = ((double) balize.get(index)*2.0) / listaRateRapide[index].getViteza()  ;
////            System.out.println("Rata cu id-ul " + listaRateRapide[index].getId() + " se afla pe culoarul " + index + ": t=" + time +"s");
////        }
////        // System.out.println(Arrays.toString(listaRateRapide));
////    }
//
//    @Override
//    public String toString() {
//        return super.toString()+"RaceEvent{" +
//                '}';
//    }
//}
