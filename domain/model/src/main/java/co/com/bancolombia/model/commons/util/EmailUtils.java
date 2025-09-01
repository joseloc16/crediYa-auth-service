package co.com.bancolombia.model.commons.util;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class EmailUtils {

    public String maskEmail(String email) {
        if (email == null) return null;
        int at = email.indexOf('@');
        if (at <= 1) return "***" + email.substring(Math.max(at, 0));
        return email.charAt(0) + "***" + email.substring(at);
    }

    public String normalizeEmail(String email) {
        return email == null ? null : email.strip().toLowerCase(Locale.ROOT);
    }
}

