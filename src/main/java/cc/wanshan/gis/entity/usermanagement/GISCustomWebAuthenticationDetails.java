package cc.wanshan.gis.entity.usermanagement;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
/**
 * @Author Li Cheng
 * @Description
 * @Date 11:02 2019/5/14
 * @Param
 * @return
 **/
public class GISCustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private static final long serialVersionUID = 1L;
    public GISCustomWebAuthenticationDetails(HttpServletRequest request) {

        super(request);
    }
}
