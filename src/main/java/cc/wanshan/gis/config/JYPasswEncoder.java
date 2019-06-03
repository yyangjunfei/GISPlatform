package cc.wanshan.gis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * @Author Li Cheng
 * @Date 8:40 2019/6/3
 **/
public class JYPasswEncoder implements PasswordEncoder {
    private static final Logger logger= LoggerFactory.getLogger(JYPasswEncoder.class);
    @Override
    public String encode(CharSequence charSequence) {
        logger.info("encode::charSequence = [{}]",charSequence);
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        logger.info("matches::charSequence = [{}], s = [{}]",charSequence, s);
        if (charSequence.toString().equals(s)){
                return true;
        }
        logger.error("",charSequence, s);
        return false;
    }
}
