package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.BroadCast;
import com.ekt.Servicios.repository.BroadCastRepositorio;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public class BroadCastServicioImpl implements BroadCastServicio {

    @Autowired
    private BroadCastRepositorio broadCastRepositorio;

    @Override
    @Transactional
    public BroadCast save(BroadCast broadCast) {
        return broadCastRepositorio.save(broadCast) ;
    }

    @Override
    public void notificacion2(String title, String asunto, String token){
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\n    \"to\": \""+ token +"\",\n    " +
                "\"notification\": {\n        " +
                "\"body\": \""+ asunto +"\",\n        " +
                "\"title\": \""+ title +"\"\n    }\n}");

        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .method("POST", body)
                .addHeader("Authorization", "key=AAAAOMDADOM:APA91bF39PZzaPSPbFgPbEO6KvjsOD-AtfnpwEgNGZ6lMFQyx4xaswBX6HDe3iQfjAPiP5MR32Onws1Ry5diSbVY_PwRBhZLQ0PGJzPFLUk14xR8ELQVyleVG2_z00wdWBqs1inATbLP")
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
