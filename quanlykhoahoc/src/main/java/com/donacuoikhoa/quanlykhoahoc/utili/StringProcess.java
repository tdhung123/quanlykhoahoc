package com.donacuoikhoa.quanlykhoahoc.utili;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class StringProcess {
    // public String autoGenerateCode(String strInput) {
    // String strResult = "", numPart = "", strPart = "";

    // Pattern numPattern = Pattern.compile("\\d+");
    // Matcher numMatcher = numPattern.matcher(strInput);
    // if (numMatcher.find()) {
    // numPart = numMatcher.group();
    // }

    // Pattern strPattern = Pattern.compile("\\D+");
    // Matcher strMatcher = strPattern.matcher(strInput);
    // if (strMatcher.find()) {
    // strPart = strMatcher.group();
    // }

    // int intPart = (Integer.parseInt(numPart) + 1);
    // int numPartLength = numPart.length();
    // int intPartLength = String.valueOf(intPart).length();

    // if (numPartLength > intPartLength) {
    // strPart = strPart.substring(0, strPart.length() - (numPartLength -
    // intPartLength));
    // }

    // strResult = strPart + intPart;
    // return strResult;
    // }
    // }
   public String autoGenerateCode(String strInput) {
        String strResult = "", numPart = "", strPart = "";

        Pattern numPattern = Pattern.compile("\\d+");
        Matcher numMatcher = numPattern.matcher(strInput);
        if (numMatcher.find()) {
            numPart = numMatcher.group();
        }

        Pattern strPattern = Pattern.compile("\\D+");
        Matcher strMatcher = strPattern.matcher(strInput);
        if (strMatcher.find()) {
            strPart = strMatcher.group();
        }

        if (numPart.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy chuỗi đầu vào.");
        }

        int intPart;
        try {
            intPart = Integer.parseInt(numPart) + 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Đầu vào của số không hợp lệ.", e);
        }

        strResult = strPart + intPart;
        return strResult;
    }
}