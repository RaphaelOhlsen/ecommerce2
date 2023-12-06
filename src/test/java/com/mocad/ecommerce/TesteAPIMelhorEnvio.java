package com.mocad.ecommerce;

import com.mocad.ecommerce.env.ApiTokenIntegracao;
import okhttp3.*;

import java.io.IOException;

public class TesteAPIMelhorEnvio {

  public static void main(String[] args) throws IOException {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{ \"from\": { \"postal_code\": \"96020360\" }, \"to\": { \"postal_code\": \"01018020\" }, \"products\": [ { \"id\": \"x\", \"width\": 11, \"height\": 17, \"length\": 11, \"weight\": 0.3, \"insurance_value\": 10.1, \"quantity\": 1 }, { \"id\": \"y\", \"width\": 16, \"height\": 25, \"length\": 11, \"weight\": 0.3, \"insurance_value\": 55.05, \"quantity\": 2 }, { \"id\": \"z\", \"width\": 22, \"height\": 30, \"length\": 11, \"weight\": 1, \"insurance_value\": 30, \"quantity\": 1 } ] }");
    Request request = new Request.Builder()
      .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX + "api/v2/me/shipment/calculate")
      .method("POST", body)
      .addHeader("Accept", "application/json")
      .addHeader("Content-Type", "application/json")
      .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
      .addHeader("User-Agent", "raphael@mocad.dev")
      .build();

    Response response = client.newCall(request).execute();
    System.out.println(response.body().string());
  }
}
