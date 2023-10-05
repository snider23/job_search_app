package com.example.intuitech_hazi.dto.outgoing;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PositionListItem {
    private Long id;
    private String title;
    private String location;
    private String url;

    public PositionListItem(Long id, String title, String location, String url) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.url = url;
    }
}
