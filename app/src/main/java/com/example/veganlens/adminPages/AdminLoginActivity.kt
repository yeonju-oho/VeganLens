import com.example.veganlens.R

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var etAdminId: EditText
    private lateinit var etAdminPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        etAdminId = findViewById(R.id.etAdminId)
        etAdminPassword = findViewById(R.id.etAdminPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val adminId = etAdminId.text.toString()
            val adminPassword = etAdminPassword.text.toString()

            // 여기에 로그인 인증 로직 추가
            if (adminId == "admin" && adminPassword == "password") {
                // 로그인 성공 시 다음 화면으로 이동
                val intent = Intent(this, AdminMainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
