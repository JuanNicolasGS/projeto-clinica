package com.clinica.util;

public class TelefoneUtil {

    public static boolean isValido(String telefone) {
        if (telefone == null) return false;
        String regex = "\\(\\d{2}\\) \\d{4,5}-\\d{4}";
        return telefone.matches(regex);
    }

}
