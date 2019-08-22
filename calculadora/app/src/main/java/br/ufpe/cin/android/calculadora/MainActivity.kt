package br.ufpe.cin.android.calculadora

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textCalc = findViewById<EditText>(R.id.text_calc)
        val infoText = findViewById<TextView>(R.id.text_info)
        val button0 = findViewById<Button>(R.id.btn_0)
        val button1 = findViewById<Button>(R.id.btn_1)
        val button2 = findViewById<Button>(R.id.btn_2)
        val button3 = findViewById<Button>(R.id.btn_3)
        val button4 = findViewById<Button>(R.id.btn_4)
        val button5 = findViewById<Button>(R.id.btn_5)
        val button6 = findViewById<Button>(R.id.btn_6)
        val button7 = findViewById<Button>(R.id.btn_7)
        val button8 = findViewById<Button>(R.id.btn_8)
        val button9 = findViewById<Button>(R.id.btn_9)
        val buttonAdd = findViewById<Button>(R.id.btn_Add)
        val buttonMinus = findViewById<Button>(R.id.btn_Subtract)
        val buttonMultiply = findViewById<Button>(R.id.btn_Multiply)
        val buttonDivide = findViewById<Button>(R.id.btn_Divide)
        val buttonClear = findViewById<Button>(R.id.btn_Clear)
        val buttonPower = findViewById<Button>(R.id.btn_Power)
        val buttonDot = findViewById<Button>(R.id.btn_Dot)
        val buttonLP = findViewById<Button>(R.id.btn_LParen)
        val buttonRP = findViewById<Button>(R.id.btn_RParen)
        val buttonEquals = findViewById<Button>(R.id.btn_Equal)
        button0.setOnClickListener {
            appendText(button0, textCalc, infoText)
        }
        button1.setOnClickListener {
            appendText(button1, textCalc, infoText)
        }
        button2.setOnClickListener {
            appendText(button2, textCalc, infoText)
        }
        button3.setOnClickListener {
            appendText(button3, textCalc, infoText)
        }
        button4.setOnClickListener {
            appendText(button4, textCalc, infoText)
        }
        button5.setOnClickListener {
            appendText(button5, textCalc, infoText)
        }
        button6.setOnClickListener {
            appendText(button6, textCalc, infoText)
        }
        button7.setOnClickListener {
            appendText(button7, textCalc, infoText)
        }
        button8.setOnClickListener {
            appendText(button8, textCalc, infoText)
        }
        button9.setOnClickListener {
            appendText(button9, textCalc, infoText)
        }
        buttonAdd.setOnClickListener {
            appendText(buttonAdd, textCalc, infoText)
            clearEditText(textCalc)
        }
        buttonMinus.setOnClickListener {
            appendText(buttonMinus, textCalc, infoText)
            clearEditText(textCalc)
        }
        buttonMultiply.setOnClickListener {
            appendText(buttonMultiply, textCalc, infoText)
            clearEditText(textCalc)
        }
        buttonDivide.setOnClickListener {
            appendText(buttonDivide, textCalc, infoText)
            clearEditText(textCalc)
        }
        buttonDot.setOnClickListener {
            appendText(buttonDot, textCalc, infoText)
            clearEditText(textCalc)
        }
        buttonClear.setOnClickListener {
            clearText(textCalc, infoText)
            clearEditText(textCalc)
        }
        buttonPower.setOnClickListener {
            appendText(buttonPower, textCalc, infoText)
            clearEditText(textCalc)
        }
        buttonLP.setOnClickListener {
            appendText(buttonLP, textCalc, infoText)
            clearEditText(textCalc)
        }
        buttonRP.setOnClickListener {
            appendText(buttonRP, textCalc, infoText)
            clearEditText(textCalc)
        }
        buttonEquals.setOnClickListener {
            var expression = infoText.text.toString()
            try{
                textCalc.setText(eval(expression).toString())
            }catch(e: Exception){
                Toast.makeText(applicationContext, "Not a valid Expression!", Toast.LENGTH_LONG).show()
            }
        }

        textCalc.setText(savedInstanceState?.getString("Result"))
        infoText.setText(savedInstanceState?.getString("Expression"))
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putString("Result", text_calc.text.toString())
        outState.putString('Expression', text_info.text.toString())
        super.onSaveInstanceState(outState, outPersistentState)

    }

    fun clearEditText(editText: EditText){
        editText.setText("")
    }

    fun clearText(editText: EditText, textView: TextView){
        editText.setText("")
        textView.setText("")
    }

    fun appendText(button: Button, editText: EditText, textView: TextView){
        editText.append(button.text)
        textView.append(button.text)
    }

    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
