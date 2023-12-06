package com.mocad.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mocad.ecommerce.env.ApiTokenIntegracao;
import com.mocad.ecommerce.model.dto.ConsultaFreteDTO;
import com.mocad.ecommerce.model.dto.EmpresaTransporteDTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FreteController {

  @GetMapping("/consultaFrete")
  public ResponseEntity<List<EmpresaTransporteDTO>> consultaFrete(@RequestBody ConsultaFreteDTO consultaFreteDTO) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(consultaFreteDTO);

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
    okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
    Request request = new Request.Builder()
        .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX + "api/v2/me/shipment/calculate")
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

    return ResponseEntity.ok(empresaTransporteDTOs);
  }
}
