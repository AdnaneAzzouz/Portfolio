package fr.adnane.portfolio.controller;

import fr.adnane.portfolio.model.EmailForm;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
public class EmailController {

    private final JavaMailSender javaMailSender;


    @PostMapping(path = "/email")
    public Mono<ResponseEntity<EmailForm>> sendEmail(@RequestBody @Valid EmailForm emailForm) {
        String fromMail = "noreply.adnaneportfolio@gmail.com";
        String message = buildEmailBody(emailForm);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("[PORTFOLIO] - Someone wants to contact you!");
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setTo("adnaneazzouzpro@outlook.com");
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
        return Mono.just(ResponseEntity.ok().body(emailForm));
    }

    private String buildEmailBody(@Valid EmailForm emailForm) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Someone by the name of \"").append(emailForm.getFirstName()).append(" ").append(emailForm.getLastName()).append("\" has left you a message: \n\n");
        stringBuilder.append(emailForm.getMessage()).append("\n\n");
        stringBuilder.append("You can reach them by email at ").append(emailForm.getEmail());
        if(StringUtils.isNotBlank(emailForm.getPhoneNumber())) stringBuilder.append("Or by phone at ").append(emailForm.getPhoneNumber());
        if(StringUtils.isNotBlank(emailForm.getCompanyName())) stringBuilder.append("You can find their company by looking up their name: ").append(emailForm.getCompanyName());
        return stringBuilder.toString();
    }
}
