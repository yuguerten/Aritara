package net.yuguerten.aritara.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StoryRequestDTO {
    private String plot;
    private String title;
    private String storyLength;
    private String narrativePerspective;
    private String genre;
    private String writingStyle;
    private String characterName;
    private String characterDescription;
    private String settingDescription;
    private String audience;
    private String feedback;
    private MultipartFile file;
}
