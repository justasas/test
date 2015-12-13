package orm;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity



public class Zanras implements Serializable {

    @Basic
    private String pavadinimas;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false,cascade={CascadeType.ALL},targetEntity = Serialas.class)
    private Serialas serialas;

    public Zanras() {

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
   
    public Serialas getSerialas() {
        return this.serialas;
    }

    public void setSerialas(Serialas serialas) {
        this.serialas = serialas;
    }
}
