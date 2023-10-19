package com.example.jobsearch.service;

import com.example.jobsearch.domain.Position;
import com.example.jobsearch.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PositionServiceTest {

    private PositionService positionService;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        positionService = new PositionService(positionRepository, clientService);
    }

    @Test
    public void testCreateNewPosition_ValidPosition() {
        Position newPosition = new Position();
        newPosition.setTitle("Software Developer");
        newPosition.setLocation("New York");

        when(positionRepository.save(any(Position.class))).thenReturn(newPosition);

        String result = positionService.createNewPosition(newPosition);

        assertNotNull(result);
        assertTrue(result.contains("http://localhost:8080/positions/"));
    }

    @Test
    public void testGetPositions() {
        List<Position> positions = new ArrayList<>();
        positions.add(new Position());
        positions.add(new Position());

        when(positionRepository.findAll()).thenReturn(positions);

        List<Position> result = positionService.getPositions();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetPositionsByTitleOrLocation() {
        Position position = new Position();
        position.setTitle("Software Engineer");
        position.setLocation("london");

        List<Position> positions = new ArrayList<>();
        positions.add(new Position());
        positions.add(new Position());

        when(positionRepository.findPositionsBy("%Software Engineer%", "%london%")).thenReturn(positions);

        List<Position> result = positionService.getPositionsByTitleOrLocation(position);

        assertEquals(positions.size(), result.size());

    }


}
