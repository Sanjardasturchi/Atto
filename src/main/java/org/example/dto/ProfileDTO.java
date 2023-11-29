package org.example.dto;

import lombok.*;
import org.example.enums.ProfileRole;
import org.example.enums.Status;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProfileDTO {
    private String name; // <1>
    private String surname; // <2>
    private String phone; //unique  <3>
    private String password; // <4>
    private LocalDateTime created_date; // <5>
    private Status status; // <6>
    private ProfileRole profileRole; // <7>
}
