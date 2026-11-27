package com.example.pr_greeting

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    companion object {

        // Ключ для передачи приветствия на второй экран
        const val EXTRA_GREETING = "extra_greeting"

        // Ключ для возврата результата на первый экран
        const val EXTRA_ANSWER = "extra_answer"
    }

    private lateinit var tvReceivedGreeting: TextView

    // Храним полученное приветствие
    private var greeting: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Подключаем XML-макет второго экрана
        setContentView(R.layout.activity_second)

        // Находим TextView
        tvReceivedGreeting = findViewById(
            R.id.tvReceivedGreeting
        )

        // Получаем данные из Intent
        greeting = intent.getStringExtra(EXTRA_GREETING)
            ?: ""

        // Показываем полученное приветствие
        tvReceivedGreeting.text = greeting
    }

    // Обработчик кнопки «Подтвердить»
    fun onConfirmClick(view: View) {

        // Создаём Intent для возврата результата
        val resultIntent = Intent()

        // Передаём подтверждённое приветствие
        resultIntent.putExtra(
            EXTRA_ANSWER,
            greeting
        )

        // Устанавливаем успешный результат
        setResult(RESULT_OK, resultIntent)

        // Закрываем второй экран
        // Результат вернётся в MainActivity
        finish()
    }

    // Обработчик кнопки «Назад»
    fun onBackClick(view: View) {

        // Закрываем текущий экран
        finish()
    }
}
