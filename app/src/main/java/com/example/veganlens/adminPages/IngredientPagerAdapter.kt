import com.example.veganlens.R

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class IngredientPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IngredientListFragment.newInstance("계란")
            1 -> IngredientListFragment.newInstance("우유")
            2 -> IngredientListFragment.newInstance("육류")
            3 -> IngredientListFragment.newInstance("생선")
            else -> Fragment()
        }
    }
}
