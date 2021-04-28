package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.model.Nadador;
import es.uji.ei1027.clubesportiu.model.Prova;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;

public class ProvaValidator {

    public boolean supports(Class<?> cls) {
        return Prova.class.equals(cls);
    }

    public void validate(Object obj, Errors errors) {
        Prova prova = (Prova) obj;
        if (prova.getNom().trim().equals(""))
            errors.rejectValue("nom", "obligatori",
                    "Cal introduir un valor");

        if (prova.getDescripcio().trim().equals(""))
            errors.rejectValue("descripcio", "obligatori",
                    "Cal introduir una descripcio");
    }
}
