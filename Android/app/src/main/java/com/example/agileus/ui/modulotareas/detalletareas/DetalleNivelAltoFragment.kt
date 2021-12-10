package com.example.agileus.ui.modulotareas.detalletareas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import com.example.agileus.R
import com.example.agileus.databinding.FragmentDetalleNivelAltoBinding
import com.example.agileus.models.TaskUpdate
import com.example.agileus.providers.DownloadProvider
import com.example.agileus.providers.FirebaseProvider
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.moduloreportes.reportes.ReporteMensajesFragmentDirections
import com.example.agileus.ui.modulotareas.dialogostareas.*
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoFechaListener
import com.example.agileus.ui.modulotareas.listenerstareas.dialogoConfirmarListener
import com.example.agileus.utils.Constantes
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*


class DetalleNivelAltoFragment : Fragment(), DialogoFechaListener,
    DialogoTareaCreadaExitosamente.NoticeDialogListener, dialogoConfirmarListener {
    lateinit var firebaseProvider: FirebaseProvider
    lateinit var mStorageInstance: FirebaseStorage
    lateinit var mStorageReference: StorageReference
    var uriPost: String = ""
    var idsuperiorInmediato: String = "618d9c26beec342d91d747d6"
    lateinit var resultLauncherArchivo: ActivityResultLauncher<Intent>


    private var anioFin: Int = 0
    private var mesInicio: String = ""
    private var anioInicio: Int = 0
    private var diaInicio: String = ""
    private var diaFin: String = ""
    private var mesFin: String = ""
    private var _binding: FragmentDetalleNivelAltoBinding? = null
    private val binding get() = _binding!!
    private lateinit var observaciones: String
    private lateinit var fechaFin: Date
    private lateinit var fechaInicio: Date
    private lateinit var descripcion: String
    private lateinit var estatus: String
    private lateinit var prioridad: String
    private lateinit var nombrePersona: String
    private lateinit var nombreTarea: String
    private lateinit var detalleNivelAltoViewModel: DetalleNivelAltoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        detalleNivelAltoViewModel =
            ViewModelProvider(this).get(DetalleNivelAltoViewModel::class.java)

        _binding = FragmentDetalleNivelAltoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: DetalleNivelAltoFragmentArgs by navArgs()
        firebaseProvider = FirebaseProvider()
        mStorageInstance =
            FirebaseStorage.getInstance()                           /*  *** Instancias Fb Storage ***  */
        mStorageReference = mStorageInstance.getReference("Documentos")

        resultLauncherArchivo =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        try {
                            var returnUri = data?.data!!
                            val uriString = data.toString()
                            val myFile = File(uriString).name
                            //val myFile = getRealPathFromURI(requireContext(), returnUri)
                            binding.btnAdjuntarArchivoF.text = "Archivo seleccionado"
                            Log.d("mensaje", "PDF: $myFile")
                            firebaseProvider.subirPdfFirebase(
                                returnUri,
                                Constantes.referenciaTareas,
                                "tarea$idsuperiorInmediato${(0..999).random()}"

                            )
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                            Log.e("mensaje", "File not found. ${e.message}");
                        }
                    }
                } else {
                    Toast.makeText(context, "No se selecciono archivo", Toast.LENGTH_LONG)
                        .show()
                }
            }
        firebaseProvider.obs.observe(viewLifecycleOwner, {
            uriPost = it
        })


        setInfo(args)
        desactivarCampos(args)

        with(binding) {
            btnCancelarTareaF.setOnClickListener {
                cancelarTarea(args)
            }

            btnEditarTareaF.setOnClickListener {
                activarCampos(args)
            }

            btnCancelarEdicion.setOnClickListener {
                desactivarCampos(args)
            }

            btnAdjuntarArchivoF.setOnClickListener {
                val intentPdf = Intent()
                intentPdf.setAction(Intent.ACTION_GET_CONTENT)
                intentPdf.type =
                    "application/pdf"                     // Filtra para archivos pdf
                resultLauncherArchivo.launch(intentPdf)
            }

            btnDescargarArchivoFF.setOnClickListener {
                var mDownloadProvider = DownloadProvider()
                mDownloadProvider.dowloadFile(
                    (activity as HomeActivity).applicationContext,
                    args.tareas.archivo, "archivo"
                )
            }


        }

        binding.btnValidarTarea.setOnClickListener {
            val newFragment2 =
                DialogoValidarTarea(args,this)
            newFragment2.show((activity as HomeActivity).supportFragmentManager, "missiles")
        }

        binding.btnGuardarTareaF.setOnClickListener {
            var obs = binding.txtObservacionesD.text.toString()
            args.tareas.observaciones = obs

            //todo cuando haya que modificar archivo crear un metodo para subir el archivo
            //todo si el archivo no es nulo o vacio
            //todo mandarlo a llamar en esta parte pasandole el parametro del archivo.

            var titulo: String
            var descripcion: String
            var fecha_ini: String
            var fecha_fin: String
            var prioridad: String
            var estatus: String
            var observaciones: String

            titulo = binding.txtNombreTareaD.text.toString()
            descripcion = binding.txtDescripcionD.text.toString()
            fecha_ini = binding.txtFechaInicioD.text.toString()
            fecha_fin = binding.txtFechaFinD.text.toString()
            prioridad = args.tareas.prioridad
            observaciones = obs
            estatus = "pendiente"

            if (descripcion.isNullOrEmpty()) {
                Toast.makeText(context, "La descripcion no puede ir vacia", Toast.LENGTH_SHORT)
                    .show()
            } else {

                var update = TaskUpdate(
                    titulo,
                    descripcion,
                    fecha_ini,
                    fecha_fin,
                    prioridad,
                    estatus,
                    observaciones
                )

                desactivarCampos(args)

                val newFragment2 =
                    DialogoActualizarTarea(
                        update,
                        args.tareas.idTarea,
                        args.tareas.idEmisor,
                        this
                    )
                newFragment2.show((activity as HomeActivity).supportFragmentManager, "missiles")
            }

        }

        binding.txtFechaInicioD.setOnClickListener {
            val newFragment = EdtFecha(this, 1)
            newFragment.show(parentFragmentManager, "Edt fecha")
        }

        binding.txtFechaFinD.setOnClickListener {
            val newFragment = EdtFecha(this, 2)
            newFragment.show(parentFragmentManager, "Edt fecha")
        }

    }

    private fun setInfo(args: DetalleNivelAltoFragmentArgs) {
//        Toast.makeText(context, "${args.tareas.idTarea}", Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, args.tareas.archivo, Toast.LENGTH_SHORT).show()
        Log.d("Mensaje", args.tareas.archivo)
        Log.d("Mensaje", args.tareas.estatus)
        var mesI: String = ""
        var diaI: String = ""
        var mesF: String = ""
        var diaF: String = ""
        var fechaFi: String = ""
        var fechaIn: String = ""
        var statusCampo = "Estatus: ${args.tareas.estatus.uppercase()}"
        var prioridadCampo = "Prioridad: ${args.tareas.prioridad.uppercase()}"
        val sdf3 = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

        val cal = Calendar.getInstance()
        var fechaI = sdf3.parse(args.tareas.fechaIni.toString())
        var fechaF = sdf3.parse(args.tareas.fechaFin.toString())


        cal.time = fechaI

        var anio = cal[Calendar.YEAR] + 1
        var mes = cal[Calendar.MONTH] + 1
        var dia = cal[Calendar.DATE] + 1
        if (mes < 10) {
            mesI = "0$mes"
        } else {
            mesI = mes.toString()
        }
        if (dia < 10) {
            diaI = "0$dia"
        } else {
            diaI = dia.toString()
        }


        fechaIn =
            cal[Calendar.YEAR].toString() + "-" + mesI + "-" + diaI

///////////////////////////////////////////////////////////////777
        cal.time = fechaF

        mes = cal[Calendar.MONTH] + 1
        dia = cal[Calendar.DATE] + 1
        if (mes < 10) {
            mesF = "0$mes"
        } else {
            mesF = mes.toString()
        }

        if (dia < 10) {
            diaF = "0$dia"
        } else {
            diaF = dia.toString()
        }

        fechaFi =
            cal[Calendar.YEAR].toString() + "-" + mesF + "-" + diaF
        Log.d("Mensaje", "fecha nueva $fechaFi")

        nombreTarea = args.tareas.titulo
        nombrePersona = args.tareas.nombreEmisor
        prioridad = args.tareas.prioridad
        estatus = args.tareas.estatus
        descripcion = args.tareas.descripcion

        if (!args.tareas.observaciones.isNullOrEmpty()) {
            observaciones = args.tareas.observaciones
            binding.txtObservacionesD.setText(observaciones)
            binding.txtObservacionesD.isVisible = true
        } else {
            binding.txtObservacionesD.isVisible = false
            observaciones = ""
        }

        if (!args.tareas.archivo.isNullOrEmpty()) {
            binding.btnDescargarArchivoFF.isVisible = true
        } else {
            binding.btnDescargarArchivoFF.isVisible = false
            args.tareas.archivo = ""
        }

        if (args.tareas.estatus.equals("terminada")) {
            binding.buttons.isVisible = false
            val newFragment = DialogoAceptar("Tarea Terminada")
            newFragment.show(parentFragmentManager, "tarea terminada")
        }

        if (args.tareas.estatus.equals("revision")) {
            binding.btnValidarTarea.isVisible = true
            binding.btnValidarTarea.isEnabled = true
        } else {
            binding.btnValidarTarea.isVisible = true
            binding.btnValidarTarea.isEnabled = false
        }

        with(binding) {
            txtNombreTareaD.setText(nombreTarea)
            txtNombrePersonaD.text = nombrePersona
            txtPrioridadD.setText("Prioridad: " + prioridad)
            txtDescripcionD.setText(descripcion)
            txtEstatusD.setText("Estatus: " + estatus)
            txtFechaInicioD.setText(fechaIn)
            txtFechaFinD.setText(fechaFi)
            txtObservacionesD.setText(observaciones)
        }
    }

    private fun desactivarCampos(args: DetalleNivelAltoFragmentArgs) {
        with(binding) {
            Log.d("Mensaje", args.tareas.archivo)
//            Toast.makeText(context, args.tareas.archivo, Toast.LENGTH_SHORT).show()
            if (!args.tareas.archivo.isNullOrEmpty()) {
                binding.btnDescargarArchivoFF.isVisible = true
            }
            if (!args.tareas.observaciones.isNullOrEmpty()) {
                txtObservacionesD.isVisible = true
                txtObservacionesD.isEnabled = false
            } else {
                txtObservacionesD.isVisible = false
                txtObservacionesD.isEnabled = false
            }


            btnValidarTarea.isVisible = true

            txtDescripcionD.isEnabled = false
            txtDescripcionD.isEnabled = false
            txtFechaInicioD.isEnabled = false
            btnAdjuntarArchivoF.isVisible = false
            txtFechaFinD.isEnabled = false
            btnEditarTareaF.isVisible = true
            btnCancelarTareaF.isVisible = true
            btnCancelarEdicion.isVisible = false
            btnGuardarTareaF.isVisible = false
        }
    }

    private fun activarCampos(args: DetalleNivelAltoFragmentArgs) {
        with(binding) {
            Toast.makeText(context, "Estatus: ${args.tareas.estatus}", Toast.LENGTH_SHORT).show()
            /* if (args.tareas.estatus.equals("revision")) {
                 btnGuardarTareaF.setText("Validar")
             }*/
            btnValidarTarea.isVisible = false
            txtObservacionesD.isEnabled = true
            txtObservacionesD.isVisible = true
            btnAdjuntarArchivoF.isVisible = false
            btnDescargarArchivoFF.isVisible = false
            txtDescripcionD.isEnabled = true
            txtDescripcionD.isEnabled = true
            txtFechaInicioD.isEnabled = true
            txtFechaFinD.isEnabled = true
            btnEditarTareaF.isVisible = false
            btnCancelarTareaF.isVisible = false
            btnCancelarEdicion.isVisible = true
            btnGuardarTareaF.isVisible = true

        }
    }

    private fun cancelarTarea(args: DetalleNivelAltoFragmentArgs) {
        val dialogoAceptar = DialogoEliminarTarea(args, this)
        dialogoAceptar.show(
            (activity as HomeActivity).supportFragmentManager,
            getString(R.string.dialogoAceptar)
        )
    }

    override fun onDateInicioSelected(anio: Int, mes: String, dia: String) {
        anioInicio = anio
        mesInicio = mes
        diaInicio = dia

        val fecha = binding.txtFechaInicioD
        val fechaObtenida = "$anio-$mes-$dia"
        fecha.setText(fechaObtenida)
    }

    override fun onDateFinSelected(anio: Int, mes: String, dia: String) {
        anioFin = anio
        mesFin = mes
        diaFin = dia

        val fecha = binding.txtFechaFinD
        val fechaObtenida = "$anio-$mes-$dia"
        fecha.setText(fechaObtenida)

    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

    override fun abreDialogoConfirmar(mensaje: String) {
        val dialogoAceptar = DialogoAceptar(mensaje)
        dialogoAceptar.show(
            (activity as HomeActivity).supportFragmentManager,
            getString(R.string.dialogoAceptar)
        )
    }

}