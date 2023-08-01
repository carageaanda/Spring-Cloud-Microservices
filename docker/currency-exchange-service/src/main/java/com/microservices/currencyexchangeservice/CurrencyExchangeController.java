package com.microservices.currencyexchangeservice;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class CurrencyExchangeController {

    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Autowired
    private CurrencyExchangeRepository repository;

    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    @Operation(summary = "Currency exchange")
    @CircuitBreaker(name = "currencyExchange", fallbackMethod = "fallbackRetrieveExchangeValue")
    public EntityModel<CurrencyExchange> retrieveExchangeValue(
            @PathVariable String from,
            @PathVariable String to) {

        logger.info("retrieveExchangeValue called with {} to {}", from, to);

        CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);
        if (currencyExchange == null) {
            throw new RuntimeException("Unable to find data for " + from + " to " + to);
        }

        String port = environment.getProperty("local.server.port");
        currencyExchange.setEnvironment(port);

        // Create a link to the current method
        WebMvcLinkBuilder selfLinkBuilder = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                        .retrieveExchangeValue(from, to));

        // Add the link to the main resource
        EntityModel<CurrencyExchange> entityModel = EntityModel.of(currencyExchange);
        entityModel.add(selfLinkBuilder.withSelfRel());

        return entityModel;
    }

    @GetMapping("/currency-exchange")
    @Operation(summary = "Get all currency exchanges")
    public List<CurrencyExchange> getAllCurrencyExchanges() {
        return repository.findAll();
    }
}
