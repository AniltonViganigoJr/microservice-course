package io.github.cursoms.mscartoes.application;

import io.github.cursoms.mscartoes.application.representation.CartaoSaveRequest;
import io.github.cursoms.mscartoes.application.representation.CartoesPorClienteResponse;
import io.github.cursoms.mscartoes.domain.Cartao;
import io.github.cursoms.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("cartoes")
public class CartaoResource {

    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

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

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf){
        List<ClienteCartao> list = clienteCartaoService.listarClienteByCpf(cpf);
        List<CartoesPorClienteResponse> resultList = list.stream()
                .map(CartoesPorClienteResponse::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultList);
    }
}