package com.example.intuitech_hazi.domain;

import com.example.intuitech_hazi.dto.incoming.PositionSaveCommand;
import jakarta.persistence.*;
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
    private String title;
    @Column(nullable = false)
    @Size(max = 50)
    private String location;

    @Column
    private String jobUrl;

    public Position(PositionSaveCommand positionSaveCommand) {
        this.title = (positionSaveCommand.getTitle() != null) ? positionSaveCommand.getTitle() : "";
        this.location = (positionSaveCommand.getLocation() != null) ? positionSaveCommand.getLocation() : "";
        this.jobUrl = "";
    }

    public Position() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }
}
