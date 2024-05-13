package graphes;

import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;
import fr.ulille.but.sae_s2_2024.Trancon;
import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import java.util.List;
import fr.ulille.but.sae_s2_2024.Chemin;

public class Tp {

    public static int TEMP_CHANGEMENT = 10;
    private static String ALPHA = "ALPHA";
    private static String OMEGA = "OMEGA";
    
    public static String cheminWithCorre(Chemin che) {
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
        return r + " Durée totale: " + che.poids() + " minutes, dont " + IChemin.getCHangementDuration(che) + " minutes de correspondance";

}
    public static void main(String[] args) {
        MultiGrapheOrienteValue g = new MultiGrapheOrienteValue();

        ILieu alpha = new ILieu(ALPHA);
        ILieu omega = new ILieu(OMEGA);
        // gare
        ILieu aG = new ILieu("A");
        ILieu bG = new ILieu("B");
        ILieu cG = new ILieu("C");
        ILieu dG = new ILieu("D");
        // aeroport
        ILieu cA = new ILieu("C");
        ILieu dA = new ILieu("D");

        ITrancon deb = new ITrancon(alpha, aG, ModaliteTransport.TRAIN);
        ITrancon finG = new ITrancon(dG, omega, ModaliteTransport.TRAIN);
        ITrancon finA = new ITrancon(dA, omega, ModaliteTransport.AVION);

        ITrancon abT = new ITrancon(aG, bG, ModaliteTransport.TRAIN);
        ITrancon acT = new ITrancon(aG, cG, ModaliteTransport.TRAIN);
        ITrancon bcT = new ITrancon(bG, cG, ModaliteTransport.TRAIN);
        ITrancon bdT = new ITrancon(bG, dG, ModaliteTransport.TRAIN);
        ITrancon cdT = new ITrancon(cG, dG, ModaliteTransport.TRAIN);
        ITrancon cdA = new ITrancon(cA, dA, ModaliteTransport.AVION);
        ITrancon cdTA = new ITrancon(cG, dA, ModaliteTransport.AVION);
        ITrancon cdAT = new ITrancon(cA, dG, ModaliteTransport.AVION);

        g.ajouterSommet(alpha);
        g.ajouterSommet(omega);
        
        g.ajouterSommet(aG);
        g.ajouterSommet(bG);
        g.ajouterSommet(cG);
        g.ajouterSommet(dG);

        g.ajouterSommet(cA);
        g.ajouterSommet(dA);
        

        g.ajouterArete(abT, 90);
        g.ajouterArete(acT, 60);
        g.ajouterArete(bcT, 60);
        g.ajouterArete(bdT, 30);
        g.ajouterArete(cdT, 90);
        g.ajouterArete(cdTA, 30 + TEMP_CHANGEMENT);

        g.ajouterArete(finA, 0);    
        g.ajouterArete(finG, 0);
        g.ajouterArete(deb,0);

        List<Chemin> chemins = AlgorithmeKPCC.kpcc(g, alpha, omega, 3);
        


        System.out.println("Les 3 trajets recommandés de A à D sont:");
        for (int i = 0; i < chemins.size(); i++) {
            System.out.println((i+1)+") "  + cheminWithCorre(chemins.get(i)));
            
        }

        //System.out.println(cheminWithCorre(chemins.get(0)));
    }
}
