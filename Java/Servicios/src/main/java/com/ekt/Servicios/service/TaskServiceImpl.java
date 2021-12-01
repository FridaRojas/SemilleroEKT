package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Task;
import com.ekt.Servicios.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (tareaOptional.isPresent()){
            LocalDateTime ldt = date
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            Task tareaUpdate = tareaOptional.get();
            tareaUpdate.setFechaLeido(ldt);
            tareaUpdate.setLeido(leido);
            tareaRepository.save(tareaUpdate);
        }
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
            }else{
                //VALIDAR
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

        boolean nombreE = Pattern.matches("^[a-zA-Z\\s]*$", nombreEmisor);
        boolean nombreR = Pattern.matches("^[a-zA-Z\\s]*$", nombreReceptor);
        boolean tituloT = Pattern.matches("^[a-zA-Z0-9\\s]*$", titulo);
        boolean descripcionT = Pattern.matches("^[A-Za-z\\s]+[\\.]{0,1}[A-Za-z\\s]*$", descripcion);
        boolean estatusT = Pattern.matches("^[a-zA-Z]*$", estatus);
        ArrayList<String> errores = new ArrayList<>();
        if (!nombreE) errores.add("nombreEmisor");
        if (!nombreR) errores.add("nombreReceptor");
        if (!tituloT) errores.add("titulo");
        if (!descripcionT) errores.add("descripcion");
        if (!estatusT) errores.add("estatus");

        return errores;
    }
}
