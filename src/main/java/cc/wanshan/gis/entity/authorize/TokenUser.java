package cc.wanshan.gis.entity.authorize;

import lombok.Data;

import java.io.Serializable;

/**
 * @author renmaoyan
 * @date 2019/8/1
 * @description TODO
 */
@Data
public class TokenUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String role;
}
