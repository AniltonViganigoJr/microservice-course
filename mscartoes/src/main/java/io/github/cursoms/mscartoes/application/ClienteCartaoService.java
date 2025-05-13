package io.github.cursoms.mscartoes.application;

import io.github.cursoms.mscartoes.domain.ClienteCartao;
import io.github.cursoms.mscartoes.infra.repository.ClienteCartaoRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository repository;

    public List<ClienteCartao> listarClienteByCpf(String cpf){
        return repository.findByCpf(cpf);
    }
}