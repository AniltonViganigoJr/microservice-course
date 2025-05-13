package io.github.cursoms.mscartoes.application;

import io.github.cursoms.mscartoes.application.representation.CartaoSaveRequest;
import io.github.cursoms.mscartoes.domain.Cartao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("cartoes")
public class CartaoResource {

    private final CartaoService cartaoService;
    //private final CartaoService cartaoService;

    public CartaoResource(CartaoService service){
        this.cartaoService = service;
    }

    @GetMapping
    public String status(){
        log.info("Obtendo informações do Microservice de Cartões...");
        return "OK";
    }

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody CartaoSaveRequest request){
        var cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam("renda") Long renda){
        List<Cartao> list = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(list);
    }
}