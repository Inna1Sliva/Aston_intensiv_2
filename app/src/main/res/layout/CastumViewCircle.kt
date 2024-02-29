package com.example.customview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import java.lang.Math.cos
import java.lang.Math.min
import java.lang.Math.sin
import kotlin.random.Random

class CustumViewCircle
    (context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val color: List<Int> = listOf(
        Color.RED,
        Color.YELLOW,
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.BLUE,
        Color.MAGENTA,

    )
    private val textList : List<String> = listOf(
        "Красный",
        "Оранжевый",
        "Желтый",
        "Зеленый",
        "Голубой",
        "Синий",
        "Фиолетовый"


    )

    private  var image:List<Drawable>
    private var currentDegree: Float = 0f
    private var circleRadius: Float = 0f
    private var paintCWith = 230f
    private var circleCenterY: Float = 0f
    private  var degreas =0
    private var isAnimating: Boolean = false
    private lateinit var seekBar:SeekBar
    private val paint:Paint = Paint()
    private lateinit var  textPaint: Paint


    fun setImage(bitmap: Bitmap?){

    }
    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth= paintCWith
        textPaint = Paint().apply {
            color = Color.WHITE
            textSize = 35f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
            //visibility = View.GONE
        }
        seekBar = SeekBar(context)
        seekBar.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT)
        seekBar.max = 100
        seekBar.progress = 50
        seekBar.rotation = -90f

        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                val scale = (5.0 + progress.toDouble() / 100).toFloat()
                circleRadius = min(width, height) / 2 * scale
                invalidate()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

        })


        image = listOf(
            ContextCompat.getDrawable(context , R.drawable.ic_launcher_foreground)!!,
            ContextCompat.getDrawable(context , R.drawable.ic_launcher_foreground)!!,
            ContextCompat.getDrawable(context , R.drawable.ic_launcher_foreground)!!
        )
        setOnClickListener {
            if (!isAnimating){
                rotateCircle()
            }

        }
    }

    fun rotateCircle() {
        currentDegree = (degreas % 360).toFloat()
        degreas = Random.nextInt(3600) + 720
        val rotateAnim = RotateAnimation(currentDegree, degreas.toFloat(),  Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnim.duration = 2000
        rotateAnim.fillAfter= true
        rotateAnim.interpolator = AccelerateDecelerateInterpolator()
        rotateAnim.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
                isAnimating= false
            }

            override fun onAnimationEnd(p0: Animation?) {

            }

            override fun onAnimationRepeat(p0: Animation?) {
                TODO("Not yet implemented")
            }

        })
        startAnimation(rotateAnim)

    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX =width/ 2f
        val centerY = height / 2f
        val radius = minOf(centerX,centerY) - paintCWith / 2f


        val sweepAngel =360f / color.size
        var startAngel = sweepAngel / 2
        for (i in color.indices){
           val ovalLift = centerX - radius
           val ovalTop = centerY - radius
            val ovalRight = centerX + radius
            val ovalBottom = centerY + radius

            val color = color[i]

            paint.color = color
            canvas.drawArc(
                ovalLift,
                ovalTop,
                ovalRight,
                ovalBottom,
                startAngel,
                sweepAngel,

                false,
                paint
            )

               startAngel += sweepAngel
            drawMenuText(canvas)
        }
    }
private fun drawMenuText(canvas: Canvas){
        textList.forEachIndexed { index, string ->
            val rect = Rect()
            textPaint.getTextBounds(string, 0, string.length, rect)
            val angle = 45f * index + (360f / color.size) / 2f
            val coordinat = getCoordinateXY(angle)
            canvas.drawText(
                string,
                coordinat.first ,
                coordinat.second  ,
                textPaint

            )
            invalidate()
        }
    }
    private fun getCoordinateXY (angle: Float): Pair< Float, Float>{

        val centerX =(width/ 2f)
        val centerY = (height / 2f)
        val radius = min(centerX, centerY)-paintCWith / 2f
        val x = centerX + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
        val y = centerY + radius * sin(Math.toRadians(angle.toDouble())).toFloat()

        return Pair(x,y)
    }


}