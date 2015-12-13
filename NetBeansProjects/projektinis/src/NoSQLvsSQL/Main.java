package NoSQLvsSQL;

import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.client.MongoCollection;
import orm.Serialas;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.Document;

/**
 *
 * @author unknown
 */
public class Main {
    private static EntityManagerFactory factory;
    
    public static void main(String args[]) throws ParseException
    {
//          Persistence.generateSchema("PSIPU", null);
        
        // ----- duomenu generavimas -----
        
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

        // saugoti duomenis i PostgreSQL
        factory = Persistence.createEntityManagerFactory("PSIPU");
        EntityManager em = factory.createEntityManager();
        
        for(Serialas serialas : sqlDb.serialai)
        {
            em.getTransaction().begin();
            em.persist(serialas);
            em.getTransaction().commit();
        }
        
        
        MongoDB mongoDuomenys = MongoDB.generuotiAtsitiktiniusDuomenis(sqlDb.serialai, sqlDb.vartotojai);
        MongoCollection<Document> mSerialai = MongoDB.db.getCollection("serialai");
        MongoCollection<Document> mSerijos = MongoDB.db.getCollection("serijos");
        MongoCollection<Document> mKomentarai = MongoDB.db.getCollection("komentarai");
        MongoCollection<Document> mMegstamiausi = MongoDB.db.getCollection("serialai");
        MongoCollection<Document> mVartotojai = MongoDB.db.getCollection("vartotojai");
       
        mSerialai.insertMany(mongoDuomenys.serialai);
        mSerijos.insertMany(mongoDuomenys.serijos);
        mKomentarai.insertMany(mongoDuomenys.komentarai);
        mVartotojai.insertMany(mongoDuomenys.vartotojai);
        mMegstamiausi.insertMany(mongoDuomenys.komentarai);

    }
    
    gen
}
// megstamiausi - vartotojo nera
// pavardes nnera, serialo nera