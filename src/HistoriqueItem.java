package src;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2024.Chemin;

public class HistoriqueItem implements Serializable {
    private static final String FILENAME = "historique.ser";
    private LocalDate date;
    private String che;
    private double prix;
    private double pollution;
    private int temps;

    public HistoriqueItem(Chemin che, double prix, double pollution, double temps) {
        this(ToolsCorrespondance.cheminWithCorreArrow(che, null), prix, pollution, temps);
    }

    public HistoriqueItem(String che, double prix, double pollution, double temps) {
        this.che = che;
        this.prix = prix;
        this.pollution = pollution;
        this.temps = (int) temps;
        this.date = LocalDate.now();
    }

    public String getChe() {
        return che;
    }

    public void setChe(String che) {
        this.che = che;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public double getPollution() {
        return pollution;
    }

    public void setPollution(double pollution) {
        this.pollution = pollution;
    }

    public int getTemps() {
        return temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    public LocalDate getDate() {
        return date;
    }

    public static void save(List<HistoriqueItem> historique) throws IOException, FileNotFoundException {
        FileOutputStream fileOut = new FileOutputStream(FILENAME);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(historique);
        out.flush();
        out.close();
    }

    //@SuppressWarnings("unchecked")
    public static List<HistoriqueItem> load() throws IOException, FileNotFoundException, ClassNotFoundException{
        FileInputStream fileOut = new FileInputStream(FILENAME);
        ObjectInput out = new ObjectInputStream(fileOut);
        Object obj =  out.readObject();
        if (obj instanceof List) {
            out.close();
            return (List<HistoriqueItem>) obj;
        }else {
            out.close();
            return null;
        }
        
    }

    public static boolean saveExists() {
        return new java.io.File(FILENAME).exists();
    }

    public static void createSave() {
        try {
            FileOutputStream tmp = new FileOutputStream(FILENAME);
            ObjectOutputStream out = new ObjectOutputStream(tmp);
            out.writeObject(new ArrayList<HistoriqueItem>());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // HistoriqueItem h = new HistoriqueItem("chemin de test", 1.0, 2.0, 3);
        // System.out.println(h.getChe());
        // System.out.println(h.getPrix());
        // System.out.println(h.getPollution());
        // System.out.println(h.getTemps());

        // Serializable s = h;

        // List<HistoriqueItem> historique = new ArrayList<>();
        // historique.add(h);
        try {
            // HistoriqueItem.save(historique);
            List<HistoriqueItem> historique2 = HistoriqueItem.load();
            System.out.println(historique2.get(0) + " " + historique2.get(1));
        } catch(Exception e) {
            e.printStackTrace();
        }
        

    }

    

}
