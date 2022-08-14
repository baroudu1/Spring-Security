package com.example.securityjwt.payloads;

import lombok.*;

@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class ResponseAuthWithOutJWT {
    private String message = "";
}
