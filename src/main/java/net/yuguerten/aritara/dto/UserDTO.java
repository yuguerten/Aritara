package net.yuguerten.aritara.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
}
