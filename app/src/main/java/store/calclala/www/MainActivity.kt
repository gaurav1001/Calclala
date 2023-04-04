package store.calclala.www

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import store.calclala.www.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var working: TextView? = null
    private var result: TextView? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var expression: Expression
    private var lastNumeric = false
    private var statsError = false
    private var isDot = false
    private var isFlash = false
    private lateinit var cameraManager: CameraManager
    private lateinit var getCameraID:String
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            // O means back camera unit,
            // 1 means front camera unit
            getCameraID = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        result = binding.result
        working = binding.working

        binding.ac.setOnClickListener {
            onAllClear()
        }

        binding.one.setOnClickListener {
            onDigitClick(it)
        }

        binding.two.setOnClickListener {
            onDigitClick(it)
        }

        binding.three.setOnClickListener {
            onDigitClick(it)
        }

        binding.four.setOnClickListener {
            onDigitClick(it)
        }

        binding.five.setOnClickListener {
            onDigitClick(it)
        }

        binding.six.setOnClickListener {
            onDigitClick(it)
        }

        binding.seven.setOnClickListener {
            onDigitClick(it)
        }

        binding.eight.setOnClickListener {
            onDigitClick(it)
        }

        binding.nine.setOnClickListener {
            onDigitClick(it)
        }

        binding.zero.setOnClickListener {
            onDigitClick(it)
        }

        binding.equal.setOnClickListener {
            onEqualClick()
        }

        binding.divide.setOnClickListener {
            onOperatorClick(it)
        }

        binding.plus.setOnClickListener {
            onOperatorClick(it)
        }
        binding.minus.setOnClickListener {
            onOperatorClick(it)
        }
        binding.multiply.setOnClickListener {
            working?.append("*")
        }
        binding.cut.setOnClickListener {
            onBackClick()
        }
        binding.dot.setOnClickListener {
            onDotClick(it)
        }
        binding.flash.setOnClickListener {
          onFlashClick()
        }
        binding.setting.setOnClickListener {

        }
    }

    private fun onAllClear(){
        working?.text = ""
        result?.text = ""
        lastNumeric = false
        statsError = false
        isDot = false
    }

    private fun onDigitClick(view: View) {

        if(statsError){
            working?.text = (view as Button).text.toString()
            statsError = false
        }else{
            working?.append((view as Button).text.toString())
        }
        lastNumeric = true

    }

    private fun onEqual() {
        if (lastNumeric && !statsError && working?.text?.isNotEmpty() == true) {
            val txt = working?.text.toString()
            expression = ExpressionBuilder(txt).build()
            try {
                val data = expression.evaluate()
                val b = "="+String.format("%.2f",data)
                result?.text = b


            } catch (e: Exception) {
                Log.d("ERROR", "Err")
                statsError = true
                lastNumeric = false
            }
        }else{
            Toast.makeText(this,"input must be digit", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onEqualClick(){
        result?.text = working?.text
        onEqual()
    }

    private fun onOperatorClick(view: View) {
        val catch = view as Button
        when(catch.text){
            "X" -> working?.append("*")
            else -> working?.append(catch.text)
        }
        isDot = false
        lastNumeric = false
    }

    private fun onBackClick(){
        working?.text = working?.text.toString().dropLast(1)

    }

    private fun onDotClick(view: View) {
        if (!isDot) {
            working?.append((view as AppCompatButton).text.toString())
            lastNumeric = false
            isDot = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onFlashClick(){
        isFlash = if(isFlash){
            try {
                cameraManager.setTorchMode(getCameraID,false)
            }catch (e:Exception){
                e.printStackTrace()
            }
            binding.flash.setImageResource(R.drawable.baseline_flashlight_off_24)
            false
        }else{
            try {
                cameraManager.setTorchMode(getCameraID,true)
            }catch (e:Exception){
                e.printStackTrace()
            }
            binding.flash.setImageResource(R.drawable.baseline_flashlight_on_24)
            true
        }
    }
}