package com.mocad.ecommerce;

import com.mocad.ecommerce.utils.ValidaCNPJ;
import com.mocad.ecommerce.utils.ValidaCPF;

public class TesteCPFCNPJ {

    public static void main(String[] args) {
        boolean isCNPJ = ValidaCNPJ.isCNPJ("66.347.536/0001-96");

        boolean isCPF = ValidaCPF.isCPF("009.599.147-65");

        System.out.println("isCNPJ = " + isCNPJ);
        System.out.println("isCPF = " + isCPF);
    }
}
