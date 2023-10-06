package com.example.intuitech_hazi.controller;

import com.example.intuitech_hazi.domain.Position;
import com.example.intuitech_hazi.dto.outgoing.PositionListItem;
import com.example.intuitech_hazi.service.ClientService;
import com.example.intuitech_hazi.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/positions")
@Validated
public class PositionController {

    private final String URL= "http://localhost:8080/positions/";

   private PositionService positionService;
   private ClientService clientService;

    @Autowired
    public PositionController(PositionService positionService, ClientService clientService) {
        this.positionService = positionService;
        this.clientService = clientService;
    }

    @PostMapping    //TODO nem hozza letre postmannel megnezni, headerken add at az apikeyt
    public ResponseEntity<String> createNewJob(@RequestBody Position newPosition,
                                               @RequestHeader("apiKey") String apiKey){
        if (!clientService.isApiKeyExists(apiKey)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Hibás API kulcs");
        }else {

            positionService.createNewPosition(newPosition);
//            newPosition.setTitle(newPosition.getTitle());
//            newPosition.setLocation(newPosition.getLocation());
            Long positionId = newPosition.getId();
            String positionUrl = URL + positionId;

            return ResponseEntity.status(HttpStatus.CREATED).body(positionUrl);
        }
    }

    @GetMapping
    public List<Position> getPositions(){
        return positionService.getPositions();
    }

    @GetMapping("/{id}")
    public Position getPositions(@PathVariable Long id){
        return positionService.getPositionById(id);
    }
}
