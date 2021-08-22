package com.example.mycalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var lastNumeric: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onDigit(view: View) {
        val tvInput = findViewById<TextView>(R.id.tvInput)
        tvInput.append((view as Button).text)
        lastNumeric = true
    }

    fun onClear(view: View) {
        findViewById<TextView>(R.id.tvInput).text = ""
        lastNumeric = false
        lastDot = false
    }

    fun onDecimalPoint(view: View) {
        if (lastNumeric && !lastDot) {
            findViewById<TextView>(R.id.tvInput).append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onEqual(view: View) {
        if (lastNumeric) {
            val tvInput = findViewById<TextView>(R.id.tvInput)
            var tvValue = tvInput.text.toString()
            var prefix = ""

            try {
                if (tvValue.startsWith("-")) {
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }

                if (tvValue.contains("-")) {
                    val splitValue = tvValue.split("-")
                    val one = prefix.plus(splitValue.first())
                    val two = splitValue.last()

                    tvInput.text = removeZerosAfterZero((one.toBigDecimal().subtract(two.toBigDecimal())).toString())
                } else if (tvValue.contains("/")) {
                    val splitValue = tvValue.split("/")
                    val one = prefix.plus(splitValue.first())
                    val two = splitValue.last()

                    if (two == "0") {
                        tvInput.text = "Error"
                    } else {
                        tvInput.text = removeZerosAfterZero((one.toBigDecimal().divide(two.toBigDecimal())).toString())
                    }
                } else if (tvValue.contains("*")) {
                    val splitValue = tvValue.split("*")
                    val one = prefix.plus(splitValue.first())
                    val two = splitValue.last()

                    tvInput.text = removeZerosAfterZero((one.toBigDecimal().multiply(two.toBigDecimal())).toString())
                } else if (tvValue.contains("+")) {
                    val splitValue = tvValue.split("+")
                    val one = prefix.plus(splitValue.first())
                    val two = splitValue.last()

                    tvInput.text = removeZerosAfterZero((one.toBigDecimal().add(two.toBigDecimal())).toString())
                }
            } catch (ex: ArithmeticException) {
                ex.printStackTrace()
            }
        }
    }

    fun onOperator(view: View) {
        val tvInput = findViewById<TextView>(R.id.tvInput)
        if (lastNumeric && !isOperatorAdded(tvInput.text.toString())) {
            tvInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    private fun removeZerosAfterZero(result: String): String {
        var value = result
        if (result.endsWith(".0")) {
            value = result.substring(0, result.length - 2)
        } else if (result.contains(".0")){
            val splitValue = result.split(".")
            val two = splitValue.last()

            if (two.toInt() == 0) {
                value = result.substring(0, result.length - (two.length + 1))
            }
        }

        return value
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) {
            false
        } else {
            value.contains("/") || value.contains("*") || value.contains("+") || value.contains("-")
        }
    }
}