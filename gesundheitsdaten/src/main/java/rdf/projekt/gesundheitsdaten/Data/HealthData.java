package rdf.projekt.gesundheitsdaten.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "gesundheitsdaten")
public class HealthData {

    @Id
    private String id;
    private int Statistik_Code;
    private String Statistik_Bezeichnung;
    private String Zeit_Code;
    private String Zeit_Bezeichnung;
    private int Jahr;        
    private String Ausgabentraeger_Code;  
    private String Ausgabentraeger;  
    private int Ausgaben;
    private String Ausgaben_in;
    private String Ausgabenart;

    public String getZeit_Code() {
        return this.Zeit_Code;
    }

    public HealthData() {
    }   

    public void setZeit_Code(String Zeit_Code) {
        this.Zeit_Code = Zeit_Code;
    }

    public String getZeit_Bezeichnung() {
        return this.Zeit_Bezeichnung;
    }

    public void setZeit_Bezeichnung(String Zeit_Bezeichnung) {
        this.Zeit_Bezeichnung = Zeit_Bezeichnung;
    }

    public String getAusgabenart() {
        return this.Ausgabenart;
    }

    public void setAusgabenart(String Ausgabenart) {
        this.Ausgabenart = Ausgabenart;
    }
   
   
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAusgaben() {
        return this.Ausgaben;
    }

    public void setAusgaben(int Ausgaben) {
        this.Ausgaben = Ausgaben;
    }

    public String getAusgaben_in() {
        return this.Ausgaben_in;
    }

    public void setAusgaben_in(String Ausgaben_in) {
        this.Ausgaben_in = Ausgaben_in;
    }

    public String getAusgabentraeger() {
        return this.Ausgabentraeger;
    }

    public void setAusgabentraeger(String Ausgabentraeger) {
        this.Ausgabentraeger = Ausgabentraeger;
    }

    public String getAusgabentraeger_Code() {
        return this.Ausgabentraeger_Code;
    }

    public void setAusgabentraeger_Code(String Ausgabentraeger_Code) {
        this.Ausgabentraeger_Code = Ausgabentraeger_Code;
    }

    public int getJahr() {
        return this.Jahr;
    }

    public void setJahr(int Jahr) {
        this.Jahr = Jahr;
    }

    public String getStatistik_Bezeichnung() {
        return this.Statistik_Bezeichnung;
    }

    public void setStatistik_Bezeichnung(String Statistik_Bezeichnung) {
        this.Statistik_Bezeichnung = Statistik_Bezeichnung;
    }

    public int getStatistik_Code() {
        return this.Statistik_Code;
    }

    public void setStatistik_Code(int Statistik_Code) {
        this.Statistik_Code = Statistik_Code;
    }


    

    
}
