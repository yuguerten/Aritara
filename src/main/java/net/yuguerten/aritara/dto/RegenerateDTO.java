package net.yuguerten.aritara.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class RegenerateDTO {
    private String originalStory;
    private String feedback;
}
