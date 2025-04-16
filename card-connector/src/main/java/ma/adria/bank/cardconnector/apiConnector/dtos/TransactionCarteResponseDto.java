package ma.adria.bank.cardconnector.apiConnector.dtos;

import ma.adria.bank.dto.monetique.MouvementCarteDto;

import java.util.List;

public class TransactionCarteResponseDto {
    private List<MouvementCarteDto> mouvements;
    private String encours;
}
