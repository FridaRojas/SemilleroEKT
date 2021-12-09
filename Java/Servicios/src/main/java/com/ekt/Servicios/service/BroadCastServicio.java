package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.BroadCast;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;

public interface BroadCastServicio {
    BroadCast save(BroadCast broadCast);
    public void notificacion2(String title, String asunto, String token);
}
