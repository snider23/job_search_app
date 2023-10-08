package com.example.intuitech_hazi.service;

import com.example.intuitech_hazi.domain.Position;
import com.example.intuitech_hazi.dto.outgoing.PositionListItem;
import com.example.intuitech_hazi.repository.PositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PositionService {

    private PositionRepository positionRepository;
    private final String URL = "http://localhost:8080/positions/";

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
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
        String jobTitle = "%" + position.getTitle() + "%";
        String jobLocation = "%" + position.getLocation() + "%";
        List<Position> positionList = positionRepository.findPositionsBy(jobTitle, jobLocation);
        List<Position> result= new ArrayList<>();
        result.addAll(positionList);
        return result;
    }


}
