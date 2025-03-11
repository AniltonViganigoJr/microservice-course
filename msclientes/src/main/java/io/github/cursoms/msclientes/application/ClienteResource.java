package io.github.cursoms.msclientes.application;

import io.github.cursoms.msclientes.application.representation.ClienteSaveRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("clientes")
public class ClienteResource {

    private static final Logger log = LoggerFactory.getLogger(ClienteResource.class);
    private final ClienteService service;

    public ClienteResource(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public String status(){
        log.info("Obtendo o status do microservice de clientes");
        return "OK";
    }

    @PostMapping
    public ResponseEntity save(@RequestBody ClienteSaveRequest request) {
        var cliente = request.toModel();
        service.save(cliente);
        //http:localhost:PORT/clientes?cpf={numero_do_cpf} -> Modelo de URL que será criada.
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity dadosCliente(@RequestParam("cpf") String cpf){
        var cliente = service.getByCpf(cpf);
        if (cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
}