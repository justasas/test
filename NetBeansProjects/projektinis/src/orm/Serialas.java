package orm;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity

public class Serialas implements Serializable {

    @Basic
    private Date išleista;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, targetEntity = Serija.class, mappedBy = "serialas")
    private Collection<Serija> serijos;

    @Basic
    private String pavadinimas;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, targetEntity = Zanras.class, mappedBy = "serialas")
    private Collection<Zanras> zanrai;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Serialas() {

    }

    public Date getIšleista() {
        return this.išleista;
    }

    public void setIšleista(Date išleista) {
        this.išleista = išleista;
    }

    public Collection<Serija> getSerijos() {
        return this.serijos;
    }

    public void setSerijos(Collection<Serija> serijos) {
        this.serijos = serijos;
    }

    public String getPavadinimas() {
        return this.pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    public Collection<Zanras> getZanrai() {
        return this.zanrai;
    }

    public void setZanrai(Collection<Zanras> zanrai) {
        this.zanrai = zanrai;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }  
}
