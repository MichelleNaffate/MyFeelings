package naffate.mcihelle.myfeelings.utilities
import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.FileInputStream
import java.io.IOException
import kotlin.math.log

class JSONFile {
    val MY_FEELING ="data.json"
    constructor(){

    }
    fun saveData(context: Context,json: String){
        try {
            context.openFileOutput(MY_FEELING, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }

        }catch (e: IOException){
            Log.e("GUARDAR","Error in writing"+ e.localizedMessage)

        }

    }
    fun getData(context: Context):String{
        try{
            return context.openFileInput(MY_FEELING).bufferedReader().readLine()
        }catch (e: IOException){
            Log.e("OBTENER","Error in fetching data :"+e.localizedMessage)
            return ""
        }
    }

}




