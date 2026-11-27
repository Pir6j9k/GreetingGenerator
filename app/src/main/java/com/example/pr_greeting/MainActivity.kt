package com.example.pr_greeting


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
class MainActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var cbPolite: CheckBox
    private lateinit var tvResult: TextView
    private lateinit var tvStatus: TextView

    // Храним последнее сгенерированное приветствие
    private var currentGreeting: String = ""

    // Регистрируем получение результата со второго экрана
    // Регистрация выполняется на уровне класса, до запуска Activity
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        // Проверяем, успешно ли завершился второй экран
        if (result.resultCode == RESULT_OK) {

            // Получаем Intent с результатом
            val data = result.data

            // Получаем возвращённое приветствие
            val answer = data?.getStringExtra(
                SecondActivity.EXTRA_ANSWER
            )

            // Показываем результат на первом экране
            tvStatus.text = answer
                ?: getString(R.string.result_placeholder)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Подключаем XML-макет первого экрана
        setContentView(R.layout.activity_main)

        // Находим элементы по их id
        etName = findViewById(R.id.etName)
        cbPolite = findViewById(R.id.cbPolite)
        tvResult = findViewById(R.id.tvResult)
        tvStatus = findViewById(R.id.tvStatus)
    }

    // Проверка имени и генерация приветствия
    private fun generateGreeting(): String? {

        // Получаем введённое имя
        val name = etName.text.toString().trim()

        // Проверяем, не пустое ли поле
        if (name.isEmpty()) {
            etName.error = getString(R.string.empty_name_error)
            return null
        }

        // Разрешаем буквы, пробелы, дефисы и апострофы
        if (!name.all {
                it.isLetter() || it.isWhitespace() ||
                        it == '-' || it == '\''
            }) {

            etName.error = getString(R.string.invalid_name_error)
            return null
        }

        // Получаем три шаблона из string-array
        val templates = resources.getStringArray(
            R.array.greeting_templates
        )

        // Выбираем шаблон по длине имени
        val templateIndex = when {
            name.length <= 4 -> 0
            name.length <= 8 -> 1
            else -> 2
        }

        // Подставляем имя в выбранный шаблон
        var greeting = getString(
            R.string.greeting_result,
            String.format(templates[templateIndex], name)
        )

        // Добавляем вежливое обращение, если установлен CheckBox
        if (cbPolite.isChecked) {

            val prefix = getString(R.string.polite_prefix)

            greeting = prefix + greeting
        }

        return greeting
    }

    // Метод вызывается при нажатии кнопки «Рассчитать»
    // android:onClick в XML вызывает этот метод
    fun onCalculateClick(view: View) {

        // Получаем сгенерированное приветствие
        val greeting = generateGreeting()

        // Если проверка прошла успешно, показываем результат
        if (greeting != null) {

            currentGreeting = greeting

            tvResult.text = greeting

            tvStatus.text = ""
        }
    }

    // Метод вызывается при нажатии на иллюстрацию
    fun onImageClick(view: View) {

        // Показываем подсказку через Snackbar
        Snackbar.make(
            view,
            getString(R.string.image_hint),
            Snackbar.LENGTH_LONG
        ).show()
    }

    // Метод вызывается при нажатии кнопки «Далее»
    fun onNextClick(view: View) {

        // Проверяем, существует ли актуальное приветствие
        // Генерируем его заново, чтобы учесть изменения CheckBox или имени
        val greeting = generateGreeting()

        if (greeting == null) {
            return
        }

        currentGreeting = greeting

        // Создаём явный Intent для второго экрана
        val intent = Intent(this, SecondActivity::class.java)

        // Передаём приветствие
        intent.putExtra(
            SecondActivity.EXTRA_GREETING,
            currentGreeting
        )

        // Открываем второй экран и ожидаем результат
        launcher.launch(intent)
    }
}