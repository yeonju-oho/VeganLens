import com.example.veganlens.R

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminMainActivity : AppCompatActivity() {

    private lateinit var btnUserManagement: Button
    private lateinit var btnIngredientManagement: Button
    private lateinit var btnSuggestions: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        btnUserManagement = findViewById(R.id.btnUserManagement)
        btnIngredientManagement = findViewById(R.id.btnIngredientManagement)
        btnSuggestions = findViewById(R.id.btnSuggestions)

        btnUserManagement.setOnClickListener {
            // 회원 관리 화면으로 이동
        }

        btnIngredientManagement.setOnClickListener {
            val intent = Intent(this, IngredientManagementActivity::class.java)
            startActivity(intent)
        }

        btnSuggestions.setOnClickListener {
            // 건의사항 관리 화면으로 이동
        }
    }
}
