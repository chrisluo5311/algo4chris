package com.algo4chris.algo4chrisweb.controller.findveiw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class FindViewController {

    /**
     * Google帳號登入後導回首頁
     * 附帶用戶名、jwt token、refresh token
     *
     * */
    @GetMapping("/home")
    @PreAuthorize("hasRole('ROLE_MEMBER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public String home(@RequestParam String memberName,
                       @RequestParam(name="Authorization") String jwtToken,
                       @RequestParam String refreshToken,
                       Model model){
        model.addAttribute("memberName",memberName);
        model.addAttribute("jwtToken",jwtToken);
        model.addAttribute("refreshToken",refreshToken);
        return "index";
    }

    /**
     * 註冊頁面
     * */
    @GetMapping("/signup")
    public String signUp(){
        return "signup";
    }
}
