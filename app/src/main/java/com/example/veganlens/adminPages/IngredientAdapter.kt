import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veganlens.R

class IngredientAdapter : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    // 예제 데이터, 실제 데이터는 ViewModel이나 다른 방법을 통해 제공할 수 있음
    private val ingredients = listOf("계란", "우유", "육류", "생선")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvIngredientName: TextView = itemView.findViewById(R.id.tvIngredientName)

        fun bind(ingredient: String) {
            tvIngredientName.text = ingredient
        }
    }
}
