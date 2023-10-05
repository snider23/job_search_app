package com.example.intuitech_hazi.controller;

import com.example.intuitech_hazi.domain.Position;
import com.example.intuitech_hazi.dto.incoming.PositionSaveCommand;
import com.example.intuitech_hazi.dto.outgoing.PositionListItem;
import com.example.intuitech_hazi.service.ClientService;
import com.example.intuitech_hazi.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/positions")
@Validated
public class PositionController {

   private PositionService positionService;
   private ClientService clientService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping    //TODO nem hozza letre postmannel megnezni
    public ResponseEntity<String> createNewJob(@RequestBody @Valid Position newPosition,
                                                         @RequestParam UUID apiKey){
        if (!clientService.isApiKeyExists(apiKey)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }else {
            positionService.createNewPosition(newPosition);
            Long positionId = newPosition.getId();
            String positionUrl = "/positions/" + positionId;

            return ResponseEntity.status(HttpStatus.CREATED).body(positionUrl);
        }
    }
}
