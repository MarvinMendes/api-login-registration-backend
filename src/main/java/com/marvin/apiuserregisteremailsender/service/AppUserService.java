package com.marvin.apiuserregisteremailsender.service;

import com.marvin.apiuserregisteremailsender.domain.AppUser;
import com.marvin.apiuserregisteremailsender.domain.Token;
import com.marvin.apiuserregisteremailsender.domain.registration.RegistrationRequest;
import com.marvin.apiuserregisteremailsender.email.EmailSender;
import com.marvin.apiuserregisteremailsender.exceptions.*;
import com.marvin.apiuserregisteremailsender.repository.AppUserRepository;
import com.marvin.apiuserregisteremailsender.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final TokenRepository tokenRepository;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String USER_NAME_NOT_FOUND = "The user name was %s not found";

    public String save(RegistrationRequest request) throws UserAlreadyRegisterException, EmailFailedException {

        boolean userExist = appUserRepository.findAppUserByUserName(request.getEmail()).isPresent();
        if (userExist) {
            throw new UserAlreadyRegisterException(request.getEmail());
        }

        AppUser appUser = new AppUser();
        appUser.setFullName(String.format("%s %s", request.getFirstName(), request.getLastName()));
        appUser.setUserName(request.getEmail());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Token token = new Token();
        token.generateToken();
        token.setCreateAt(LocalDateTime.now());
        token.setExpiredAt(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(token);

        appUser.setToken(token);

        appUserRepository.save(appUser);

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token.getToken();
        emailSender.sendEmail(request.getEmail(), buildEmail(request.getFirstName(), link));

        return token.getToken();
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return appUserRepository.findAppUserByUserName(userName).orElseThrow
                (() -> new UsernameNotFoundException(String.format(USER_NAME_NOT_FOUND, userName))) ;
    }

    @Transactional
    public String confirmToken(String token) throws TokenNotFoundException, TokenExpiredException, UserWrongTokenException {
        Token tokenEntity = tokenRepository.findTokenByToken(token).orElseThrow(() -> new TokenNotFoundException(token));
        AppUser appUser = appUserRepository.findAppUserByToken(tokenEntity).orElseThrow(() -> new UserWrongTokenException(token));

        LocalDateTime createAt = appUser.getToken().getCreateAt();
        LocalDateTime expiredAt = appUser.getToken().getExpiredAt();

        Duration duration = Duration.between(createAt, expiredAt);

        //expiredAt  > *isBefor* LocalDateTime.now() -> expiredAt.isBefore(LocalDateTime.now()
        //duration.toMinutes() > 15
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(token);
        }
        appUser.setExpired(true);
        appUser.setEnabled(true);

        return "Confirmed!";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
