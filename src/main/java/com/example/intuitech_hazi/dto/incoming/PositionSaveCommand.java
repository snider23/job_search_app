package com.example.intuitech_hazi.dto.incoming;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PositionSaveCommand {
    @NotEmpty
    @Size(max = 50)
    private String title;
    @NotEmpty
    @Size(max = 50)
    private String location;

    //TODO ide szerintem nem kell konstruktor? kesobb megnezni

}
