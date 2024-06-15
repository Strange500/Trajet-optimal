package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ulille.but.sae_s2_2024.*;
import src.exception.CheminInexistantException;

public class IhmInterfaceImpl implements IhmInterface {

    private VoyageurIHM voyageurCorrespondanceTemps;
    private VoyageurIHM voyageurCorrespondancePrix;
    private VoyageurIHM voyageurCorrespondanceCO2;

    private static Double MAX_TEMPS = 960.0;
    private static Double MAX_PRIX = 750.0;
    private static Double MAX_CO2 = 450.0;

    private ModaliteTransport preferredTransport;

    public IhmInterfaceImpl(String username) {
        voyageurCorrespondanceTemps = new VoyageurIHM(username, TypeCout.TEMPS);
        voyageurCorrespondancePrix = new VoyageurIHM(username, TypeCout.PRIX);
        voyageurCorrespondanceCO2 = new VoyageurIHM(username, TypeCout.CO2);
        preferredTransport = null;
    }

    public Set<String> getStartCity() {
        voyageurCorrespondanceCO2.getPlateforme().getLieux();
        Set<String> startCity = new HashSet<>();
        for (Lieu l : voyageurCorrespondanceCO2.getPlateforme().getLieux()) {
            startCity.add(ToolsCorrespondance.cleanLieux(l.toString()));
        }
        return startCity;
    }

    public Set<String> getDestinationCity() {
        return getStartCity();
    }

    public Set<String> getTransport() {
        Set<String> transport = new HashSet<>();
        for (ModaliteTransport t : ModaliteTransport.values()) {
            transport.add(t.toString());
        }
        return transport;
    }

    public Set<String> getCriteria() {
        Set<String> criteria = new HashSet<>();
        for (TypeCout c : TypeCout.values()) {
            criteria.add(c.toString());
        }
        return criteria;
    }

    private Double processValue(TypeCout critere, double value) {
        switch (critere) {
            case TEMPS:
                return value / MAX_TEMPS;
            case PRIX:
                return value / MAX_PRIX;
            case CO2:
                return value / MAX_CO2;
        }
        return Double.valueOf(0);
    }

    private Double getScore(Chemin chemin, Podium<TypeCout> podiumTypeCout) {
        double c1, c2, c3;
        double p1 = 0.6, p2 = 0.35, p3 = 0.05;
        PlateformeCorrespondance p = voyageurCorrespondanceCO2.getPlateforme();
        c1 = processValue(podiumTypeCout.getFirst(), p.getPoidsByTypeCout(chemin, podiumTypeCout.getFirst()));
        c2 = processValue(podiumTypeCout.getSecond(), p.getPoidsByTypeCout(chemin, podiumTypeCout.getSecond()));
        c3 = processValue(podiumTypeCout.getThird(), p.getPoidsByTypeCout(chemin, podiumTypeCout.getThird()));

        return (p1 * c1) + (p2 * c2) + (p3 * c3);
    }

    public Map<Double, Chemin> getBestResults(Podium<TypeCout> podiumTypeCout, String dep, String arr)
            throws CheminInexistantException {
        Map<Double, Chemin> bestResults = new HashMap<>();
        voyageurCorrespondanceCO2.setDepart(dep);
        voyageurCorrespondanceCO2.setArrivee(arr);
        voyageurCorrespondanceCO2.setModalite(preferredTransport);
        voyageurCorrespondanceCO2.setNb_trajet(20);
        voyageurCorrespondanceCO2.thresholdCO2 = MAX_CO2;
        voyageurCorrespondanceCO2.thresholdPrix = MAX_PRIX.intValue();
        voyageurCorrespondanceCO2.thresholdTemps = MAX_TEMPS.intValue();
        List<Chemin> r = null;

        r = voyageurCorrespondanceCO2.computeBestPathTrigger();
        ToolsCorrespondance.applyThreshold(voyageurCorrespondanceCO2.getPlateforme(), r, TypeCout.CO2, getSeuilCO2());
        ToolsCorrespondance.applyThreshold(voyageurCorrespondanceCO2.getPlateforme(), r, TypeCout.PRIX, getSeuilPrix());
        ToolsCorrespondance.applyThreshold(voyageurCorrespondanceCO2.getPlateforme(), r, TypeCout.TEMPS,
                getSeuilTemps());

        if (r == null) {
            throw new CheminInexistantException();
        }

        for (Chemin c : r) {
            bestResults.put(getScore(c, podiumTypeCout), c);
        }
        return bestResults;
    }

    public void setPreferredTransport(ModaliteTransport preferredTransport) {
        this.preferredTransport = preferredTransport;
    }

    public ModaliteTransport getPreferredTransport() {
        return this.preferredTransport;
    }

    public Map<TypeCout, Double> getCheminPoids(Chemin ch) {
        Map<TypeCout, Double> poids = new HashMap<>();
        PlateformeCorrespondance p = voyageurCorrespondanceCO2.getPlateforme();
        poids.put(TypeCout.TEMPS, p.getPoidsByTypeCout(ch, TypeCout.TEMPS));
        poids.put(TypeCout.PRIX, p.getPoidsByTypeCout(ch, TypeCout.PRIX));
        poids.put(TypeCout.CO2, p.getPoidsByTypeCout(ch, TypeCout.CO2));
        return poids;
    }

    public double getSeuilPrix() {
        return MAX_PRIX;
    }

    public double getSeuilCO2() {
        return MAX_CO2;
    }

    public double getSeuilTemps() {
        return MAX_TEMPS;
    }

    public void setSeuilPrix(double seuilPrix) {
        MAX_PRIX = seuilPrix;
    }

    public void setSeuilCO2(double seuilCO2) {
        MAX_CO2 = seuilCO2;
    }

    public void setSeuilTemps(double seuilTemps) {
        MAX_TEMPS = seuilTemps;
    }

    public static void main(String[] args) {
        IhmInterfaceImpl ihm = new IhmInterfaceImpl("test");
        System.out.println(ihm.getStartCity());
        System.out.println(ihm.getDestinationCity());
        System.out.println(ihm.getTransport());
        System.out.println(ihm.getCriteria());

        Podium<TypeCout> podiumTypeCout = new Podium<>();
        podiumTypeCout.setFirst(TypeCout.TEMPS);
        podiumTypeCout.setSecond(TypeCout.PRIX);
        podiumTypeCout.setThird(TypeCout.CO2);

        // System.out.println(ihm.getBestResults(podiumTypeCout, "Lille", "Paris",
        // null));
        try {
            Map<Double, Chemin> r = ihm.getBestResults(podiumTypeCout, "Lille", "Paris");
            Set<Double> set = r.keySet();
            List<Double> list = new ArrayList<>(set);
            Collections.sort(list);
            System.out.println("NOUVEAU CLASSEMENT : ");
            for (Double key : list) {
                System.out.println(
                        key + " : " + ToolsCorrespondance.cheminWithCorreBis(r.get(key), podiumTypeCout.getSecond()));
            }
            System.out.println("\n\nANCIEN CLASSEMNT :");
            try {
                List<Chemin> chemins = ihm.voyageurCorrespondanceCO2.computeBestPathTrigger();
                if (chemins != null) {
                    System.out.println("Les trajets recommandés de " + ihm.voyageurCorrespondanceCO2.depart + " à "
                            + ihm.voyageurCorrespondanceCO2.arrivee + " sont :");
                    for (int i = 0; i < chemins.size(); i++) {
                        System.out
                                .println(i + 1 + ") " + ToolsCorrespondance.cheminWithCorreBis(chemins.get(i),
                                        ihm.voyageurCorrespondanceCO2.critere));
                    }
                } else {
                    System.out.println("Aucun chemin trouvé pour les critères demandés");
                }

            } catch (CheminInexistantException e) {
                e.printStackTrace();
            }
        } catch (CheminInexistantException e) {
            e.printStackTrace();
        }

    }
}
