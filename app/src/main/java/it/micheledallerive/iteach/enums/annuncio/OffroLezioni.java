package it.micheledallerive.iteach.enums.annuncio;

import it.micheledallerive.iteach.R;

public enum OffroLezioni {
    INDIVIDUALI("Individuali", R.id.chip_individuali),
    DI_GRUPPO("Di gruppo", R.id.chip_di_gruppo);

    private final String text;
    private final int id;

    OffroLezioni(String text, int id){
        this.text = text;
        this.id = id;
    }

    public String getText(){
        return this.text;
    }

    public int getId() {
        return id;
    }

    public static OffroLezioni getFromString(String string){
        switch(string){
            case "Individuali":
                return OffroLezioni.INDIVIDUALI;
            case "Di gruppo":
                return OffroLezioni.DI_GRUPPO;
        }
        return null;
    }
}
