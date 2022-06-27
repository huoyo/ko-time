package cn.langpy.kotime.util;

import java.util.*;

public class KoUtil {
    private final static String koTimeSecret = UUID.randomUUID().toString().replace("-", "");

    private final static List<Integer> choices = randomSecretIndexs();

    public static String login(String userName) {
        String token = encode(userName + "KOTO" + (System.currentTimeMillis() + (12 * 60 * 60 * 1000)));
        return token;
    }


    public static void checkLogin(String token) {
        decode(token);
    }

    public static boolean isLogin(String token) {
        try {
            decode(token);
            return true;
        } catch (Exception verificationException) {
            return false;
        }
    }


    private static List<Integer> randomSecretIndexs() {
        List<Integer> choices = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int intrandom = random.nextInt(20);
            if (choices.contains(intrandom)) {
                continue;
            }
            choices.add(intrandom);
        }
        return choices;
    }


    private static String encode(String text) {
        Base64.Encoder encoder = Base64.getEncoder();
        String encode = encoder.encodeToString(text.getBytes());
        int choicesSize = choices.size();
        for (int i = 0; i < choicesSize; i++) {
            Integer choice = choices.get(i);
            String pre = encode.substring(0, choice);
            String suf = encode.substring(choice);
            encode = pre + koTimeSecret.substring(i, i + 1) + suf;
        }
        return encode;
    }

    private static String decode(String token) {
        int tokenLength = token.length();
        int choicesSize = choices.size();
        for (int i = choicesSize - 1; i >= 0; i--) {
            Integer choice = choices.get(i);
            String pre = token.substring(0, choice);
            String suf = token.substring(choice + 1);
            String secretAt = koTimeSecret.substring(i, i + 1);
            if ((choice + 1) > tokenLength) {
                throw new InvalidAuthInfoException("error token!");
            }
            String tokenAt = token.substring(choice, choice + 1);
            if (!secretAt.equals(tokenAt)) {
                throw new InvalidAuthInfoException("error token!");
            }
            token = pre + suf;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(token);
        String decodeStr = new String(decode);
        String[] split = decodeStr.split("KOTO");
        Long expireTime = Long.valueOf(split[1]);
        if (expireTime < System.currentTimeMillis()) {
            throw new InvalidAuthInfoException("expired time!");
        }
        return decodeStr;
    }

}
