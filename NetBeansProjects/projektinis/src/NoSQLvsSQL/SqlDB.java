/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NoSQLvsSQL;

import orm.Komentaras;
import orm.Megstamiausi;
import orm.Serialas;
import orm.Serija;
import orm.Vartotojas;
import orm.Zanras;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author unknown
 */
public class SqlDB {
    public List<Vartotojas> vartotojai;
    public List<Serialas> serialai;
    public List<Serija> serijos;
    public List<Zanras> zanrai;
    public List<Komentaras> komentarai;
    public List<Megstamiausi> megstamiausi;
        
    private Random rand = new Random();
    private int tekstoEilutesIlgis = 20;
    private int aprasymoIlgis = 100;    
    
    int serialuKiekis, serijuKiekisSerialui,
        komentaruKiekisSerijai, zanruKiekisSerialui, 
        vartotojuKiekis, megstamiausiuKiekis;

    SqlDB(int serialuKiekis, int serijuKiekisSerialui,
        int komentaruKiekisSerijai, int zanruKiekisSerialui, 
        int vartotojuKiekis, int megstamiausiuKiekis)
    {
        this.serialuKiekis = serialuKiekis;
        this.serijuKiekisSerialui = serijuKiekisSerialui;
        this.komentaruKiekisSerijai = komentaruKiekisSerijai;
        this.zanruKiekisSerialui = zanruKiekisSerialui;
        this.vartotojuKiekis = vartotojuKiekis;
        this.megstamiausiuKiekis = megstamiausiuKiekis;
        
        vartotojai = new ArrayList<>();
        serialai = new ArrayList<>();
        serijos = new ArrayList<>();
        zanrai = new ArrayList<>();
        komentarai = new ArrayList<>();
        megstamiausi = new ArrayList<>();
    }
    
    public void generuotiDuomenis() throws ParseException {
        // vartotoju generavimas
        for(int i = 0 ; i < vartotojuKiekis; i++)
            vartotojai.add(generuotiVartotoja());

        // serialu generavimas
        for(int i = 0 ; i < serialuKiekis; i++)
        {
            Serialas serialas = generuotiSeriala();
            
            generuotiSerijas(serialas);
            generuotiZanrus(serialas);

            serialai.add(serialas);
        }
    }

    private void generuotiZanrus(Serialas serialas)
    {
        for(int z = 0; z < zanruKiekisSerialui; z++)
        {
            Zanras zanras = generuotiZanra(serialas);
            zanrai.add(zanras);
        }
    }
    
    private void generuotiSerijas(Serialas serialas)
    {
        for(int i = 0; i < serijuKiekisSerialui; i++)
        {
            Serija serija = generuotiSerija(serialas);
            serijos.add(serija);
            for(int k = 0; k < komentaruKiekisSerijai; k++)
            {
                Komentaras komentaras;
                komentaras = generuotiKomentara(serija, vartotojai.get(rand.nextInt(vartotojuKiekis)));
                komentarai.add(komentaras);
            }
        }
    }
    
    public Vartotojas generuotiVartotoja()
    {
        Vartotojas v = new Vartotojas();
        
        v.setVardas(getRandomAlphabeticString(tekstoEilutesIlgis));
        v.setPavardė(getRandomAlphabeticString(tekstoEilutesIlgis));
        v.setPaštas(getRandomString(tekstoEilutesIlgis));
        v.setSlaptažodis(getRandomString(tekstoEilutesIlgis));
        v.setVart_vardas(getRandomString(tekstoEilutesIlgis));
        
        return v;
    }
    
    public Serialas generuotiSeriala() throws ParseException
    {
        Serialas serialas = new Serialas();
        
        serialas.setPavadinimas(getRandomString(tekstoEilutesIlgis));
        serialas.setIšleista(new java.sql.Date(getRandomDate().getTime()));
        
        return serialas;
    }

    private Serija generuotiSerija(Serialas serialas) {
        Serija s = new Serija();
        s.setPavadinimas(getRandomString(tekstoEilutesIlgis));
        s.setAprašymas(getRandomString(aprasymoIlgis));
        s.setSerialas(serialas);
        return s;
    }
    
    private Komentaras generuotiKomentara(Serija serija, Vartotojas vart)
    {
        Komentaras k = new Komentaras();
        
        k.setTurinys(getRandomString(aprasymoIlgis));
        k.setVartotojas(vart);
        k.setSerija(serija);

        return k;
    }

    private Megstamiausi generuotiMegstamiausia(Serialas serialas, Vartotojas vart)
    {
        Megstamiausi m = new Megstamiausi();
        
        m.setSerialas(serialas);
        m.setVartotojas(vart);

        return m;
    }

    private Zanras generuotiZanra(Serialas serialas)
    {
        Zanras z = new Zanras();
        
        z.setPavadinimas(getRandomString(tekstoEilutesIlgis));
        z.setSerialas(serialas);
        
        return z;
    }
        
    public String getRandomString(int length)
    {
        return RandomStringUtils.random(rand.nextInt(length));
    }
        
    public String getRandomAlphabeticString(int length)
    {
        return RandomStringUtils.randomAlphabetic(rand.nextInt(length));
    }
    
    public Date getRandomDate() throws ParseException
    {
        long offset = Timestamp.valueOf("2000-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2016-01-01 00:00:00").getTime();
        long diff = end - offset + 1;
        Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
 
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        return simpleDateFormat.parse(rand.toString());
    }
}