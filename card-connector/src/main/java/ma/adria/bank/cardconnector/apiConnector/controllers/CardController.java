package ma.adria.bank.cardconnector.apiConnector.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ma.adria.bank.cardconnector.apiConnector.exception.InvalidRequestException;
import ma.adria.bank.cardconnector.apiConnector.response.ApiResponseBuilder;
import ma.adria.bank.cardconnector.apiConnector.response.ResponseWrapper;
import ma.adria.bank.cardconnector.apiConnector.services.CardService;
import ma.adria.bank.dto.*;
import ma.adria.bank.dto.monetique.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cards")
@Tag(name = "Cartes", description = "Opérations sur les cartes bancaires")
@Slf4j
@Validated
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Liste des cartes d'un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/list")
    public ResponseEntity<ResponseWrapper<List<CarteDto>>> listCartes(
            @RequestParam("radical") String radical,
            @AuthenticationPrincipal UserDetailsDto userDetailsDto) throws Exception {

        validateRadical(radical, userDetailsDto);
        List<CarteDto> cartes = cardService.getCardList(radical);
        return ApiResponseBuilder.buildSuccessResponse("Liste des cartes récupérée", cartes);
    }

    @Operation(summary = "Opposer une carte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/oppose")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> opposerCarte(
            @Valid @RequestBody DemandeOppositionCarteDto demande) throws Exception {
        if (StringUtils.isBlank(demande.getNumeroCarte())) {
            throw new InvalidRequestException("Numéro de carte est obligatoire.");
        }
        Map<String, Object> result = cardService.opposerCarte(demande.getNumeroCarte());
        return ApiResponseBuilder.buildSuccessResponse("Carte opposée avec succès", result);
    }

    @Operation(summary = "Lister les demandes d'opposition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/opposition/list")
    public ResponseEntity<ResponseWrapper<List<DemandeOppositionCarteDto>>> getListDemandeOppositionCarte(
            @Valid @RequestBody FormObjectRechercheDto form,
            @RequestParam("idClient") String idClient,
            @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        validateRadical(idClient, userDetailsDto);
        List<DemandeOppositionCarteDto> list = Collections.singletonList(new DemandeOppositionCarteDto());
        return ApiResponseBuilder.buildSuccessResponse("Liste des demandes d'opposition récupérée", list);
    }

    @Operation(summary = "Afficher les transactions d'une carte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/transactions")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> transactionsOfCarte(
            @Valid @RequestBody MouvementCarteRecherchFormDto form,
            @RequestParam(value = "encooursCarte", required = false) Boolean encooursCarte) throws Exception {

        if (StringUtils.isBlank(form.getReference())) {
            throw new InvalidRequestException("Référence carte est requise.");
        }

        Map<String, Object> result = cardService.getTransactionsAndEncoursCarte(form, encooursCarte);
        return ApiResponseBuilder.buildSuccessResponse("Transactions récupérées", result);
    }

    @Operation(summary = "Suivi de commande d'une carte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/suivi-commande")
    public ResponseEntity<ResponseWrapper<CarteDto>> suiviCommandeCarte(@RequestParam("idCarte") String idCarte) throws Exception {
        if (StringUtils.isBlank(idCarte)) {
            throw new InvalidRequestException("Identifiant carte est requis.");
        }
        CarteDto carte = cardService.suiviCommandeCarte(idCarte);
        return ApiResponseBuilder.buildSuccessResponse("Commande carte récupérée", carte);
    }

    @Operation(summary = "Afficher les plafonds d'une carte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/plafonds")
    public ResponseEntity<ResponseWrapper<List<PlafondsCarteDto>>> afficherPlafond(
            @RequestParam("numeroCarte") String numeroCarte,
            @RequestParam(value = "numeroCompte", required = false) String numeroCompte) throws Exception {

        if (StringUtils.isBlank(numeroCarte)) {
            throw new InvalidRequestException("Le numéro de carte est obligatoire.");
        }
        List<PlafondsCarteDto> plafonds = cardService.afficherPlafond(numeroCarte, numeroCompte);
        return ApiResponseBuilder.buildSuccessResponse("Plafonds carte récupérés", plafonds);
    }

    @Operation(summary = "Bloquer ou débloquer une carte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/blocage-deblocage")
    public ResponseEntity<ResponseWrapper<CarteDto>> blocageDeblocageCarte(
            @RequestParam("idCarte") String idCarte,
            @RequestParam("typeAction") String typeAction,
            @RequestParam(value = "dateFin", required = false) String dateFin) throws Exception {

        if (StringUtils.isBlank(idCarte) || StringUtils.isBlank(typeAction)) {
            throw new InvalidRequestException("idCarte et typeAction sont obligatoires.");
        }
        CarteDto carte = cardService.blocageDeblocageCarte(idCarte, typeAction, dateFin);
        return ApiResponseBuilder.buildSuccessResponse("Blocage/déblocage effectué", carte);
    }

    @Operation(summary = "Commander une carte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/commande")
    public ResponseEntity<ResponseWrapper<CarteDto>> commandeCarte(
            @Valid @RequestBody CarteRequestDto carteRequestDto) throws Exception {
        CarteDto carte = cardService.commandeCarte(carteRequestDto);
        return ApiResponseBuilder.buildSuccessResponse("Carte commandée avec succès", carte);
    }

    @Operation(summary = "Lister les types de carte disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/types")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> getTypesCarte(
            @Valid @RequestBody TypeCarteDto typeCarteDto) throws Exception {
        Map<String, Object> types = cardService.getTypesCarte(typeCarteDto);
        return ApiResponseBuilder.buildSuccessResponse("Types de carte récupérés", types);
    }

    @Operation(summary = "Lister les demandes de cartes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/request/list")
    public ResponseEntity<ResponseWrapper<List<CarteDto>>> demandesCarte(
            @AuthenticationPrincipal UserDetailsDto userDetailsDto) throws Exception {
        List<CarteDto> list = cardService.getCardRequestList(userDetailsDto);
        return ApiResponseBuilder.buildSuccessResponse("Demandes de cartes récupérées", list);
    }

    @Operation(summary = "Obtenir les détails d'une carte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/details")
    public ResponseEntity<ResponseWrapper<CarteDto>> detailsOfCarte(
            @Valid @RequestBody InfoCarteMiddleRequestDto request) throws Exception {
        CarteDto details = cardService.getInfoCarte(request);
        return ApiResponseBuilder.buildSuccessResponse("Détails de carte récupérés", details);
    }

    @Operation(summary = "Recalculer le code PIN de la carte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/recalcul-pin")
    public ResponseEntity<ResponseWrapper<RecalculPinResponseDTO>> recalculPinCarte(
            @Valid @RequestBody DemandeRecalculPinDto demandeRecalculPinDto) throws Exception {
        RecalculPinResponseDTO responseDTO = cardService.recalculPinCarte(demandeRecalculPinDto);
        String message = (responseDTO.getErrorCode() == 0) ? "PIN recalculé avec succès" : "Erreur lors du recalcul du PIN";
        boolean success = responseDTO.getErrorCode() == 0;
        return ApiResponseBuilder.buildResponse(success, message, responseDTO);
    }

    @Operation(summary = "Modifier le plafond de la carte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/modify-plafond")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> modifyPlafond(
            @Valid @RequestBody ModifyPlafondRequest modifyPlafondRequest) throws Exception {
        Map<String, Object> result = cardService.modifyPlafond(modifyPlafondRequest);
        return ApiResponseBuilder.buildSuccessResponse("Plafond modifié avec succès", result);
    }

    @Operation(summary = "Vérifier l'éligibilité de modification de plafond")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/eligibility")
    public ResponseEntity<ResponseWrapper<EligibilityResponseDto>> checkEligibility(
            @Valid @RequestBody EligibilityRequestDTO eligibilityRequestDTO) throws Exception {
        EligibilityResponseDto response = cardService.checkEligibility(eligibilityRequestDTO);
        return ApiResponseBuilder.buildSuccessResponse("Éligibilité vérifiée avec succès", response);
    }

    @Operation(summary = "Recharger une carte bancaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/recharge")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> rechargerCarte(
            @Valid @RequestBody DemandeRechargeCarteDto demandeRechargeCarteDto) throws Exception {
        Map<String, Object> result = cardService.rechargerCarte(demandeRechargeCarteDto);
        return ApiResponseBuilder.buildSuccessResponse("Carte rechargée avec succès", result);
    }

    @Operation(summary = "Activer le paiement non sécurisé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes"),
            @ApiResponse(responseCode = "500", description = "Erreur technique interne")
    })
    @PostMapping("/activate-unsecure-payment")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> activateUnsecurePayment(
            @Valid @RequestBody AUPaymentRequest auPaymentRequest) throws Exception {
        Map<String, Object> result = cardService.activateUnsecurePaymentCard(auPaymentRequest);
        return ApiResponseBuilder.buildSuccessResponse("Paiement non sécurisé activé", result);
    }

    private void validateRadical(String radical, UserDetailsDto userDetailsDto) {
        if (StringUtils.isBlank(radical) || !radical.equals(userDetailsDto.getIdentifiantRC())) {
            log.warn("Validation échouée : Radical {} ne correspond pas à l'utilisateur {}",
                    radical, userDetailsDto.getUsername());
            throw new InvalidRequestException("Radical invalide.");
        }
    }

}
