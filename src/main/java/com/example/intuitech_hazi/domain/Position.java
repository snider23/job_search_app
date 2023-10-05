package com.example.intuitech_hazi.domain;

import com.example.intuitech_hazi.dto.incoming.PositionSaveCommand;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    @Size(max = 50)
    private String name;
    @Column(nullable = false)
    @Size(max = 50)
    private String location;

    public Position(PositionSaveCommand positionSaveCommand) {
    this.name= positionSaveCommand.getTitle();
    this.location= positionSaveCommand.getLocation();
    }
}
