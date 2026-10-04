package sri.example.contextflowai

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var chatContainer: LinearLayout
    private lateinit var scrollView: ScrollView
    private lateinit var messageInput: EditText

    // Conversation context
    private var currentTopic = ""
    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatContainer = findViewById(R.id.chatContainer)
        scrollView = findViewById(R.id.scrollView)
        messageInput = findViewById(R.id.messageInput)

        val sendButton = findViewById<Button>(R.id.sendButton)

        sendButton.setOnClickListener {
            sendMessage()
        }

        addMessage(
            "Hello! 👋 I'm ContextFlow AI.\n\n" +
                    "I can remember information from our conversation. " +
                    "Try telling me your name or asking about Python, Kotlin, Android, or Machine Learning.",
            false
        )
    }

    private fun sendMessage() {

        val message = messageInput.text.toString().trim()

        if (message.isEmpty()) return

        addMessage(message, true)

        val response = generateResponse(message)

        addMessage(response, false)

        messageInput.text.clear()
    }

    private fun generateResponse(message: String): String {

        val text = message.lowercase()

        // Remember user's name
        if (text.contains("my name is")) {

            userName = message
                .substringAfter("my name is")
                .trim()

            return "Nice to meet you, $userName! 😊\n\n" +
                    "I'll remember your name during this conversation."
        }

        // Recall user's name
        if (text.contains("what is my name") ||
            text.contains("do you know my name")) {

            return if (userName.isNotEmpty()) {
                "Yes! Your name is $userName. 😊"
            } else {
                "You haven't told me your name yet."
            }
        }

        // Python topic
        if (text.contains("python")) {

            currentTopic = "python"

            return "Python is a popular programming language used in " +
                    "AI, machine learning, data science, automation and web development."
        }

        // Kotlin topic
        if (text.contains("kotlin")) {

            currentTopic = "kotlin"

            return "Kotlin is a modern programming language widely used " +
                    "for Android application development."
        }

        // Android topic
        if (text.contains("android")) {

            currentTopic = "android"

            return "Android development is used to create mobile applications " +
                    "using tools such as Android Studio, Kotlin and the Android SDK."
        }

        // Machine learning topic
        if (text.contains("machine learning")) {

            currentTopic = "machine learning"

            return "Machine learning allows computers to learn patterns " +
                    "from data and make predictions or decisions."
        }

        // Context follow-up
        if (text.contains("tell me more") ||
            text == "more" ||
            text.contains("explain more")) {

            return when (currentTopic) {

                "python" ->
                    "Python has many useful libraries such as NumPy, Pandas " +
                            "and TensorFlow. It is widely used in AI and data science."

                "kotlin" ->
                    "Kotlin provides concise syntax, null safety and coroutines, " +
                            "which make Android development easier."

                "android" ->
                    "Android applications can use Activities, XML layouts, APIs, " +
                            "databases and cloud services."

                "machine learning" ->
                    "Machine learning includes supervised, unsupervised and " +
                            "reinforcement learning."

                else ->
                    "Tell me a topic first, such as Python, Kotlin, Android " +
                            "or Machine Learning."
            }
        }

        // Why / How / Advantages / Examples
        if (text.contains("why") ||
            text.contains("how") ||
            text.contains("advantages") ||
            text.contains("example")) {

            return when (currentTopic) {

                "python" ->
                    "Since we're discussing Python, its main advantages are " +
                            "simple syntax, readability and a large number of libraries."

                "kotlin" ->
                    "Since we're discussing Kotlin, its advantages include " +
                            "concise code, null safety and strong Android support."

                "android" ->
                    "Since we're discussing Android, developers can create " +
                            "applications for a wide range of mobile devices."

                "machine learning" ->
                    "Since we're discussing machine learning, its advantage " +
                            "is that systems can learn patterns from data."

                else ->
                    "I need a topic first. Try Python, Kotlin, Android or Machine Learning."
            }
        }

        // Greeting
        if (text.contains("hello") ||
            text == "hi" ||
            text.contains("hey")) {

            return "Hello! 👋 What would you like to talk about?"
        }

        // Thanks
        if (text.contains("thank")) {

            return "You're welcome! 😊 You can continue asking follow-up questions."
        }

        return "I understood your message, but I don't have enough context yet.\n\n" +
                "Try asking about Python, Kotlin, Android or Machine Learning."
    }

    private fun addMessage(message: String, isUser: Boolean) {

        val textView = TextView(this)

        textView.text = message
        textView.textSize = 15f
        textView.setTextColor(
            if (isUser) Color.WHITE else Color.rgb(35, 40, 50)
        )

        textView.setPadding(16, 12, 16, 12)

        textView.background = getDrawable(
            if (isUser) R.drawable.bg_user
            else R.drawable.bg_bot
        )

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(
            if (isUser) 60 else 0,
            6,
            if (isUser) 0 else 60,
            6
        )

        params.gravity =
            if (isUser) Gravity.END
            else Gravity.START

        textView.layoutParams = params

        chatContainer.addView(textView)

        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}