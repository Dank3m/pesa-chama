package com.kinduberre.chama.util;

import jakarta.servlet.http.HttpServletRequest;

public class PasswordResetUtil {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}