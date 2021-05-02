package naffate.mcihelle.myfeelings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import naffate.mcihelle.myfeelings.utilities.Emociones
import naffate.mcihelle.myfeelings.utilities.CustomCircleDrawable
import naffate.mcihelle.myfeelings.utilities.customBarDrawable
import naffate.mcihelle.myfeelings.utilities.JSONFile
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    var jsonFile: JSONFile? = null
    var veryHappy=0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verySad =0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsonFile= JSONFile()
        fetchingData()
        if(!data){
            var emociones=ArrayList<Emociones>()
            val fondo=CustomCircleDrawable(this,emociones)
            graph.background=fondo
            graphVeryHappy.background=customBarDrawable(this, Emociones("Muy feliz", 0.0f, R.color.mustard, veryHappy))
            graphVeryHappy.background=customBarDrawable(this, Emociones("Feliz", 0.0f, R.color.orange, happy))
            graphVeryHappy.background=customBarDrawable(this, Emociones("Neutral", 0.0f, R.color.greenie, neutral))
            graphVeryHappy.background=customBarDrawable(this, Emociones("Triste", 0.0f, R.color.blue, sad))
            graphVeryHappy.background=customBarDrawable(this, Emociones("Muy triste", 0.0f, R.color.deepblue, verySad))
        }else{
            actualizarGrafica()
            iconoMayoria()
        }

        btnGuardar.setOnClickListener {
            guardar()
        }

        btnmuyfeliz.setOnClickListener {
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }
        btnmediofeliz.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }
        btnNeutral.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }
        btnsad.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }
        btnverySad.setOnClickListener {
            verySad++
            iconoMayoria()
            actualizarGrafica()
        }

    }
    fun fetchingData() {
        try {
            var json: String = jsonFile?.getData(this) ?: ""
            if (json != "") {
                this.data = true
                var jsonArray: JSONArray = JSONArray(json)

                this.lista = parseJson(jsonArray)

                lista.forEach { i ->
                    when (i.nombre) {
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> verySad = i.total
                    }
                }
            }else{
                this.data=false
            }
        }catch (e:JSONException){
            e.printStackTrace()
        }
    }

    fun iconoMayoria() {
        if(veryHappy>happy && veryHappy > neutral && veryHappy>sad&&veryHappy>verySad){
            icon.setImageResource(R.drawable.ic_baseline_sentiment_very_satisfied_24)
        }else if(happy>veryHappy&&happy>neutral&&happy>sad&&happy>verySad) {
            icon.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_alt_24)
        }else if(neutral>veryHappy&&neutral>happy&&neutral>sad&&neutral>verySad){
            icon.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_24)
        }else if(sad>veryHappy&&sad>neutral&&sad>happy&&sad>verySad){
            icon.setImageResource(R.drawable.ic_baseline_sentiment_dissatisfied_24)
        }else if(verySad>veryHappy&&verySad>neutral&&verySad>sad&&verySad>happy){
            icon.setImageResource(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
        }


    }

    fun actualizarGrafica() {
        val total = veryHappy+happy+neutral+verySad+sad

        var pVH:Float=(veryHappy*100/total)
        var pH:Float=(happy*100/total)
        var pN:Float=(neutral*100/total)
        var pS:Float=(sad*100/total)
        var pVS:Float =(verySad*100/total)

        Log.d("porcentajes", "very happy"+pVH)
        Log.d("porcentajes", "happy"+pH)
        Log.d("porcentajes", "neutral"+pN)
        Log.d("porcentajes", "sad"+pS)
        Log.d("porcentajes", "very sad"+pVS)

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepblue, verySad))

        val fondo=CustomCircleDrawable(this,lista)

        graphVeryHappy.background=customBarDrawable(this,lista[0])
        graphfeliz.background=customBarDrawable(this,lista[1])
        graphneutral.background=customBarDrawable(this,lista[2])
        graphtriste.background=customBarDrawable(this,lista[3])
        graphVerytriste.background=customBarDrawable(this,lista[4])

        graph.background=fondo
    }

    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones>
    {
        var lista=ArrayList<Emociones>()

        for(i in 0..jsonArray.length()){
            try{
                val nombre=jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje=jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion=Emociones(nombre,porcentaje,color,total)
                lista.add(emocion)
            }catch (e:JSONException){
                e.printStackTrace()
            }
        }

        return lista
    }

    fun guardar() {
        var jsonArray = JSONArray()

        var o:Int=0
        lista.forEach { i->
            Log.d("objetos",i.toString())
            var j:JSONObject= JSONObject()
            j.put("nombre",i.nombre)
            j.put("porcentaje",i.porsentaje)
            j.put("color", i.color)
            j.put("total", i.total)

            jsonArray.put(o,j)
            o++
        }

        jsonFile?.saveData(this,jsonArray.toString())

        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }

}