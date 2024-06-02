package com.greengrim.green.common.webview;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebviewController {

    @GetMapping("/privacy-policy")
    public String getPrivacyPolicy() {
        return "privacy-policy-page";
    }

    @GetMapping("/terms-of-service")
    public String getTermsOfService() {
        return "terms-of-service";
    }

}
