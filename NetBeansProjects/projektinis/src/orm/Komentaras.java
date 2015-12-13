package orm;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity

public class Komentaras implements Serializable {

    @Basic
    private String turinys;

    @ManyToOne(cascade = {CascadeType.ALL}, targetEntity = Serija.class)
    private Serija serija;

    @Column(nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.ALL}, targetEntity = Vartotojas.class)
    private Vartotojas vartotojas;

    public Komentaras() {

    }

    public String getTurinys() {
        return this.turinys;
    }

    public void setTurinys(String turinys) {
        this.turinys = turinys;
    }

    public Serija getSerija() {
        return this.serija;
    }

    public void setSerija(Serija serija) {
        this.serija = serija;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vartotojas getVartotojas() {
        return this.vartotojas;
    }

    public void setVartotojas(Vartotojas vartotojas) {
        this.vartotojas = vartotojas;
    }
}
