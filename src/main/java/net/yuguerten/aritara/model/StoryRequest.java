package net.yuguerten.aritara.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class StoryRequest {
    private String plot;
    private String storyLength;
    private String narrativePerspective;
    private String genre;
    private String characterName;
    private String characterDescription;
    private String settingDescription;
    private String audience;
}
