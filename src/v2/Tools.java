package src.v2;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;

import javax.tools.Tool;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.Trancon;
import src.v2.exception.CheminInexistantException;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.Lieu;

/**
 * Classe contenant des outils pour l'application
 */
public class Tools {
    private static final String SEPARATOR = ";";

    private static final int DEPART_IDX = 0;
    private static final int DESTINATION_IDX = 1;
    private static final int MODALITE_IDX = 2;
    private static final int PRIX_IDX = 3;
    private static final int CO2_IDX = 4;
    private static final int TEMPS_IDX = 5;

    public static String SUFFIXE = "_BIS";
    public static void main (String[] args) {

        ArrayList<String> data = Tools.getCSV(Voyageur.path);
        ArrayList<String> correspondance = Tools.getCSV(Voyageur.path2);

        String depart, destination ;
        ModaliteTransport modalite;
        double prix, pollution;
        int Duree;

        int thresholdPrix = Integer.MAX_VALUE, thresholdDuree = Integer.MAX_VALUE;
        double thresholdPollution = Double.MAX_VALUE;

        if (donneesValides(data)) {

            Plateforme g = initPlateforme(data, correspondance);
            
            ModaliteTransport moyen = getModaliteTransport();

            Lieu dep = getLieuDepart(g);

            Lieu dest = getLieuDestination(g, dep);

            if (g.hasPathByModalite(dep.toString(), dest.toString(), moyen)) {
                
                List<TypeCout> criteres = new ArrayList<>();
                criteres.add(TypeCout.PRIX);
                criteres.add(TypeCout.CO2);
                criteres.add(TypeCout.TEMPS);
                
                TypeCout critere = getTypeCout();
                criteres.remove(critere);

                for (TypeCout c : criteres) {
                    switch (c) {
                        case PRIX:
                            thresholdPrix = getThresholdPrix();
                            break;
                        case CO2:
                            thresholdPollution = getThresholdPollution();
                            break;
                        case TEMPS:
                            thresholdDuree = getThresholdDuree();
                            break;
                    }
                }

                

                int nb_trajet = getNbTrajet();
                List<Chemin> chemins;
                try {
                    chemins = g.getPathByModaliteAndTypeCout(dep.toString(), dest.toString(), moyen, critere, nb_trajet);
                } catch (CheminInexistantException e) {
                    System.out.println("Il n'y a pas de chemin disponible pour les critères que vous avez choisi");
                    return;
                }
                
                for (TypeCout c : criteres) {
                    switch (c) {
                        case PRIX:
                            applyThreshold(g, chemins, c, thresholdPrix);
                            break;
                        case CO2:
                            applyThreshold(g, chemins, c, thresholdPollution);
                            break;
                        case TEMPS:
                            applyThreshold(g, chemins, c, thresholdDuree);
                            break;
                    }
                }

                if (chemins.size() == 0) {
                    System.out.println("Il n'y a pas de chemin disponible pour les critères que vous avez choisi");
                }
                else {
                    System.out.println("Les trajets recommandés de " + dep + " à " + dest + " sont:");
                    for (int i = 0; i < chemins.size(); i++) {
                        System.out.println(i + 1 + ") " + cheminWithCorre(chemins.get(i), critere));
                    }
                }
                
            }
            else {
                System.out.println("Il n'y a pas de chemin disponible pour le moyen de transport que vous avez choisi");
            }
            
            

            /*List<Chemin> chemins = AlgorithmeKPCC.kpcc(g, new LieuImpl("villeA"), new LieuImpl("villeD"), 3);
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

    public static String buildLieuname(String ville, ModaliteTransport modality, ModaliteTransport modality2) {
        return ville + "_" + modality + "_" + modality2;
    }

    public static Set<String> buildLieuxNames(String ville) {
        Set<String> lieux = new HashSet<>();
        for (ModaliteTransport m : ModaliteTransport.values()) {
            for (ModaliteTransport m2 : ModaliteTransport.values()) {
                lieux.add(ville+"_"+m+"_"+m2);
            }
        }
        return lieux;
    }

    public static List<String> getLieuxWithDepModality(String ville, ModaliteTransport modality) {
        List<String> lieux = new ArrayList<>();
        for (String s : buildLieuxNames(ville)) {
            if (s.split("_")[2].equals(modality.toString())) {
                lieux.add(s);
            }
        }
        return lieux;
    }

    public static List<String> getLieuxWithArrModality(String ville, ModaliteTransport modality) {
        List<String> lieux = new ArrayList<>();
        for (String s : buildLieuxNames(ville)) {
            if (s.split("_")[1].equals(modality.toString())) {
                lieux.add(s);
            }
        }
        return lieux;
    }

    public static void ajouterCorrespondance(Plateforme g, String ville, ArrayList<String> correspondance) {
        List<String> correspondanceNonRenseigne = new ArrayList<>();
        for (String s : buildLieuxNames(ville)) {

            correspondanceNonRenseigne.add(s);

            String[] elt = s.split("_");
            String arrMod = elt[1].toUpperCase();
            String depMod = elt[2].toUpperCase();
            String villeBis = elt[0];

            int cpt = 0;
            while (cpt < correspondance.size()) {
                String[] elements = correspondance.get(cpt).split(SEPARATOR);
                String villeDonne = elements[0];
                String modarr = elements[1].toUpperCase();
                String moddep = elements[2].toUpperCase();
                int temp = Integer.parseInt(elements[3]);
                double pollution = Double.parseDouble(elements[4]);
                double prix = Double.parseDouble(elements[5]);

                if (villeDonne.equals(villeBis) && modarr.equals(arrMod) && moddep.equals(depMod)) {
                    String nomVille = buildLieuname(ville, ModaliteTransport.valueOf(arrMod), ModaliteTransport.valueOf(depMod));
                    g.ajouterArrete(nomVille, nomVille + SUFFIXE, ModaliteTransport.valueOf(depMod), prix, pollution, temp);
                    correspondanceNonRenseigne.remove(s);
                }
                cpt++;
            }
        }
        for (String s : correspondanceNonRenseigne) {
            String[] elt = s.split("_");
            String arrMod = elt[1].toUpperCase();
            String depMod = elt[2].toUpperCase();
            String villeBis = elt[0];
            String nomVille = buildLieuname(ville, ModaliteTransport.valueOf(arrMod), ModaliteTransport.valueOf(depMod));
            g.ajouterArrete(nomVille, nomVille + SUFFIXE, ModaliteTransport.valueOf(depMod), 0, 0, 0);
        }
    }

    /**
     * Initialise la plateforme avec les données passées en paramètre
     * @param args Données
     * @return Plateforme
     */
    public static Plateforme initPlateforme(ArrayList<String> args, ArrayList<String> correspondance) {
        Plateforme g = new Plateforme();
        for (String arg : args) {
            String[] elements = arg.split(SEPARATOR);
            
            String depart = elements[DEPART_IDX];
            String destination = elements[DESTINATION_IDX];
            ModaliteTransport modalite = ModaliteTransport.valueOf(elements[MODALITE_IDX].toUpperCase());
            double prix = Double.parseDouble(elements[PRIX_IDX]);
            double pollution = Double.parseDouble(elements[CO2_IDX]);
            int Duree = Integer.parseInt(elements[TEMPS_IDX]);

            ajouterCorrespondance(g, depart, correspondance);
            ajouterCorrespondance(g, destination, correspondance);

            List<String> depLieux = getLieuxWithDepModality(depart, modalite);
            List<String> destLieux = getLieuxWithArrModality(destination, modalite);

            for (String depLieu : depLieux) {
                for (String destLieu : destLieux) {
                    if (depLieu.split("_")[2].equals(destLieu.split("_")[1])) {
                        System.out.println("AJOUT DE " + depLieu + SUFFIXE + " VERS " + destLieu);

                        g.ajouterArrete(depLieu + SUFFIXE , destLieu, modalite, prix, pollution, Duree);
                        //g.ajouterArrete(destLieu + SUFFIXE , depLieu, modalite, prix, pollution, Duree);
                    }
                    
                    
                }
            }

            depLieux = getLieuxWithDepModality(destination, modalite);
            destLieux = getLieuxWithArrModality(depart, modalite);

            for (String depLieu : depLieux) {
                for (String destLieu : destLieux) {
                    if (depLieu.split("_")[2].equals(destLieu.split("_")[1])) {
                        System.out.println("AJOUT DE " + depLieu + SUFFIXE + " VERS " + destLieu);
                        g.ajouterArrete(depLieu + SUFFIXE , destLieu, modalite, prix, pollution, Duree);
                        //g.ajouterArrete(destLieu + SUFFIXE , depLieu, modalite, prix, pollution, Duree);
                    }
                    
                    
                }
            }
            // g.ajouterArrete(depart, destination, modalite, prix, pollution, Duree);
            // g.ajouterArrete(destination, depart, modalite, prix, pollution, Duree);
        }
        return g;

    }
    
    /**
     * Vérifie si une chaîne de caractères est un nombre
     * @param chaine Chaîne de caractères
     * @return Vrai si la chaîne est un nombre, faux sinon
     */
    public static boolean estNombre(String chaine) {
        try {
            Double.parseDouble(chaine);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Vérifie si les données passées en paramètre sont valides
     * @param args Données
     * @return Vrai si les données sont valides, faux sinon
     */
    public static boolean donneesValides(ArrayList<String> args) {
        if (args.size() == 0) {
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

    /**
     * Récupère une chaîne de caractères entrée par l'utilisateur
     * @return Chaîne de caractères
     */
    public static String getUserInuput() {
        Scanner scanner = new Scanner(System.in);
        String r = scanner.next();
        return r;
    }

    /**
     * Récupère le moyen de transport entré par l'utilisateur
     * @return Moyen de transport
     */
    public static ModaliteTransport getModaliteTransport() {
        System.out.println("Quel moyen de transport voulez-vous utiliser?");
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

    /**
     * Récupère le lieu de départ entré par l'utilisateur
     * @param g Plateforme
     * @return Lieu de départ
     */
    public static Lieu getLieuDepart(Plateforme g) {
        System.out.println("Ville de départ disponible:");
        for (Lieu l : g.getLieux()) {
            System.out.println("\t- " + l);
        }
        System.out.println("Entrez la ville de départ:");
        String r = getUserInuput();
        StringBuilder sb = new StringBuilder();
        sb.append((r.charAt(0) + "").toUpperCase());
        sb.append(r.substring(1).toLowerCase());
        r = sb.toString();
        if (r.length() == 0) {
            System.out.println("Vous n'avez rien entré");
            return getLieuDepart(g);
        }
        if (!Plateforme.contientLieux(g.getG1(), r)) {
            System.out.println("La ville que vous avez entré n'existe pas");
            return getLieuDepart(g);
        }
        return new LieuImpl(r);
    }

    /**
     * Récupère le lieu de destination entré par l'utilisateur
     * @param g Plateforme
     * @param depart Lieu de départ
     * @return Lieu de destination
     */
    public static Lieu getLieuDestination(Plateforme g, Lieu depart) {
        System.out.println("Ville de destination disponible:");
        for (Lieu l : g.getLieux(depart))  {
            System.out.println("\t- " + l);
        }
        System.out.println("Entrez la ville de destination:");
        String r = getUserInuput();
        StringBuilder sb = new StringBuilder();
        sb.append((r.charAt(0) + "").toUpperCase());
        sb.append(r.substring(1).toLowerCase());
        r = sb.toString();
        if (r.length() == 0) {
            System.out.println("Vous n'avez rien entré");
            return getLieuDestination(g, depart);
        }
        if (!Plateforme.contientLieux(g.getG1(), r)) {
            System.out.println("La ville que vous avez entré n'existe pas");
            return getLieuDestination(g, depart);
        }
        return new LieuImpl(r);
    }

    /**
     * Récupère le critère entré par l'utilisateur
     * @return Critère
     */
    public static TypeCout getTypeCout() {
        System.out.println("TypeCout disponible:");
        for (TypeCout l : TypeCout.values()) {
            System.out.println("\t- " + l);
        }
        System.out.println("Entrez le critere:");
        String r = getUserInuput();
        if (r.length() == 0) {
            System.out.println("Vous n'avez rien entré");
            return getTypeCout();
        }
        try {
            return TypeCout.valueOf(r.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Le critere que vous avez entré n'existe pas");
            return getTypeCout();
        }
    }

    /**
     * Récupère le nombre de trajets entré par l'utilisateur
     * @return Nombre de trajets
     */
    public static int getNbTrajet() {
        System.out.println("Combien de trajet voulez-vous?");
        String r = getUserInuput();
        if (r.length() == 0) {
            System.out.println("Vous n'avez rien entré");
            return getNbTrajet();
        }
        if (!estNombre(r)) {
            System.out.println("Vous devez entrer un nombre");
            return getNbTrajet();
        }
        return Integer.parseInt(r);
    }

    /**
     * Récupère le seuil de prix entré par l'utilisateur
     * @return Seuil de prix
     */
    public static int getThresholdPrix() {
        System.out.println("Entrez le prix maximum que vous êtes prêt à payer (en €):");
        String r = getUserInuput();
        if (r.length() == 0) {
            System.out.println("Vous n'avez rien entré");
            return getThresholdPrix();
        }
        if (!estNombre(r)) {
            System.out.println("Vous devez entrer un nombre");
            return getThresholdPrix();
        }
        return Integer.parseInt(r);
    }

    /**
     * Récupère le seuil de durée entré par l'utilisateur
     * @return Seuil de durée
     */
    public static int getThresholdDuree() {
        System.out.println("Entrez la durée maximum que vous êtes prêt à passer (en minutes):");
        String r = getUserInuput();
        if (r.length() == 0) {
            System.out.println("Vous n'avez rien entré");
            return getThresholdDuree();
        }
        if (!estNombre(r)) {
            System.out.println("Vous devez entrer un nombre");
            return getThresholdDuree();
        }
        return Integer.parseInt(r);
    }

    /**
     * Récupère le seuil de pollution entré par l'utilisateur
     * @return Seuil de pollution
     */
    public static double getThresholdPollution() {
        System.out.println("Entrez la pollution maximum que vous êtes prêt à subir (en kgCO2e):");
        String r = getUserInuput();
        if (r.length() == 0) {
            System.out.println("Vous n'avez rien entré");
            return getThresholdPollution();
        }
        if (!estNombre(r)) {
            System.out.println("Vous devez entrer un nombre");
            return getThresholdPollution();
        }
        return Double.parseDouble(r);
    }

    /**
     * Applique un seuil sur les chemins
     * @param g Plateforme
     * @param chemins Chemins
     * @param critere Critère
     * @param threshold Seuil
     */
    public static void applyThreshold(Plateforme g, List<Chemin> chemins, TypeCout critere, int threshold) {
        List<Chemin> toRemove = new ArrayList<>();
        for (Chemin che : chemins) {
            double poidsByTypeCout = g.getPoidsByTypeCout(che, critere);
            if (poidsByTypeCout > threshold) {
                toRemove.add(che);

            }
        }
        chemins.removeAll(toRemove);

    }

    /**
     * Applique un seuil sur les chemins
     * @param g Plateforme
     * @param chemins Chemins
     * @param critere Critère
     * @param threshold Seuil
     */
    public static void applyThreshold(Plateforme g, List<Chemin> chemins, TypeCout critere, double threshold) {
        List<Chemin> toRemove = new ArrayList<>();
        for (Chemin che : chemins) {
            double poidsByTypeCout = g.getPoidsByTypeCout(che, critere);
            if (poidsByTypeCout > threshold) {
                toRemove.add(che);
            }
        }
        chemins.removeAll(toRemove);

    }

    public static String cleanLieux(String name) {
        return name.split("_")[0];
    }

    /**
     * Retourne une chaîne de caractères représentant un chemin
     * @param che Chemin
     * @param critere Critère
     * @return Chaîne de caractères
     */
    public static String cheminWithCorre(Chemin che, TypeCout critere) {
        String r = "";
        // on enleve les arrete vers alpha et omega
        che.aretes().remove(0);
        che.aretes().remove(che.aretes().size()-1);

        for (Chemin cheModal : CheminImpl.splitByModalite(che)) {
            if (!r.isEmpty()) {
                r += " puis ";
            }
            r += cheModal.aretes().get(0).getModalite() + " de " +
                    cleanLieux(cheModal.aretes().get(0).getDepart().toString()) + " à " + cleanLieux(cheModal.aretes().get(cheModal.aretes().size()-1).getArrivee().toString()) + " ";
            if (cheModal.aretes().size() > 1) {
                boolean first = true;
                for (int i = 1; i < cheModal.aretes().size(); i++) {
                    if (!cheModal.aretes().get(i).getDepart().toString().contains(Tools.SUFFIXE)) {
                        if (first) {
                            r += "en passant par";
                            first = false;
                        }
                        r += " " + cleanLieux(cheModal.aretes().get(i).getDepart().toString()) + "";
                        if (i < cheModal.aretes().size() - 1) {
                            r += ",";
                        }
                    }
                    
                    
                }

            }
            
        }
        return r + " total: " + che.poids() + " " +TypeCout.getUnit(critere) ;
    }

    /**
     * Retourne une chaîne de caractères représentant un chemin
     * @param che Chemin
     * @param critere Critère
     * @return Chaîne de caractères
     */
    public static String cheminWithCorreDEBUG(Chemin che, TypeCout critere) {
        String r = "";
        // on enleve les arrete vers alpha et omega
        che.aretes().remove(0);
        che.aretes().remove(che.aretes().size()-1);

        for (Chemin cheModal : CheminImpl.splitByModalite(che)) {
            if (!r.isEmpty()) {
                r += " puis ";
            }
            r += cheModal.aretes().get(0).getModalite() + " de " +
                    cheModal.aretes().get(0).getDepart().toString() + " à " + cheModal.aretes().get(cheModal.aretes().size()-1).getArrivee().toString() + " ";
            if (cheModal.aretes().size() > 1) {
                boolean first = true;
                for (int i = 1; i < cheModal.aretes().size(); i++) {
                        if (first) {
                            r += "en passant par";
                            first = false;
                        }
                        r += " " + cheModal.aretes().get(i).getDepart().toString() + "";
                        if (i < cheModal.aretes().size() - 1) {
                            r += ",";
                        }
                    
                    
                    
                }

            }
            
        }
        return r + " total: " + che.poids() + " " +TypeCout.getUnit(critere) ;
    }

    public static boolean equalsArrete(Trancon tr1, Trancon tr2) {
        if (!cleanLieux(tr1.getDepart().toString()).equals(cleanLieux(tr2.getDepart().toString())) ) {
            return false;
        }
        if (!cleanLieux(tr1.getArrivee().toString()).equals(cleanLieux(tr2.getArrivee().toString())) ) {
            return false;
        }
        // if (tr1.getModalite() != tr2.getModalite()) {
        //     return false;
        // }
        return true;
    }

    public static boolean equalsChemin(Chemin che1, Chemin che2) {
        if (che1.aretes().size() != che2.aretes().size()) {
            return false;
        }
        for (int i = 0; i < che1.aretes().size(); i++) {
            if (!equalsArrete(che1.aretes().get(i), che2.aretes().get(i))) {
                return false;
            }
        }
        return true;
    }

    public static List<Chemin>  removeDuplicates(List<Chemin> chemins, int nb_trajet) {
        List<Chemin> n = new ArrayList<>();
        for (Chemin che : chemins) {
            if (n.size() == nb_trajet) {
                break;
            }
            boolean add = true;
            for (Chemin che2 : n) {
                if (equalsChemin(che, che2)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                n.add(che);
            }
            
        }
        return n;
    }

    /**
     * Retourne une chaîne de caractères représentant un chemin
     * @param che Chemin
     * @param critere Critère
     * @return Chaîne de caractères
     */
    public static String cheminWithCorreBis(Chemin che, TypeCout critere) {
        String r = "";
        // on enleve les arrete vers alpha et omega
        che.aretes().remove(0);
        che.aretes().remove(che.aretes().size()-1);

        for (Chemin cheModal : CheminImpl.splitByModalite(che)) {
            if (!r.isEmpty()) {
                r += " puis ";
            }
            r += cheModal.aretes().get(0).getModalite() + " de " +
                    cleanLieux(cheModal.aretes().get(0).getDepart().toString()) + " à " + cleanLieux(cheModal.aretes().get(cheModal.aretes().size()-1).getArrivee().toString()) + " ";
            // if (cheModal.aretes().size() > 1) {
            //     r += "en passant par";
            //     for (int i = 1; i < cheModal.aretes().size(); i++) {
            //         r += " " + cheModal.aretes().get(i).getDepart() + "";
            //         if (i < cheModal.aretes().size() - 1) {
            //             r += ",";
            //         }
                    
            //     }
            // }
            
        }
        return r + " total: " + che.poids() + " " +TypeCout.getUnit(critere) ;
    }

    public static ArrayList<String> getCSV(String path) {
        try {
            Scanner scanner = new Scanner(new File(path));
            ArrayList<String> contenu = new ArrayList<String>();
            while (scanner.hasNextLine()) {
                contenu.add(scanner.nextLine());
            }
            scanner.close();
            return contenu;
        } catch (Exception e) {
            System.out.println("Fichier introuvable");
        }
        return null;
    }
}
