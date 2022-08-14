package com.example.securityjwt.payloads;

import lombok.*;

@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class ResponseAuthentication {
    private String message = "";
    private String accessToken = null;
//    private String refreshToken = null;

    public ResponseAuthentication(String message) {
        this.message = message;
    }


}
