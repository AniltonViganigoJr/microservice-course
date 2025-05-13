package io.github.cursoms.mscartoes.application.representation;

import io.github.cursoms.mscartoes.application.ClienteCartaoService;
import io.github.cursoms.mscartoes.domain.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartoesPorClienteResponse {

    private String name;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public static CartoesPorClienteResponse toModel(ClienteCartao model){
        return new CartoesPorClienteResponse(
                model.getCartao().getNome(),
                model.getCartao().getBandeira().toString(),
                model.getLimite()
        );
    }
}
