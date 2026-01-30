package com.erica.fintech.MicroMpesaplusAirtimePlatform.service;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.User;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegisterService(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String firstName, String middleName, String surname,  String nationalId, String phone, String password) throws Exception {

        if (userRepo.findByPhone(phone).isPresent()) {
            throw new Exception("Phone number already registered");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setSurname(surname);
        user.setNationalId(nationalId);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        String fullName = firstName + (middleName != null && !middleName.isBlank() ? " " + middleName : "") + " " + surname;
        user.setFullName(fullName.trim());

        userRepo.save(user);
    }

}
