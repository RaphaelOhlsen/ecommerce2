package com.mocad.ecommerce;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mocad.ecommerce.env.ApiTokenIntegracao;
import com.mocad.ecommerce.model.dto.EmpresaTransporteDTO;
import okhttp3.*;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TesteAPIMelhorEnvio {

  public static void main(String[] args) throws IOException {

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, {
        "service": "3,"agency":49,"from":{"name":"Nome do remetente","phone":"53984470102","email":"contato@melhorenvio.com.br","document":"16571478358","company_document":"89794131000100","state_register":"123456","address":"Endereço do remetente","complement":"Complemento","number":"1","district":"Bairro","city":"São Paulo","country_id":"BR","postal_code":"01002001","note":"observação"},"to":{"name":"Nome do destinatário","phone":"53984470102","email":"contato@melhorenvio.com.br","document":"25404918047","company_document":"07595604000177","state_register":"123456","address":"Endereço do destinatário","complement":"Complemento","number":"2","district":"Bairro","city":"Porto Alegre","state_abbr":"RS","country_id":"BR","postal_code":"90570020","note":"observação"},"products":[{"name":"Papel adesivo para etiquetas 1","quantity":3,"unitary_value":100.00},{"name":"Papel adesivo para etiquetas 2","quantity":1,"unitary_value":700.00}],"volumes":[{"height":15,"width":20,"length":10,"weight":3.5}],"options":{"insurance_value":1000.00,"receipt":false,"own_hand":false,"reverse":false,"non_commercial":false,"invoice":{"key":"31190307586261000184550010000092481404848162"},"platform":"Nome da Plataforma","tags":[{"tag":"Identificação do pedido na plataforma, exemplo: 1000007","url":"Link direto para o pedido na plataforma, se possível, caso contrário pode ser passado o valor null"}]}}
);
    Request request = new Request.Builder()
        .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX + "api/v2/me/cart")
        .method("POST", body)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
        .addHeader("User-Agent", "raphael@mocad.dev")
        .build();

    Response response = client.newCall(request).execute();


    JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());

    List<EmpresaTransporteDTO> empresaTransporteDTOs = new ArrayList<EmpresaTransporteDTO>();

    for (JsonNode node : jsonNode) {
      EmpresaTransporteDTO empresaTransporteDTO = new EmpresaTransporteDTO();

      if (node.get("id") != null) {
        empresaTransporteDTO.setId(node.get("id").asText());
      }

      if (node.get("name") != null) {
        empresaTransporteDTO.setNome(node.get("name").asText());
      }

      if (node.get("price") != null) {
        empresaTransporteDTO.setValor(node.get("price").asText());
      }

      if (node.get("company") != null) {
        empresaTransporteDTO.setEmpresa(node.get("company").get("name").asText());
        empresaTransporteDTO.setPicture(node.get("company").get("picture").asText());
      }

      if (empresaTransporteDTO.dadosOK()) {
        empresaTransporteDTOs.add(empresaTransporteDTO);
      }
    }
    System.out.println(empresaTransporteDTOs.get(0).getNome());
  }
}
