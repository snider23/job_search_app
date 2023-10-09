package com.example.intuitech_hazi.service;

import com.example.intuitech_hazi.domain.Position;
import com.example.intuitech_hazi.dto.outgoing.PositionListItem;
import com.example.intuitech_hazi.repository.PositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PositionService {

    private PositionRepository positionRepository;
    private ClientService clientService;
    private final String URL = "http://localhost:8080/positions/";

    public PositionService(PositionRepository positionRepository, ClientService clientService) {

        this.positionRepository = positionRepository;
        this.clientService=clientService;
    }

    public String createNewPosition(Position newPosition){
        if (newPosition.getTitle().length()>50){
            throw new IllegalArgumentException("The title of position is too long");
        } else if (newPosition.getLocation().length() >50) {
            throw new IllegalArgumentException("The location of position is too long");
        }
        Position createdPosition = positionRepository.save(newPosition);
        Long positionId = createdPosition.getId();
        String positionURL= String.format("%s%d",URL,positionId);
        createdPosition.setJobUrl(positionURL);
        return createdPosition.toString();
    }

    public List<Position> getPositions() {
        return positionRepository.findAll();
    }

    public Position getPositionById(Long id){return positionRepository.findPositionById(id);}

    public List<Position> getPositionsByTitleOrLocation(Position position){
        if (position.getTitle().length()>50){
            throw new IllegalArgumentException("The title of position is too long");
        } else if (position.getLocation().length() >50) {
            throw new IllegalArgumentException("The location of position is too long");
        }
        String jobTitle = "%" + position.getTitle() + "%";
        String jobLocation = "%" + position.getLocation() + "%";
        List<Position> positionList = positionRepository.findPositionsBy(jobTitle, jobLocation);
        List<Position> result= new ArrayList<>();
        result.addAll(positionList);
        return result;
    }

    public List<PositionListItem> getComplexPositions(@RequestBody Position position, @RequestHeader("apiKey") String apiKey) {

        if (!clientService.isApiKeyExists(apiKey)) {
            throw new IllegalArgumentException("api key not found");
        }

        return getPositionsByTitleOrLocation(position).stream().map(PositionListItem::new).collect(Collectors.toList());
    }

}
