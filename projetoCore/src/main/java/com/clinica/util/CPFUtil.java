package com.clinica.util;

public class CPFUtil {

    public static boolean isValido(String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11) return false;
        if (cpf.matches("(\\d)\\1{10}")) return false;
        int soma = 0, peso = 10;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * peso--;
        }
        int resto = 11 - (soma % 11);
        int dig1 = resto > 9 ? 0 : resto;
        soma = 0; peso = 11;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * peso--;
        }
        resto = 11 - (soma % 11);
        int dig2 = resto > 9 ? 0 : resto;
        return dig1 == (cpf.charAt(9) - '0') && dig2 == (cpf.charAt(10) - '0');
    }

    public static String formatar(String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        return cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
                                "$1.$2.$3-$4");
    }
}
