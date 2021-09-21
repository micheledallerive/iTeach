package it.micheledallerive.iteach.enums.annuncio;

import it.micheledallerive.iteach.R;

public enum SonoDisponibile {
    ONLINE("Online", R.id.chip_online),
    IN_PRESENZA("In presenza", R.id.chip_in_presenza),
    PER_SPOSTARMI("Per spostarmi", R.id.chip_spostarmi);

    private final String text;
    private final int id;

    SonoDisponibile(String text, int id){
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public static SonoDisponibile getFromString(String string){
        switch(string){
            case "Online":
                return SonoDisponibile.ONLINE;
            case "In presenza":
                return SonoDisponibile.IN_PRESENZA;
            case "Per spostarmi":
                return SonoDisponibile.PER_SPOSTARMI;
        }
        return null;
    }
}
