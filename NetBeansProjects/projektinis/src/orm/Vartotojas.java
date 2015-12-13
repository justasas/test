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



public class Vartotojas implements Serializable {

    @Basic
    private String paštas;

    @OneToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},targetEntity = Komentaras.class,mappedBy = "vartotojas")
    private Collection<Komentaras> komentarai;

    @Basic
    private String slaptažodis;

    @Basic
    private String pavardė;

    @OneToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},targetEntity = Megstamiausias.class,mappedBy = "vartotojas")
    private Collection<Megstamiausias> megstamiausi;

    @Basic
    private String vardas;

    @Basic
    private String vart_vardas;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    public Vartotojas() {

    }
   
    public String getPaštas() {
        return this.paštas;
    }

    public void setPaštas(String paštas) {
        this.paštas = paštas;
    }
   
    public Collection<Komentaras> getKomentarai() {
        return this.komentarai;
    }

    public void setKomentarai(Collection<Komentaras> komentarai) {
        this.komentarai = komentarai;
    }
   
    public String getSlaptažodis() {
        return this.slaptažodis;
    }

    public void setSlaptažodis(String slaptažodis) {
        this.slaptažodis = slaptažodis;
    }
   
    public String getPavardė() {
        return this.pavardė;
    }

    public void setPavardė(String pavardė) {
        this.pavardė = pavardė;
    }
   
    public Collection<Megstamiausias> getMegstamiausi() {
        return this.megstamiausi;
    }

    public void setMegstamiausi(Collection<Megstamiausias> megstamiausi) {
        this.megstamiausi = megstamiausi;
    }
   
    public String getVardas() {
        return this.vardas;
    }

    public void setVardas(String vardas) {
        this.vardas = vardas;
    }
   
    public String getVart_vardas() {
        return this.vart_vardas;
    }

    public void setVart_vardas(String vart_vardas) {
        this.vart_vardas = vart_vardas;
    }
   
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
