package uol.compass.cspcapi.application.api.auth.dto;

public class AuthenticatedResponseDTO {
    private String token;

    public AuthenticatedResponseDTO(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }
}
