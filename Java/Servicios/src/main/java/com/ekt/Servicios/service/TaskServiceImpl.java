package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Task;
import com.ekt.Servicios.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

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
}
