package src;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.Trancon;

public class ToolsCorrespondance extends Tools {

    public static String SUFFIXE = "_BIS";

    /**
     * @param ville
     * @param modality
     * @param modality2
     * @return Chaîne de caractères représentant un lieu composé du nom de la vile
     *         suivi des modalités de départ et d'arrivée
     */
    public static String buildLieuname(String ville, ModaliteTransport modality, ModaliteTransport modality2) {
        return ville + "_" + modality + "_" + modality2;
    }

    /**
     * @param ville
     * @return Set de String contenant les noms des lieux reprsentant une ville
     */
    public static Set<String> buildLieuxNames(String ville) {
        Set<String> lieux = new HashSet<>();
        for (ModaliteTransport m : ModaliteTransport.values()) {
            for (ModaliteTransport m2 : ModaliteTransport.values()) {
                lieux.add(ville + "_" + m + "_" + m2);
            }
        }
        return lieux;
    }

    /**
     * @param ville
     * @param modality
     * @return Liste de String contenant les noms des lieux avec la modalité de
     *         départ
     */
    public static List<String> getLieuxWithDepModality(String ville, ModaliteTransport modality) {
        List<String> lieux = new ArrayList<>();
        for (String s : buildLieuxNames(ville)) {
            if (s.split("_")[2].equals(modality.toString())) {
                lieux.add(s);
            }
        }
        return lieux;
    }

    /**
     * @param ville
     * @param modality
     * @return Liste de String contenant les noms des lieux avec la modalité
     *         d'arrivée
     */
    public static List<String> getLieuxWithArrModality(String ville, ModaliteTransport modality) {
        List<String> lieux = new ArrayList<>();
        for (String s : buildLieuxNames(ville)) {
            if (s.split("_")[1].equals(modality.toString())) {
                lieux.add(s);
            }
        }
        return lieux;
    }

    /**
     * Ajoute les correspondances à la plateforme
     * 
     * @param g
     * @param ville
     * @param correspondance
     */
    public static void ajouterCorrespondance(Plateforme g, String ville, ArrayList<String> correspondance) {
        // cette fonction ajoute toutes correspondance existante pour une ville (chaque
        // correspodance donne création a un lieux dédiée)
        List<String> correspondanceNonRenseigne = new ArrayList<>();
        for (String s : buildLieuxNames(ville)) {

            correspondanceNonRenseigne.add(s);
            // on recupere le nom de la ville ainsi que les modalités de départ et d'arrivée
            String[] elt = s.split("_");
            String arrMod = elt[1].toUpperCase();
            String depMod = elt[2].toUpperCase();
            String villeBis = elt[0];

            int cpt = 0;
            while (cpt < correspondance.size()) {
                // on recupere les informations de la correspondance
                String[] elements = correspondance.get(cpt).split(SEPARATOR);
                String villeDonne = elements[0];
                String modarr = elements[1].toUpperCase();
                String moddep = elements[2].toUpperCase();
                int temp = Integer.parseInt(elements[3]);
                double pollution = Double.parseDouble(elements[4]);
                double prix = Double.parseDouble(elements[5]);

                // si la ville de la correspondance est la même que celle du lieu et que les
                // modalités de départ et d'arrivée sont les mêmes que celles du lieu alors on
                // ajoute l'arrete

                if (villeDonne.equals(villeBis) && modarr.equals(arrMod) && moddep.equals(depMod)) {
                    String nomVille = buildLieuname(ville, ModaliteTransport.valueOf(arrMod),
                            ModaliteTransport.valueOf(depMod));
                    g.ajouterArrete(nomVille, nomVille + SUFFIXE, ModaliteTransport.valueOf(depMod), prix, pollution,
                            temp);
                    correspondanceNonRenseigne.remove(s);
                }
                cpt++;
            }
        }

        // enfin on ajoute les correspondance ne donnant lieux a aucun malus
        for (String s : correspondanceNonRenseigne) {
            String[] elt = s.split("_");
            String arrMod = elt[1].toUpperCase();
            String depMod = elt[2].toUpperCase();
            // String villeBis = elt[0];
            String nomVille = buildLieuname(ville, ModaliteTransport.valueOf(arrMod),
                    ModaliteTransport.valueOf(depMod));
            g.ajouterArrete(nomVille, nomVille + SUFFIXE, ModaliteTransport.valueOf(depMod), 0, 0, 0);
        }
    }

    /**
     * Initialise la plateforme avec les données passées en paramètre
     * 
     * @param args Données
     * @return Plateforme
     */
    public static PlateformeCorrespondance initPlateforme(ArrayList<String> args, ArrayList<String> correspondance) {
        PlateformeCorrespondance g = new PlateformeCorrespondance();
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

                        g.ajouterArrete(depLieu + SUFFIXE, destLieu, modalite, prix, pollution, Duree);
                        // g.ajouterArrete(destLieu + SUFFIXE , depLieu, modalite, prix, pollution,
                        // Duree);
                    }

                }
            }

            depLieux = getLieuxWithDepModality(destination, modalite);
            destLieux = getLieuxWithArrModality(depart, modalite);

            for (String depLieu : depLieux) {
                for (String destLieu : destLieux) {
                    if (depLieu.split("_")[2].equals(destLieu.split("_")[1])) {
                        g.ajouterArrete(depLieu + SUFFIXE, destLieu, modalite, prix, pollution, Duree);
                        // g.ajouterArrete(destLieu + SUFFIXE , depLieu, modalite, prix, pollution,
                        // Duree);
                    }

                }
            }
            // g.ajouterArrete(depart, destination, modalite, prix, pollution, Duree);
            // g.ajouterArrete(destination, depart, modalite, prix, pollution, Duree);
        }
        return g;

    }

    public static String cleanLieux(String name) {
        return name.split("_")[0];
    }

    /**
     * Retourne une chaîne de caractères représentant un chemin
     * 
     * @param che     Chemin
     * @param critere Critère
     * @return Chaîne de caractères
     */
    public static String cheminWithCorre(Chemin che, TypeCout critere) {
        String r = "";
        // on enleve les arrete vers alpha et omega
        che.aretes().remove(0);
        che.aretes().remove(che.aretes().size() - 1);

        for (Chemin cheModal : CheminImpl.splitByModalite(che)) {
            if (!r.isEmpty()) {
                r += " puis ";
            }
            r += cheModal.aretes().get(0).getModalite() + " de " +
                    cleanLieux(cheModal.aretes().get(0).getDepart().toString()) + " à "
                    + cleanLieux(cheModal.aretes().get(cheModal.aretes().size() - 1).getArrivee().toString()) + " ";
            if (cheModal.aretes().size() > 1) {
                boolean first = true;
                for (int i = 1; i < cheModal.aretes().size(); i++) {
                    if (!cheModal.aretes().get(i).getDepart().toString().contains(ToolsCorrespondance.SUFFIXE)) {
                        if (first) {
                            r += "en passant par";
                            first = false;
                        }
                        r += " " + cleanLieux(cheModal.aretes().get(i).getDepart().toString());
                        if (i < cheModal.aretes().size() - 1) {
                            r += ",";
                        }
                    }

                }

            }

        }
        return r + " total: " + che.poids() + " " + TypeCout.getUnit(critere);
    }

    /**
     * Retourne une chaîne de caractères représentant un chemin
     * 
     * @param che     Chemin
     * @param critere Critère
     * @return Chaîne de caractères
     */
    public static String cheminWithCorreDEBUG(Chemin che, TypeCout critere) {
        String r = "";
        // on enleve les arrete vers alpha et omega
        che.aretes().remove(0);
        che.aretes().remove(che.aretes().size() - 1);

        for (Chemin cheModal : CheminImpl.splitByModalite(che)) {
            if (!r.isEmpty()) {
                r += " puis ";
            }
            r += cheModal.aretes().get(0).getModalite() + " de " +
                    cheModal.aretes().get(0).getDepart().toString() + " à "
                    + cheModal.aretes().get(cheModal.aretes().size() - 1).getArrivee().toString() + " ";
            if (cheModal.aretes().size() > 1) {
                boolean first = true;
                for (int i = 1; i < cheModal.aretes().size(); i++) {
                    if (first) {
                        r += "en passant par";
                        first = false;
                    }
                    r += " " + cheModal.aretes().get(i).getDepart().toString();
                    if (i < cheModal.aretes().size() - 1) {
                        r += ",";
                    }

                }

            }

        }
        return r + " total: " + che.poids() + " " + TypeCout.getUnit(critere);
    }

    /**
     * @param tr1
     * @param tr2
     * @return true si les arretes sont égales, false sinon
     */
    public static boolean equalsArrete(Trancon tr1, Trancon tr2) {
        if (!cleanLieux(tr1.getDepart().toString()).equals(cleanLieux(tr2.getDepart().toString()))) {
            return false;
        }
        if (!cleanLieux(tr1.getArrivee().toString()).equals(cleanLieux(tr2.getArrivee().toString()))) {
            return false;
        }
        // if (tr1.getModalite() != tr2.getModalite()) {
        // return false;
        // }
        return true;
    }

    /**
     * @param che1
     * @param che2
     * @return true si les chemins sont égaux, false sinon
     */
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

    /**
     * @param chemins
     * @param nb_trajet
     * @return Liste de Chemin sans doublons
     */
    public static List<Chemin> removeDuplicates(List<Chemin> chemins, int nb_trajet) {
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
     * 
     * @param che     Chemin
     * @param critere Critère
     * @return Chaîne de caractères
     */
    public static String cheminWithCorreBis(Chemin che, TypeCout critere) {
        String r = "";
        // on enleve les arrete vers alpha et omega
        che.aretes().remove(0);
        che.aretes().remove(che.aretes().size() - 1);

        for (Chemin cheModal : CheminImpl.splitByModalite(che)) {
            if (!r.isEmpty()) {
                r += "puis ";
            }
            r += cheModal.aretes().get(0).getModalite() + " de " +
                    cleanLieux(cheModal.aretes().get(0).getDepart().toString()) + " à "
                    + cleanLieux(cheModal.aretes().get(cheModal.aretes().size() - 1).getArrivee().toString()) + " ";
            // if (cheModal.aretes().size() > 1) {
            // r += "en passant par";
            // for (int i = 1; i < cheModal.aretes().size(); i++) {
            // r += " " + cheModal.aretes().get(i).getDepart() + "";
            // if (i < cheModal.aretes().size() - 1) {
            // r += ",";
            // }

            // }
            // }

        }
        if (critere == TypeCout.PRIX) {
            r += "pour un prix total de : " + che.poids() + " €";
        } else if (critere == TypeCout.CO2) {
            r += "pour une pollution totale de : " + che.poids() + " kgCO2e";
        } else if (critere == TypeCout.TEMPS) {
            r += "pour une durée totale de : " + che.poids() + " minutes";
        }
        return r;
    }

    /**
     * Retourne une chaîne de caractères représentant un chemin
     * 
     * @param che     Chemin
     * @param critere Critère
     * @return Chaîne de caractères
     */
    public static String cheminWithCorreArrow(Chemin che, TypeCout critere) {
        if (che.aretes().size() == 0) {
            return "";
        }
        String r = "";
        // on enleve les arrete vers alpha et omega
        che.aretes().remove(0);
        if (che.aretes().size() == 0) {
            return "";
        }
        che.aretes().remove(che.aretes().size() - 1);
        boolean first = true;
        for (Chemin cheModal : CheminImpl.splitByModalite(che)) {
            // if (!r.isEmpty()) {
            // r += "puis ";
            // }
            if (first) {
                first = false;
                r += cleanLieux(cheModal.aretes().get(0).getDepart().toString());
            }
            r += "  —— " + cheModal.aretes().get(0).getModalite().toString().toLowerCase() + " ——►  " +
                    cleanLieux(cheModal.aretes().get(cheModal.aretes().size() - 1).getArrivee().toString()) + " ";

        }
        // if (critere == TypeCout.PRIX) {
        // r += "pour un prix total de : " + che.poids() + " €";
        // } else if (critere == TypeCout.CO2) {
        // r += "pour une pollution totale de : " + che.poids() + " kgCO2e";
        // } else if (critere == TypeCout.TEMPS) {
        // r += "pour une durée totale de : " + che.poids() + " minutes";
        // }
        return r;
    }

    /**
     * @param path
     * @return ArrayList de String contenant les données d'un fichier CSV
     */
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

    /**
     * Vérifie si les données passées en paramètre sont valides
     * 
     * @param args Données
     * @return Vrai si les données sont valides, faux sinon
     */
    public static boolean donneesValides(ArrayList<String> args) {
        if (args.isEmpty()) {
            return false;
        }
        ArrayList<String> toRemove = new ArrayList<>();
        for (String arg : args) {
            String[] elements = arg.split(";");
            if (elements.length != 6 || !estNombre(elements[3]) || !estNombre(elements[4]) || !estNombre(elements[5]) ||
                    elements[0].isEmpty() || elements[1].isEmpty() || elements[2].isEmpty()
                    || elements[3].isEmpty() || elements[4].isEmpty() || elements[5].isEmpty()) {
                        if (arg.equals("")) {
                            toRemove.add(arg);
                        }else {
                            return false;
                        }
                
            }
        }
        args.removeAll(toRemove);
        return true;
    }

}
