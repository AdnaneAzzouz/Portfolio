package fr.adnane.portfolio.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class EmailForm {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String email;
    private String phoneNumber;
    private String companyName;
    @NonNull
    private String message;
}
