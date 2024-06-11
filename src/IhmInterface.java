package src;

import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;

public interface IhmInterface {
    

    public Set<String> getStartCity();

    public Set<String> getDestinationCity();

    public Set<String> getTransport();

    public Set<String> getCriteria();

    public Map<Double, Chemin> getBestResults(Podium<TypeCout> podiumTypeCout, String dep, String arr, ModaliteTransport transp);
    

}
