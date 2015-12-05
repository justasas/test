package orm;


import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity

public class Serija implements Serializable {

    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = Komentaras.class)
    private Collection<Komentaras> komentarai;

    @Basic
    private float įvertinimas;

    @Basic
    private String pavadinimas;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String aprašymas;

    public Serija() {

    }

    public Collection<Komentaras> getKomentarai() {
        return this.komentarai;
    }

    public void setKomentarai(Collection<Komentaras> komentarai) {
        this.komentarai = komentarai;
    }

    public float getĮvertinimas() {
        return this.įvertinimas;
    }

    public void setĮvertinimas(float įvertinimas) {
        this.įvertinimas = įvertinimas;
    }

    public String getPavadinimas() {
        return this.pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAprašymas() {
        return this.aprašymas;
    }

    public void setAprašymas(String aprašymas) {
        this.aprašymas = aprašymas;
    }
}
