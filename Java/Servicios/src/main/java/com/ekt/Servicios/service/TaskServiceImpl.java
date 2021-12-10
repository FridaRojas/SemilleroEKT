package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.ResponseTask;
import com.ekt.Servicios.entity.Task;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.TaskRepository;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository tareaRepository;

    @Override
    public Task save(Task tarea) {
        return tareaRepository.save(tarea);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Task> findById(String id){ return tareaRepository.findById(id); }

    @Override
    public Iterable<Task> findAll() {
        return tareaRepository.findAll();
    }

    @Autowired
    private UserServiceImpl usuarioService;

    @Override
    public void  deleteById(String id){
        Optional<Task> tareaOptionals = tareaRepository.findById(id);
        if (tareaOptionals.isPresent()){
            Task tareaUpdate = tareaOptionals.get();
            tareaUpdate.setEstatus("Cancelado");

            tareaRepository.save(tareaUpdate);
        }
    }

    @Override
    public void updateById(String id, Task tarea) {
        Optional<Task> tareaOptional = tareaRepository.findById(id);
        if (tareaOptional.isPresent()){
            Task tareaUpdate = tareaOptional.get();

            tareaUpdate.setDescripcion(tarea.getDescripcion());
            tareaUpdate.setFecha_ini(tarea.getFecha_ini());
            tareaUpdate.setFecha_fin(tarea.getFecha_fin());
            tareaUpdate.setTitulo(tarea.getTitulo());
            tareaUpdate.setPrioridad(tarea.getPrioridad());
            tareaUpdate.setEstatus(tarea.getEstatus());
            tareaUpdate.setLeido(false);
            tareaUpdate.setObservaciones(tarea.getObservaciones());

            tareaRepository.save(tareaUpdate);
        }
    }
    @Override
    public void actualizarEstatus(String id_tarea, String estatus){
        Optional<Task> tareaOptionals = tareaRepository.findById(id_tarea);
        if (tareaOptionals.isPresent()){
            Task updateEstatus = tareaOptionals.get();
            updateEstatus.setEstatus(estatus);
            tareaRepository.save(updateEstatus);
        }
    }
    @Override
    public void actualizaLeido(String id_tarea, Boolean leido){
        LocalDateTime date =  LocalDateTime.now();
        Optional<Task> tareaOptional = tareaRepository.findById(id_tarea);
        /*if (tareaOptional.isPresent()){
            LocalDateTime ldt = date
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            Task tareaUpdate = tareaOptional.get();
            tareaUpdate.setFechaLeido(ldt);
            tareaUpdate.setLeido(leido);
            tareaRepository.save(tareaUpdate);
        }*/
    }
    @Override
    public void updateRealDateStart(String id_tarea, Task tarea){
        Optional<Task> tareaOptional = tareaRepository.findById(id_tarea);
        if(tareaOptional.isPresent()){
            if(tarea.getFecha_iniR()!=null) {
                Task tareaUpdate = tareaOptional.get();
                tareaUpdate.setFecha_iniR(tarea.getFecha_iniR());
                tareaRepository.save(tareaUpdate);
            }else{
                //VALIDAR
            }
        }
    }

    @Override
    public void updateRealDateFinish(String id_tarea, Task tarea){
        Optional<Task> tareaOptional = tareaRepository.findById(id_tarea);
        if(tareaOptional.isPresent()){
            if(tarea.getFecha_finR()!=null){
                Task tareaUpdate = tareaOptional.get();
                tareaUpdate.setFecha_finR(tarea.getFecha_finR());
                tareaRepository.save(tareaUpdate);
            }
        }
    }

    @Override
    public ArrayList<String> validarTareasCrear(Task tarea) {
        String idgrupo = tarea.getId_grupo();
        String idEmisor = tarea.getId_emisor();
        String nombreEmisor = tarea.getNombre_emisor();
        String idReceptor = tarea.getId_receptor();
        String nombreReceptor = tarea.getNombre_receptor();
        Date fechaInicial = tarea.getFecha_ini();
        Date fechaFinal = tarea.getFecha_fin();
        String titulo = tarea.getTitulo();
        String descripcion = tarea.getDescripcion();
        String prioridad = tarea.getPrioridad();
        String estatus = tarea.getEstatus();
        String fechaI = fechaInicial.toString();
        String fechaF = fechaFinal.toString();

        boolean nombreE = Pattern.matches("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]*$", nombreEmisor);
        boolean nombreR = Pattern.matches("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]*$", nombreReceptor);
        boolean tituloT = Pattern.matches("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d10-9\\s]*$", titulo);
        boolean descripcionT = Pattern.matches("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d10-9\\s]*$", descripcion);
        boolean estatusT = Pattern.matches("^[a-zA-Z]*$", estatus);
        boolean prioridadT = Pattern.matches("^[a-zA-Z]*$", prioridad);

        ArrayList<String> errores = new ArrayList<>();
        if (!nombreE) errores.add("nombreEmisor");
        if (!nombreR) errores.add("nombreReceptor");
        if (!tituloT) errores.add("titulo");
        if (!descripcionT) errores.add("descripcion");
        if (!estatusT) errores.add("estatus");
        if (!prioridadT) errores.add("prioridad");

        return errores;
    }

    @Override
    public void notificacion(String token, String asunto) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\n    \"to\": \""+token+"\",\n    " +
                "\"notification\": {\n        " +
                "\"body\": \""+ asunto +"\",\n        " +
                "\"title\": \"Tienes una tarea nueva\"\n    }\n}");

        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .method("POST", body)
                .addHeader("Authorization", "key=AAAAOMDADOM:APA91bF39PZzaPSPbFgPbEO6KvjsOD-AtfnpwEgNGZ6lMFQyx4xaswBX6HDe3iQfjAPiP5MR32Onws1Ry5diSbVY_PwRBhZLQ0PGJzPFLUk14xR8ELQVyleVG2_z00wdWBqs1inATbLP")
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public ArrayList<String> validarTareasActualizar(Task tarea) {

        Date fechaInicial = tarea.getFecha_ini();
        Date fechaFinal = tarea.getFecha_fin();
        String titulo = tarea.getTitulo();
        String descripcion = tarea.getDescripcion();
        String prioridad = tarea.getPrioridad();
        String estatus = tarea.getEstatus();
        String observaciones = tarea.getObservaciones();
        boolean observacionesA;
        if(observaciones==null||observaciones.equals(""))
            observacionesA=true;
        else
            observacionesA = Pattern.matches("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d10-9\\s]*$", observaciones);
        boolean prioridadA = Pattern.matches("^[a-zA-Z]*$", prioridad);
        boolean tituloA = Pattern.matches("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d10-9\\s]*$", titulo);
        boolean descripcionA = Pattern.matches("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d10-9\\s]*$", descripcion);
        boolean estatusA = Pattern.matches("^[a-zA-Z]*$", estatus);
        ArrayList<String> erroresActulizar = new ArrayList<>();
        if (!observacionesA) erroresActulizar.add("observaciones");
        if (!prioridadA) erroresActulizar.add("prioridad");
        if (!tituloA) erroresActulizar.add("titulo");
        if (!descripcionA) erroresActulizar.add("descripcion");
        if (!estatusA) erroresActulizar.add("estatus");
        return erroresActulizar;
    }
    @Override
    public ArrayList<String> validarSesion(String token_sesion, String id_usuario){
        ArrayList<String> data = new ArrayList<>();
        Optional<User> usuarioValido = usuarioService.findById(id_usuario);
        System.out.println("token_Davis: "+token_sesion);
        String token_valido;// buscar en BD
        if(usuarioValido.isPresent()) {
            System.out.println("token_BD: "+usuarioValido.get().getTokenAuth());
            token_valido = usuarioValido.get().getTokenAuth();
            if (token_valido == null || token_valido == "")
                data.add("No existe el token en la BD");
            if (!token_sesion.equals(token_valido))
                data.add("Token de sesión invalido");
        }else{
                data.add("Usuario invalido");
        }
        return data;
    }

    @Override
    public Iterable<Task> findByPriority(String prioridad) {
        return tareaRepository.findByPriority(prioridad);
    }

    @Override
    public Iterable<Task> findByIdGrupo(String id_grupo) {
        return tareaRepository.findByIdGrupo(id_grupo);
    }

    @Override
    public Iterable<Task> findIdReceptorTareaByPrioridad(String id_usuario, String prioridad) {
        return tareaRepository.findIdReceptorTareaByPrioridad(id_usuario, prioridad);
    }

    @Override
    public Iterable<Task> getAllOutByUserId(String id_usuario) {
        return tareaRepository.getAllOutByUserId(id_usuario);
    }

    @Override
    public Iterable<Task> getAllInByUserId(String id_receptor) {
        return tareaRepository.getAllInByUserId(id_receptor);
    }

    @Override
    public Iterable<Task> getAllByGroupAndIdEmisor(String id_grupo, String id_usuario) {
        return tareaRepository.getAllByGroupAndIdEmisor(id_grupo, id_usuario);
    }

    @Override
    public Iterable<Task> getAllByIdReceptorAndStatus(String id_usuario, String estatus) {
        return tareaRepository.getAllByIdReceptorAndStatus(id_usuario,estatus);
    }

    @Override
    public Iterable<Task> getAllByIdEmisorAndStatus(String id_usuario, String estatus) {
        return tareaRepository.getAllByIdEmisorAndStatus(id_usuario, estatus);
    }
}
