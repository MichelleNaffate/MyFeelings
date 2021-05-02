package naffate.mcihelle.myfeelings.utilities

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import naffate.mcihelle.myfeelings.R

class customBarDrawable: Drawable {
    var coordenadas : RectF?= null
    var context: Context? = null
    var emocion : Emociones? = null

    constructor(context: Context?, emocion: Emociones) : super() {
        this.context = context
        this.emocion = emocion
    }

    override fun draw(canvas: Canvas) {
        var fondo: Paint = Paint()
        fondo.style =Paint.Style.FILL
        fondo.isAntiAlias = true
        fondo.color=context?.resources?.getColor(R.color.gray) ?: R.color.gray
        val ancho: Float = ( canvas.width - 10).toFloat()
        val alto :Float = (canvas.height - 10 ).toFloat()

        coordenadas = RectF(0.0F,0.0F,ancho,alto)

        canvas.drawRect(coordenadas!!, fondo)

        if(this.emocion != null){
            val porcentaje : Float = this.emocion!!.porsentaje*(canvas.width - 10)/100
            var coordenadas2 = RectF(0.0F,0.0F, porcentaje, alto)

            var seccion: Paint= Paint()
            seccion.style = Paint.Style.FILL
            seccion.color = ContextCompat.getColor(this.context!!, emocion!!.color)
            canvas.drawRect(coordenadas2, seccion)
        }
    }

    override fun setAlpha(alpha: Int) {
        TODO("Not yet implemented")
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("Not yet implemented")
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }
}