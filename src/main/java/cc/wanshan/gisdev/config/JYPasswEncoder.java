package cc.wanshan.gisdev.config;

import org.springframework.security.crypto.password.PasswordEncoder;

public class JYPasswEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        if (charSequence.toString().equals(s)){
                return true;
        }
        return false;
    }
}
