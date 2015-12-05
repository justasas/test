package orm;


import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity

public class Megstamiausias implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Id
    @ManyToOne(cascade = {CascadeType.ALL}, targetEntity = Serialas.class)
    private Serialas serialas;

    public Megstamiausias() {

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
