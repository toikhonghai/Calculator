package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun isNumber(view: View) {
        if (view is Button) binding.txtDisplay.append(view.text)
    }

    fun isOperation(view: View) {
        if (view is Button) {
            var expression = binding.txtDisplay.text
            if (expression.isEmpty()) {
                expression = "0"
                binding.txtDisplay.text = "0"
            }
            if (expression[expression.length - 1].isDigit()) binding.txtDisplay.append(view.text)
            else {
                binding.txtDisplay.text = expression.subSequence(0, expression.length - 1)
                binding.txtDisplay.append(view.text)
            }
        }
    }

    fun clear(view: View) {
        binding.txtDisplay.text = ""
    }

    fun backSpace(view: View) {
        val expression = binding.txtDisplay.text
        if (expression.isNotEmpty()) binding.txtDisplay.text =
            expression.subSequence(0, expression.length - 1)
    }

    fun clearEntry(view: View) {
        val expression = binding.txtDisplay.text
        var index = 0
        if (expression.isEmpty()) return
        for (i in (expression.length - 1) downTo 0) {
            if (!(expression[i].isDigit())) {
                index = i
                break
            }
        }
        if (index == 0) {
            binding.txtDisplay.text = ""
            return
        }
        binding.txtDisplay.text = expression.subSequence(0, index + 1)
    }

    fun toggleNegative(view: View) {
        val expression = binding.txtDisplay.text
        if (expression.isEmpty()) return
        var index = 0
        for (i in (expression.length - 1) downTo 0) {
            if (!(expression[i].isDigit())) {
                index = i
                break
            }
        }
        if (index == 0) {
            binding.txtDisplay.text = "-$expression"
            return
        }
        binding.txtDisplay.text = expression.subSequence(0, index + 1)
        val negativeNumber = expression.subSequence(index + 1, expression.length)
        binding.txtDisplay.append("-$negativeNumber")
    }


    fun result(view: View) {
        val expression = binding.txtDisplay.text
        if (expression.isEmpty()) return
        try {
            var res = 0.0
            var curNumber = ""
            var curOperator = "+"
            var i = 0
            for (i in expression.indices) {
                val char = expression[i]
                if (char.isDigit() || char == '.') curNumber += char
                else {
                    if (curNumber.isNotEmpty()) {
                        val num = curNumber.toDouble()
                        when (curOperator) {
                            "+" -> res += num
                            "-" -> res -= num
                            "×" -> res *= num
                            "/" -> {
                                if (num == 0.0) {
                                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                                    return
                                }
                                res /= num
                            }
                        }
                        curNumber = ""
                    } else {
                        if (char == '-') curNumber = "-"
                        else {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                            return
                        }
                    }
                    if (char != '-' || curNumber.isEmpty()) curOperator = char.toString()
                }
            }
            if (curNumber.isNotEmpty()) {
                val num = curNumber.toDouble()
                when (curOperator) {
                    "+" -> res += num
                    "-" -> res -= num
                    "×" -> res *= num
                    "/" -> {
                        if (num == 0.0) {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                            return
                        }
                        res /= num
                    }
                }
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                return
            }
            val fomatResult = if (res != res.toLong().toDouble()) {
                String.format("%.1f", res)
            } else res.toLong().toString()
            binding.txtDisplay.text = fomatResult
        } catch (e: Exception) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }


}