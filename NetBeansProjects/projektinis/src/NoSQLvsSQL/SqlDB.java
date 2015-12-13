/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NoSQLvsSQL;

import orm.Komentaras;
import orm.Megstamiausias;
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
    }
    
    public void generuotiDuomenis() throws ParseException {
        // serialu generavimas
        for(int i = 0 ; i < serialuKiekis; i++)
        {
            Serialas serialas = generuotiSeriala();
            generuotiSerijas(serialas);
            generuotiZanrus(serialas);
            serialai.add(serialas);
        }
        
        // vartotoju ir megstamiausiu serialu generavimas
        for(int i = 0 ; i < vartotojuKiekis; i++)
        {
            Vartotojas vartotojas = generuotiVartotoja();
            vartotojai.add(vartotojas);
            
            List<Megstamiausias> megstamiausi = new ArrayList<>();
            for(int j = 0; j < megstamiausiuKiekis; j++)
            {
                Megstamiausias megstamiausias = generuotiMegstamiausia(serialai.get(rand.nextInt(serialuKiekis)), vartotojas);
                megstamiausi.add(megstamiausias);
            }
            vartotojas.setMegstamiausi(megstamiausi); 
        }
     
        // uzpildoma komentarais
        for(Serialas serialas : serialai)
        {
            for(Serija serija : serialas.getSerijos())
            {
                List<Komentaras> komentaruSarasas = new ArrayList<>();       
                for(int k = 0; k < komentaruKiekisSerijai; k++)
                {
                    Komentaras komentaras = generuotiKomentara(vartotojai.get(rand.nextInt(vartotojuKiekis)), serija);
                    komentaruSarasas.add(komentaras);
                }
                serija.setKomentarai(komentaruSarasas);
            }
        }
    }

    private void generuotiZanrus(Serialas serialas)
    {
        List<Zanras> zanrai = new ArrayList<>();
        for(int z = 0; z < zanruKiekisSerialui; z++)
        {
            Zanras zanras = generuotiZanra(serialas);
            zanras.setSerialas(serialas);
            zanrai.add(zanras);
        }
        serialas.setZanrai(zanrai);
    }
    
    private void generuotiSerijas(Serialas serialas)
    {
        List<Serija> serijuSarasas = new ArrayList<>();
 
        for(int i = 0; i < serijuKiekisSerialui; i++)
        {
            Serija serija = generuotiSerija();
            serija.setSerialas(serialas);
            serijuSarasas.add(serija);
        }
        serialas.setSerijos(serijuSarasas);
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

    private Serija generuotiSerija() {
        Serija s = new Serija();
        s.setPavadinimas(getRandomString(tekstoEilutesIlgis));
        s.setAprašymas(getRandomString(aprasymoIlgis));
        return s;
    }
    
    private Komentaras generuotiKomentara(Vartotojas vart, Serija serija)
    {
        Komentaras k = new Komentaras();
        
        k.setTurinys(getRandomString(aprasymoIlgis));
        k.setVartotojas(vart);
        k.setSerija(serija);
        
        if(vart.getKomentarai() == null)
        {
            vart.setKomentarai(new ArrayList());
        }
        vart.getKomentarai().add(k);
        
        return k;
    }

    private Megstamiausias generuotiMegstamiausia(Serialas serialas, Vartotojas vartotojas)
    {
        Megstamiausias m = new Megstamiausias();
        m.setSerialas(serialas);
        m.setVartotojas(vartotojas);
        return m;
    }

    private Zanras generuotiZanra(Serialas serialas)
    {
        Zanras z = new Zanras();
        z.setPavadinimas(getRandomString(tekstoEilutesIlgis));
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