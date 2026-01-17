package com.hoctap.learningsupportapi.service.lichhoc;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpStore {

    private static class OtpData {
        String otp;
        long expireAt;
    }

    private final ConcurrentHashMap<String, OtpData> store = new ConcurrentHashMap<>();

    public void save(String email, String otp, int minutes) {
        OtpData data = new OtpData();
        data.otp = otp;
        data.expireAt = System.currentTimeMillis() + minutes * 60_000;
        store.put(email, data);
    }

    public boolean verify(String email, String otp) {
        OtpData data = store.get(email);
        if (data == null) return false;
        if (System.currentTimeMillis() > data.expireAt) {
            store.remove(email);
            return false;
        }
        return data.otp.equals(otp);
    }

    public void remove(String email) {
        store.remove(email);
    }
}

