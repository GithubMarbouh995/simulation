package ma.adria.bank.cardconnector.apiConnector.services;

import lombok.RequiredArgsConstructor;
import ma.adria.bank.clients.*;
import ma.adria.bank.constantes.Constante;
import ma.adria.bank.dto.*;
import ma.adria.bank.dto.monetique.CarteDto;
import ma.adria.bank.dto.monetique.MouvementCarteDto;
import ma.adria.bank.dto.monetique.RecalculPinResponseDTO;
import ma.adria.bank.dto.monetique.TypeCarteDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CardService {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CardService.class);

    private final IGetCardListClient cardListClient;
    private final IGetCardMovementsClient cardMovementsClient;
    private final IOpposerCardClient opposerCardClient;
    private final ISuivicommandeCarte iSuivicommandeCarte;
    private final IGetCardPlafonds iGetCardPlafonds;
    private final ICommandeCarte iCommandeCarte;
    private final IBlocageDeblocageCarte iBlocageDeblocageCarte;
    private final IGetTypesCarte iGetTypesCarte;
    private final IClientCardRequest iClientCardRequest;
    private final IGetEncoursCarteClient encoursCarteClient;
    private final IGetInfoCarte getInfoCarte;
    private final IRecalculePin iRecalculePin;
    private final IModifyPlafondClient modifyPlafondClient;
    private final IEligibilityPlafondClient eligibilityPlafondClient;
    private final IDemandeRechargeCardClient demandeRechargeCardClient;
    private final IActivateUnsecurePaymentCardClient activateUnsecurePaymentCardClient;

    public List<CarteDto> getCardList(String idClient) throws Exception {
        return cardListClient.getGetCardList(idClient);
    }

    public List<MouvementCarteDto> getCardMovements(MouvementCarteRecherchFormDto mvm) throws Exception {
        return cardMovementsClient.getGetCardMovements(mvm);
    }

    public Map<String, Object> opposerCarte(String cardNumber) throws Exception {
        return opposerCardClient.opposerCarte(cardNumber);
    }

    public CarteDto suiviCommandeCarte(String idCarte) throws Exception {
        return iSuivicommandeCarte.suiviCommandeCarte(idCarte);
    }

    public List<PlafondsCarteDto> afficherPlafond(String cardNumber, String accountNumber) throws Exception {
        return iGetCardPlafonds.GetCardPlafonds(cardNumber, accountNumber);

    }

    public CarteDto blocageDeblocageCarte(String idCarte, String typeAction, String dateFin) throws Exception {
        return iBlocageDeblocageCarte.blocageDeblocageCarte(idCarte, typeAction, dateFin);
    }

    public CarteDto commandeCarte(CarteRequestDto carteRequestDto) throws Exception {
        return iCommandeCarte.commandeCarte(carteRequestDto);
    }

    public Map<String, Object> getTypesCarte(TypeCarteDto typeCarteDto) throws Exception {
        return iGetTypesCarte.getTypesCarte(typeCarteDto);
    }

    public List<CarteDto> getCardRequestList(UserDetailsDto userDetailsDto) throws Exception {
        return iClientCardRequest.clientCardsRequestList(userDetailsDto);
    }

    public String getEncoursCarte(MouvementCarteRecherchFormDto card) throws Exception {
        return encoursCarteClient.getGetEncoursCarte(card);
    }


    public CarteDto getInfoCarte(InfoCarteMiddleRequestDto infoCarteRequestDTO) throws Exception {
        return getInfoCarte.getInfoCarte(infoCarteRequestDTO);
    }

    public RecalculPinResponseDTO recalculPinCarte(DemandeRecalculPinDto demandeRecalculPinDto) throws Exception {
        return iRecalculePin.recalculePin(demandeRecalculPinDto);
    }

    public Map<String, Object> modifyPlafond(ModifyPlafondRequest modifyPlafondRequest) throws Exception {
        return modifyPlafondClient.modify(modifyPlafondRequest);
    }

    public EligibilityResponseDto checkEligibility(EligibilityRequestDTO eligibilityRequestDTO)throws Exception {
        return eligibilityPlafondClient.checkEligibility(eligibilityRequestDTO);
    }

    public Map<String, Object> rechargerCarte(DemandeRechargeCarteDto demandeRechargeCarteDto) throws Exception {
        return demandeRechargeCardClient.demandeRechargeCard(demandeRechargeCarteDto);
    }

    public Map<String, Object> activateUnsecurePaymentCard(AUPaymentRequest AUPaymentRequest) throws Exception {
        return activateUnsecurePaymentCardClient.activateUnsecurePaymentCard(AUPaymentRequest);
    }

    public Map<String, Object> getTransactionsAndEncoursCarte(MouvementCarteRecherchFormDto form, Boolean encooursCarte) throws Exception {
        Map<String, Object> map = new HashMap<>();
            List<MouvementCarteDto> movements = getCardMovements(form);
            map.put(Constante.SUCCESS, true);
            map.put(Constante.LIST, movements);
            if (encooursCarte) {
                map.put(Constante.ENCOURS_CARTE, getEncoursCarte(form));
            }
        return map;
    }
}