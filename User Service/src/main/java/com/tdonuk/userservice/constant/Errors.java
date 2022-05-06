package com.tdonuk.userservice.constant;

public enum Errors {
    UNKNOWN_ERROR("Bilinmeyen bir hata oluştu."),

    USERNAME_CONFLICT("Verilen kullanıcı adı alınmıştır."),
    USERNAME_NOT_VALID("Verilen kullanıcı adı uygun değildir. Kullanıcı adınız 4-12 karakter arasında olmalı ve özel karakterler içermemelidir."),
    USERNAME_NOT_FOUND("Verilen kullanıcı adına sahip bir kullanıcı bulunamadı"),

    EMAIL_CONFLICT("Verilen e-posta adresine tanımlanmış bir hesap bulunmaktadır"),
    EMAIL_NOT_VALID("Verilen e-posta adresi uygun değildir"),
    EMAIL_NOT_FOUND("Verilen e-posta adresine tanımlı bir hesap bulunamadı"),

    PASSWORD_NOT_VALID("Verilen parola uygun değildir. Parolanız 4-16 karakter uzunluğunda olmalı ve özel karakterler içermemelidir.");

    String message;

    Errors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
