import com.ssu.veganlens.R

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class IngredientListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddIngredient: Button

    companion object {
        private const val ARG_INGREDIENT_TYPE = "ingredient_type"

        fun newInstance(ingredientType: String): IngredientListFragment {
            val fragment = IngredientListFragment()
            val args = Bundle()
            args.putString(ARG_INGREDIENT_TYPE, ingredientType)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ingredient_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        btnAddIngredient = view.findViewById(R.id.btnAddIngredient)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = IngredientAdapter()

        btnAddIngredient.setOnClickListener {
            // 신규 등록 로직 구현
        }

        return view
    }
}
