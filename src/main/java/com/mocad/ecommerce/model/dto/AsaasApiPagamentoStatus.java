package com.mocad.ecommerce.model.dto;
/**
 *
 * Armazena URL da API de Key da chave e tipos de pagamento
 * @author alex_
 *
 */
public class AsaasApiPagamentoStatus {


  public static String BOLETO = "BOLETO";
  public static String CREDIT_CARD = "CREDIT_CARD";
  public static String PIX = "PIX";
  public static String BOLETO_PIX = "UNDEFINED"; /*conbrança que pode ser paga por pir, boleto e cartão*/

  public static String URL_API_ASAAS = "https://www.asaas.com/api/v3/";
//  public static String URL_API_ASAAS = "https://sandbox.asaas.com/api/v3/";


  public static String API_KEY = "$aact_YTU5YTE0M2M2N2I4MTliNzk0YTI5N2U5MzdjNWZmNDQ6OjAwMDAwMDAwMDAwMDAzNzU1NzM6OiRhYWNoXzI1YjBmNDJlLTRmNDItNGUzZi04NmQ5LTU3OGRkNWRhMmE4Yw==";
//  public static String API_KEY = "$aact_YTU5YTE0M2M2N2I4MTliNzk0YTI5N2U5MzdjNWZmNDQ6OjAwMDAwMDAwMDAwMDAwNzEzMTA6OiRhYWNoX2M5NWNiMDkwLTM4MTItNGFlNC04NjA1LTk0MDIxNzFiMzYzYw==";

}