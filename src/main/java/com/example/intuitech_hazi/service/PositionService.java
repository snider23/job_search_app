package com.example.intuitech_hazi.service;

import com.example.intuitech_hazi.domain.Position;
import com.example.intuitech_hazi.repository.PositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PositionService {

    private PositionRepository positionRepository;

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
        return createdPosition.toString();
    }
}
