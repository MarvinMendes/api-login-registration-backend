package com.marvin.apiuserregisteremailsender.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @SequenceGenerator(name = "token_sequence", sequenceName = "token_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_sequence")
    private Long id;

    private String token;

    private LocalDateTime createAt;

    private LocalDateTime expiredAt;

    public String generateToken() {
        return this.token = UUID.randomUUID().toString();
    }

}
