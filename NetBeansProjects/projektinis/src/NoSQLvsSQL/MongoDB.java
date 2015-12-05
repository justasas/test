package NoSQLvsSQL;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.List;

public class MongoDB {

    MongoClient mongoClient = new MongoClient();
    MongoDatabase db = mongoClient.getDatabase("db1");
          
    public BasicDBObject generuotiVartotoja(int i)
    {
	BasicDBObject document = new BasicDBObject();
	document.put("vart_vardas", "vart_vardas" + i);
	document.put("pastas", "pastas@pastas" + i);
	document.put("slaptazodis", "slaptazodis" + i);
	document.put("vardas", "vardas" + i);
        document.put("paparde", "pavarde" + i);
        return document;
    }

    public List<BasicDBObject> generuotiVartotojus(int kiekis)
    {
        List<BasicDBObject> vartotojai = new ArrayList<>();
     
        for(int i = 0; i < kiekis; i++)
        {
            vartotojai.add(generuotiVartotoja(i));
        }
        
        return vartotojai;
    }
        
    public BasicDBObject generuotiSeriala(int i)
    {
	BasicDBObject serialas = new BasicDBObject();
	serialas.put("pavadinimas", "pavadinimas1" + i);
	serialas.put("isleista", "isleista" + i);
	
        BasicDBObject serija = new BasicDBObject();
        serija.put("aprasymas", "aprasymas"+ i);
        serija.put("ivertinimas", 5.0);
        serija.put("pavadinimas", "pavadinimas" + i);
        
        serialas.put("serija", serija);
        return serialas;
    }
    
    public BasicDBObject generuotiSerija(int i)
    {
        BasicDBObject serija = new BasicDBObject();
        serija.put("aprasymas", "aprasymas"+ i);
        serija.put("ivertinimas", 5.0);
        serija.put("pavadinimas", "pavadinimas" + i);
        
        return serija;
    }

}