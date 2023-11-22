package com.mocad.ecommerce.utils;

import java.util.InputMismatchException;

public class ValidaCNPJ {

    public static boolean isCNPJ(String CNPJ) {
        // Aplicar filtro para manter apenas os números
        CNPJ = CNPJ.replaceAll("[^0-9]", "");

        // Considera-se erro CNPJs formados por uma sequência de números iguais ou com comprimento diferente de 14
        if (CNPJ.matches("(\\d)\\1+") || CNPJ.length() != 14) {
            return false;
        }

        char dig13, dig14;
        int sm, i, r, num, peso;

        try {
            // Cálculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - '0');
                sm += num * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }

            r = sm % 11;
            dig13 = (r < 2) ? '0' : (char) ((11 - r) + '0');

            // Cálculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - '0');
                sm += num * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }

            r = sm % 11;
            dig14 = (r < 2) ? '0' : (char) ((11 - r) + '0');

            // Verifica se os dígitos calculados conferem com os dígitos informados.
            return (dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13));
        } catch (InputMismatchException erro) {
            return false;
        }
    }

    public static String imprimeCNPJ(String CNPJ) {
        // Máscara do CNPJ: 99.999.999.9999-99
        return CNPJ.substring(0, 2) + "." + CNPJ.substring(2, 5) + "." +
                CNPJ.substring(5, 8) + "." + CNPJ.substring(8, 12) + "-" +
                CNPJ.substring(12, 14);
    }
}
