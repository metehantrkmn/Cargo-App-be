package com.mete;

import com.mete.cargo.CargoService;
import com.mete.cargo.NoSuchPersonExistsException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RestService {

    private final RestTemplate restTemplate;

    public record IdentityResult(
            boolean isExists
    ){}
    public boolean isPersonExists(String apiUrl,String identityNumber){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> mymap = Map.of("identityNumber",identityNumber);
        HttpEntity<?> httpEntity = new HttpEntity<>(mymap, headers);

        // Perform the GET request with headers
        ResponseEntity<IdentityResult> restResponse = restTemplate.postForEntity(
                apiUrl,
                httpEntity,
                IdentityResult.class
        );
        System.out.println(restResponse);
        return restResponse.getBody().isExists;
    }

    public record IdentityRecord(
            String identity,
            String name,
            String surname,
            String email
    ){}
    public IdentityRecord getUserInfoFromIdentityService(String identityNo){

        String apiUrl = "http://identityapp:8080/IdentityService/v1/get-user";
        // Create request parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("identityNo", identityNo);

        // Create HTTP headers
        HttpHeaders headers = new HttpHeaders();
        // Optionally, set headers if needed
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HttpEntity with headers
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make GET request with parameters and retrieve response
        ResponseEntity<IdentityRecord> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                IdentityRecord.class
        );
        if(!response.hasBody())
            throw new NoSuchPersonExistsException("no such person exist in identity service");

        return response.getBody();
    }

    public IdentityRecord getUserByNameSurname(String name, String surname){
        String apiUrl = "http://identityapp:8080/IdentityService/v1/get-user-byNameSurname";
        // Create request parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("name", name)
                .queryParam("surname",surname);

        // Create HTTP headers
        HttpHeaders headers = new HttpHeaders();
        // Optionally, set headers if needed
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HttpEntity with headers
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make GET request with parameters and retrieve response
        ResponseEntity<IdentityRecord> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                IdentityRecord.class
        );
        if(!response.hasBody())
            throw new NoSuchPersonExistsException("no such person exist in identity service");

        return response.getBody();
    }

    public record MailingRecord(String routingKey, String email,String name, String pnrNo, Integer templateNumber){}
    public void sendRequestToMailingService(MailingRecord mailingRecord){
        String apiUrl = "http://mqapplication:8080/api/MQ/mailing";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(mailingRecord, headers);

        // Perform the GET request with headers
        ResponseEntity<String> restResponse = restTemplate.postForEntity(
                apiUrl,
                httpEntity,
                String.class
        );
        System.out.println(restResponse.getBody());
    }


    public record FirebaseRecord(
            String routingKey,
            String subject,
            String content,
            Map<String,String> data
    ){}
    public void sendRequestToFirebaseService(FirebaseRecord firebaseRecord){
        System.out.println("firebase rest template function start");
        String apiUrl = "http://mqapplication:8080/api/MQ/notification";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(firebaseRecord, headers);

        // Perform the GET request with headers
        ResponseEntity<String> restResponse = restTemplate.postForEntity(
                apiUrl,
                httpEntity,
                String.class
        );
        System.out.println(restResponse.getBody());
    }

}
