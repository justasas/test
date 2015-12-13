package NoSQLvsSQL;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import orm.Komentaras;
import orm.Megstamiausias;
import orm.Serialas;
import orm.Serija;
import orm.Vartotojas;
import orm.Zanras;

public class MongoDB {

    public final static MongoClient mongoClient = new MongoClient();
    public final static MongoDatabase db = mongoClient.getDatabase("db1");
    
    public List<Document> komentarai;
    public List<Document> vartotojai;
    public List<Document> serialai;
    public List<Document> serijos;
    public List<Document> megstamiausi;

    private MongoDB()
    {
        this.komentarai = new ArrayList<Document>();
        this.vartotojai = new ArrayList<Document>();
        this.serialai = new ArrayList<Document>();
//        this.komentarai = new ArrayList<Document>();

        
    }
    
    public static MongoDB generuotiAtsitiktiniusDuomenis(List<Serialas> serialai, List<Vartotojas> vartotojai) {
        MongoDB db = new MongoDB();
//        db.createCollection("serialai");
        
        List<Megstamiausias> megstamiausi = new ArrayList<Megstamiausias>();
        
        for(Vartotojas v: vartotojai)
        {
            db.vartotojai.add(db.generuotiVartotoja(v));
            megstamiausi.addAll(v.getMegstamiausi());
        }
        
        for(Serialas s : serialai)
            db.serialai.add(db.generuotiSeriala(s));
        
        db.megstamiausi = db.generuotiVartotojuMegstamiausius(megstamiausi);
        
        return db;
    }
    
    private Document generuotiVartotoja(Vartotojas v)
    {
	Document document = new Document();
        document.put("_id", v.getId());
	document.put("vart_vardas", v.getVart_vardas());
	document.put("pastas", v.getPaštas());
	document.put("slaptazodis", v.getSlaptažodis());
	document.put("vardas", v.getVardas());
        document.put("pavarde", v.getPavardė());
        
        return document;
    }

    private List<Document> generuotiVartotojus(List<Vartotojas> vartotojai)
    {
        List<Document> ret = new ArrayList<>();

        for(Vartotojas v : vartotojai)
        {
            ret.add(generuotiVartotoja(v));
        }
        
        return ret;
    }

    private List<Document> generuotiSerijas(Serialas serialas)
    {
        List<Document> docs = new ArrayList<Document>();
                    
        for(Serija s : serialas.getSerijos()){
            Document serija = new Document();
            serija.put("_id", s.getId());
            serija.put("serialas_id", s.getSerialas().getId());
            serija.put("aprasymas", s.getAprašymas());
            serija.put("ivertinimas", s.getĮvertinimas());
            serija.put("pavadinimas", s.getPavadinimas());
            
            for(Komentaras k : s.getKomentarai())
            {
                Document komentaras = new Document();
                komentaras.put("_id", k.getId());
                komentaras.put("vartotojas_id", k.getVartotojas().getId());
                komentaras.put("serija_id", k.getSerija().getId());
                komentaras.put("turinys", k.getTurinys());
                komentarai.add(komentaras);
            }
//            serija.put("komentarai", komentarai);
            docs.add(serija);
        }

        return docs;
    }
    
    private Document generuotiSeriala(Serialas serialas)
    {
	Document serialasMongo = new Document();
        
        serialasMongo.put("_id", serialas.getId());
	serialasMongo.put("pavadinimas", serialas.getPavadinimas());
	serialasMongo.put("isleista", serialas.getIšleista().toString());

        serijos = generuotiSerijas(serialas);
        
        List<Document> zanrai = new ArrayList<Document>();
        for(Zanras z : serialas.getZanrai())
        {
            Document zanras = new Document();
            zanras.put("_id", z.getId());
            zanras.put("pavadinimas", z.getPavadinimas());
            zanrai.add(zanras);
        }        
        serialasMongo.put("zanrai", zanrai);
        
        return serialasMongo;
    }
    
    private List<Document> generuotiVartotojuMegstamiausius(List<Megstamiausias> megstamiausi)
    {
        List<Document> ret = new ArrayList<Document>();
        
        for(Megstamiausias megstamiausias : megstamiausi)
        {
            Document m = new Document();
            m.put("_id", megstamiausias.getId());
            m.put("serialas_id", megstamiausias.getSerialas().getId());
            m.put("megstamiausias_id", megstamiausias.getVartotojas().getId());
            ret.add(m);
        }
        
        return ret;
    }
}