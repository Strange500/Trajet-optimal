package graphes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;
import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.Lieu;

public class V1 {
    
    private static final String SEPARATOR = ";";

    private static final int DEPART_IDX = 0;
    private static final int DESTINATION_IDX = 1;
    private static final int MODALITE_IDX = 2;
    private static final int PRIX_IDX = 3;
    private static final int POLLUTION_IDX = 4;
    private static final int DUREE_IDX = 5;

    public static void main (String[] args) {
        String[] data = new String[]{
            "villeA;villeB;Train;60;1.7;80",
            "villeB;villeD;Train;22;2.4;40",
            "villeA;villeC;Train;42;1.4;50",
            "villeB;villeC;Train;14;1.4;60",
            "villeC;villeD;Avion;110;150;22",
            "villeC;villeD;Train;65;1.2;90"
        };

        args = data;

        String depart, destination ;
        ModaliteTransport modalite;
        double prix, pollution;
        int Duree;



        if (donneesValides(args)) {
            Graphes g = new Graphes();
            for (String arg : args) {
                String[] elements = arg.split(SEPARATOR);
                
                depart = elements[DEPART_IDX];
                destination = elements[DESTINATION_IDX];
                modalite = ModaliteTransport.valueOf(elements[MODALITE_IDX].toUpperCase());
                prix = Double.parseDouble(elements[PRIX_IDX]);
                pollution = Double.parseDouble(elements[POLLUTION_IDX]);
                Duree = Integer.parseInt(elements[DUREE_IDX]);


                g.ajouterArrete(depart, destination, modalite, prix, pollution, Duree);
                g.ajouterArrete(destination, depart, modalite, prix, pollution, Duree);
            }

            //ModaliteTransport moyen = getModaliteTransport();
            ModaliteTransport moyen = ModaliteTransport.TRAIN;

            Lieu dep = getLieuDepart(g);

            Lieu dest = getLieuDestination(g, dep);

            Critere critere = getCritere();
            

            // if (moyen == null) {
            //     List<Chemin> chemins = g.getPathByCritere(dep.toString(), dest.toString(), critere);
            //     System.out.println("Les trajets recommandés de " + dep + " à " + dest + " sont:");
            //     for (int i = 0; i < chemins.size(); i++) {
            //         System.out.println(i + 1 + ") " + cheminWithCorre(chemins.get(i)));
            //     }

            // }
            if (g.hasPathByModalite(dep.toString(), dest.toString(), moyen)) {
                
                List<Chemin> chemins = g.getPathByModaliteAndCritere(dep.toString(), dest.toString(), moyen, critere);
                System.out.println("Les trajets recommandés de " + dep + " à " + dest + " sont:");
                for (int i = 0; i < chemins.size(); i++) {
                    System.out.println(i + 1 + ") " + cheminWithCorre(chemins.get(i), critere));
                }
            }
            else {
                System.out.println("Il n'y a pas de chemin disponible pour le moyen de transport que vous avez choisi");
            }
            
            

            /*List<Chemin> chemins = AlgorithmeKPCC.kpcc(g, new ILieu("villeA"), new ILieu("villeD"), 3);
            System.out.println("Les 3 trajets recommandés de A à D sont:");
            for (int i = 0; i < chemins.size(); i++) {
                if (chemins.get(i).aretes().get(1).getModalite() == ModaliteTransport.TRAIN) {
                    System.out.println(i + 1 + ") " + chemins.get(i).aretes().get(0).getModalite() + " de " +
                            chemins.get(i).aretes().get(0).getDepart() + " à " + chemins.get(i).aretes().get(0).getArrivee() +
                            " en passant par " + chemins.get(i).aretes().get(1).getDepart() +
                            ". Durée : " + (int) chemins.get(i).poids() + " minutes");
                } else {
                    System.out.println(i + 1 + ") " + chemins.get(i).aretes().get(0).getModalite() + " de " +
                            chemins.get(i).aretes().get(0).getDepart() + " à " + chemins.get(i).aretes().get(0).getArrivee() +
                            " puis " + chemins.get(i).aretes().get(1).getModalite() + " de " +
                            chemins.get(i).aretes().get(1).getDepart() + " à " + chemins.get(i).aretes().get(1).getArrivee() +
                            ". Durée : " + (int) chemins.get(i).poids() + " minutes");
                }
            }*/
        }
    }

    

    

    public static boolean estNombre(String chaine) {
        try {
            Double.parseDouble(chaine);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean donneesValides(String[] args) {
        if (args.length == 0) {
            return false;
        }
        for (String arg : args) {
            String[] elements = arg.split(";");
            if (elements.length != 6 || !estNombre(elements[3]) || !estNombre(elements[4]) || !estNombre(elements[5]) || 
            elements[0].length() == 0 || elements[1].length() == 0 || elements[2].length() == 0 || elements[3].length() == 0 || elements[4].length() == 0 || elements[5].length() == 0) {
                return false;
            }
        }
        return true;
    }


    public static String getUserInuput() {
        Scanner scanner = new Scanner(System.in);
        String r = scanner.next();
        return r;
        
    }

    public static ModaliteTransport getModaliteTransport() {
        System.out.println("Quelle moyen de transport voulez-vous utiliser?");
        for (ModaliteTransport m : ModaliteTransport.values()) {
            System.out.println("\t- " + m);
        }
        //System.out.println("\t- Tous");
        System.out.println("Entrez le moyen de transport:");
        String moyen = getUserInuput();
        if (moyen.length() == 0) {
            System.out.println("Vous n'avez rien entré");
            return getModaliteTransport();
        }
        // if (moyen.toLowerCase().equals("tous")) {
        //     return null;
        // }
        try {
            return ModaliteTransport.valueOf(moyen.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Le moyen de transport que vous avez entré n'existe pas");
            return getModaliteTransport();
        }
    }

    public static Lieu getLieuDepart(Graphes g) {
        System.out.println("Ville de départ disponible:");
        for (Lieu l : g.getLieux()) {
            System.out.println("\t- " + l);
        }
        System.out.println("Entrez la ville de départ:");
        String r = getUserInuput();
        if (r.length() == 0) {
            System.out.println("Vous n'avez rien entré");
            return getLieuDepart(g);
        }
        if (!Graphes.contientLieux(g.getG1(), r)) {
            System.out.println("La ville que vous avez entré n'existe pas");
            return getLieuDepart(g);
        }
        return new ILieu(r);
    }

    public static Lieu getLieuDestination(Graphes g, Lieu depart) {
        System.out.println("Ville de destination disponible:");
        for (Lieu l : g.getLieux(depart))  {
            System.out.println("\t- " + l);
        }
        System.out.println("Entrez la ville de destination:");
        String r = getUserInuput();
        if (r.length() == 0) {
            System.out.println("Vous n'avez rien entré");
            return getLieuDestination(g, depart);
        }
        if (!Graphes.contientLieux(g.getG1(), r)) {
            System.out.println("La ville que vous avez entré n'existe pas");
            return getLieuDestination(g, depart);
        }
        return new ILieu(r);
    }

    public static Critere getCritere() {
        System.out.println("Critere disponible:");
        for (Critere l : Critere.values()) {
            System.out.println("\t- " + l);
        }
        System.out.println("Entrez le critere:");
        String r = getUserInuput();
        if (r.length() == 0) {
            System.out.println("Vous n'avez rien entré");
            return getCritere();
        }
        try {
            return Critere.valueOf(r.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Le critere que vous avez entré n'existe pas");
            return getCritere();
        }
    }



    public static String cheminWithCorre(Chemin che, Critere critere) {
        String r = "";
        // on enleve les arrete vers alpha et omega
        che.aretes().remove(0);
        che.aretes().remove(che.aretes().size()-1);

        for (Chemin cheModal : IChemin.splitByModalite(che)) {
            if (!r.isEmpty()) {
                r += " puis ";
            }
            r += cheModal.aretes().get(0).getModalite() + " de " +
                    cheModal.aretes().get(0).getDepart() + " à " + cheModal.aretes().get(cheModal.aretes().size()-1).getArrivee() + " ";
            if (cheModal.aretes().size() > 1) {
                r += "en passant par";
                for (int i = 1; i < cheModal.aretes().size(); i++) {
                    r += " " + cheModal.aretes().get(i).getDepart() + "";
                    if (i < cheModal.aretes().size() - 1) {
                        r += ",";
                    }
                    
                }
            }
            
        }
        return r + " totale: " + che.poids() + " " +Critere.getUnit(critere) ;

}

}
