/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NoSQLvsSQL;

import orm.Serialas;
import orm.Vartotojas;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author unknown
 */
public class Main {
    Serialas serialas = new Serialas();
    private static EntityManagerFactory factory;
    
    public static void main(String args[]) throws ParseException
    {
        factory = Persistence.createEntityManagerFactory("PSIPU");
        EntityManager em = factory.createEntityManager();
          Persistence.generateSchema("PSIPU", null);
        
        int serialuKiekis = 1;
        int serijuKiekisSerialui = 1;
        int komentaruKiekisSerijai = 1;
        int zanruKiekisSerialui = 1; 
        int vartotojuKiekis = 1;
        int megstamiausiuKiekis = 1;

        SqlDB sqlDb = new SqlDB(serialuKiekis, serijuKiekisSerialui, 
                komentaruKiekisSerijai, zanruKiekisSerialui,
                vartotojuKiekis, megstamiausiuKiekis);
        
        sqlDb.generuotiDuomenis();
        System.out.println("aaa");
        System.out.print(sqlDb);

        for(Serialas serialas : sqlDb.serialai)
        {
            em.getTransaction().begin();
            em.persist(serialas);
            em.getTransaction().commit();
        }
    }
}