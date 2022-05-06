package com.tdonuk.userservice.util;

import com.tdonuk.userservice.model.Name;
import com.tdonuk.userservice.model.entity.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Pattern;

public class UserValidator {
    private UserValidator() {}

    public static void validate(final UserEntity entity) throws Exception{
        validatePassword(entity.getPassword());
        validateUsername(entity.getUsername());
        validateEmail(entity.getEmail());
        validateName(entity.getName());
    }

    private static void validatePassword(final String password) throws Exception{
        if(password.length() >= 4 && password.length() <= 16) {
            if(! includesSpecialChars(password)) {
                return;
            } else {
                throw new Exception("Şifre özel karakterler içermektedir.");
            }
        } else {
            throw new Exception("Şifre 4-16 karakter uzunluğunda olmalıdır.");
        }
    }

    private static void validateUsername(final String username) throws Exception{
        if(username.length() > 3 && username.length() < 13) {
            if(! includesSpecialChars(username)) {
                return;
            } else {
                throw new Exception("Kullanıcı adı özel karakterler içermektedir.");
            }
        } else {
            throw new Exception("kullanıcı adı 4-12 karakter uzunluğunda olmalıdır.");
        }
    }

    private static void validateEmail(final String email) throws Exception{
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if(Pattern.matches(regex, email) && !includesSpecialChars(email)) {
            return;
        } else {
            throw new Exception("Lütfen doğru bir email adresi giriniz.");
        }
    }

    private static void validateName(final Name name) throws Exception{
        if(name.getFirstname().length() > 2 && name.getLastname().length() > 2 && !Pattern.matches("^[.^,:;?=/\\-&%+'$#£><]$", name.toString())) {
            return;
        } else {
            throw new Exception("Lütfen doğru bir isim giriniz");
        }
    }

    private static boolean includesSpecialChars(String s) {
        if(Pattern.matches("^[a-zA-Z0-9_.#/=&$,@-]*$", s)) { // there are no special characters
            return false;
        } else {
            return true;
        }
    }
}
