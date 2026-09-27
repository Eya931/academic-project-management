package com.platform.project_managment.auth;

import com.platform.project_managment.email.EmailService;
import com.platform.project_managment.email.EmailTemplateName;
import com.platform.project_managment.group.Group;
import com.platform.project_managment.group.GroupRepository;
import com.platform.project_managment.pendingProfessor.PendingProfessorService;
import com.platform.project_managment.role.Role;
import com.platform.project_managment.role.RoleRepository;
import com.platform.project_managment.security.JwtService;
import com.platform.project_managment.user.Token;
import com.platform.project_managment.user.TokenRepository;
import com.platform.project_managment.user.User;
import com.platform.project_managment.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final GroupRepository groupRepository;
    private final PendingProfessorService pendingProfessorService;


    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Value("${app.first-admin.password}")
    private String firstAdminPassword;
    @Value("${application.mailing.frontend.activation-admin-url}")
    private String  activationAdminUrl;



    public void register(RegistrationRequest request) {
        if (!request.getEmail().endsWith("@enicar.ucar.tn")) {
            throw new IllegalArgumentException("L'email doit être institutionnel (@enicar.ucar.tn)");
        }

        var userRole = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new IllegalStateException("User role not initialized"));

        Group group = null;

       /* if (request.getRole().equals("professor")) {
            if (!pendingProfessorService.isPendingProfessor(request.getEmail())) {
                throw new RuntimeException("Aucun professeur avec cet email n'est enregistré par l'administration.");
            }
        }*/

        if (request.getRole().equals("student")) {
            group = groupRepository.findAll().stream()
                    .filter(g -> g.getPendingStudentEmails() != null && g.getPendingStudentEmails().contains(request.getEmail()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Aucun groupe ne contient cet email. Contactez l'administration."));
        }

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .groups(group != null ? List.of(group) : List.of()) // éviter NullPointerException pour les professeurs
                .build();

        if (group != null) {
            group.addStudent(user);
            group.getPendingStudentEmails().remove(request.getEmail());
            groupRepository.save(group);
        }

        userRepository.save(user);

        try {
            sendActivationEmail(user);
        } catch (MessagingException e) {
            log.error("Failed to send activation email to {}", user.getEmail(), e);
        }
    }


    private void sendActivationEmail(User user) throws MessagingException {
        // 1) generate & persist a single token
        String token = generateAndSaveActivationToken(user);

        // 2) send the email
        emailService.sendEmail(
                user.getEmail(),
                user.getName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                token,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String tokenValue = generateActivationCode(6);
        Token token = Token.builder()
                .token(tokenValue)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return tokenValue;
    }

    private String generateActivationCode(int length) {
        String digits = "0123456789";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(digits.charAt(rnd.nextInt(digits.length())));
        }
        return sb.toString();
    }

    public AuthenticationResponse Authenticate(@Valid AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("username", user.getName());
        claims.put("roles", user.getRoles().stream().map(Role::getName).toList());

        var jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }


    // @Transactional
    public void activateAccount(String token) throws MessagingException {

        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been genarated");
        }
        var user = userRepository.findByEmail(savedToken.getUser().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);

    }

    private void sendValidationEmail(User user) throws MessagingException {
        try {
            sendActivationEmail(user);
        } catch (MessagingException e) {
            log.error("Failed to resend activation email to {}", user.getEmail(), e);
            throw new RuntimeException("Failed to resend activation email");
        }
    }


    public void registerAdmin(RegistrationRequest request) {
        // Vérifie que l'email est institutionnel
        if (!request.getEmail().endsWith("@enicar.ucar.tn")) {
            throw new IllegalArgumentException("Email must be institutional.");
        }

        // Récupère le rôle ADMIN
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("ADMIN role not initialized"));

        // Crée un utilisateur avec le rôle ADMIN
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(firstAdminPassword)) // mot de passe temporaire
                .enabled(false)  // L'utilisateur est désactivé par défaut
                .accountLocked(false)
                .roles(List.of(adminRole))
                .build();

        // Sauvegarde l'utilisateur
        userRepository.save(user);


        try {
            sendActivationEmailToAdmin(user);
        } catch (MessagingException e) {
            log.error("Failed to send activation email to {}", user.getEmail(), e);
        }
    }

    private void sendActivationEmailToAdmin(User user) throws MessagingException {
        // 1) generate & persist a single token
        String token = generateAndSaveActivationToken(user);

        // 2) send the email
        emailService.sendEmail(
                user.getEmail(),
                user.getName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationAdminUrl,
                token,
                "Admin Account activation"
        );
    }


}
