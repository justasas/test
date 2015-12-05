package orm;


import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class Zanras implements Serializable {

    @Basic
    private String pavadinimas;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
}
