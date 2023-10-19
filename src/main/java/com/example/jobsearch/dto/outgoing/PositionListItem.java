package com.example.jobsearch.dto.outgoing;

import com.example.jobsearch.domain.Position;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PositionListItem {
    private String jobTitle;
    private String locationName;
    private String jobUrl;

    public PositionListItem(String title, String location, String url) {
        this.jobTitle = title;
        this.locationName = location;
        this.jobUrl = url;
    }

    public PositionListItem(Position position) {
        this.jobTitle= position.getTitle();
        this.locationName= position.getLocation();
        this.jobUrl= position.getJobUrl();
    }
}
