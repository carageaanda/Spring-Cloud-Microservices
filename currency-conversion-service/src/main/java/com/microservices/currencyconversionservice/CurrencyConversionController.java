package com.microservices.currencyconversionservice;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy proxy;

    @Autowired
    private RestTemplate restTemplate;


    @Operation(summary = "Calculate currency conversion using Feign Client")
    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public EntityModel<CurrencyConversion> calculateCurrencyConversionFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {

        CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);

        // Create a link to the current method
        WebMvcLinkBuilder selfLinkBuilder = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                        .calculateCurrencyConversionFeign(from, to, quantity));

        // Add the link to the main resource
        EntityModel<CurrencyConversion> entityModel = EntityModel.of(currencyConversion);
        entityModel.add(selfLinkBuilder.withSelfRel());

        return entityModel;
    }

    @Operation(summary = "Calculate currency conversion")
    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public EntityModel<CurrencyConversion> calculateCurrencyConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {

        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversion> responseEntity = restTemplate.getForEntity(
                "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class,
                uriVariables);

        CurrencyConversion currencyConversion = responseEntity.getBody();

        // Create a link to the current method
        WebMvcLinkBuilder selfLinkBuilder = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                        .calculateCurrencyConversion(from, to, quantity));

        // Add the link to the main resource
        EntityModel<CurrencyConversion> entityModel = EntityModel.of(currencyConversion);
        entityModel.add(selfLinkBuilder.withSelfRel());

        return entityModel;
    }

}
