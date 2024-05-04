package RecyclerViewHelper

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import raul.ochoa.crudraul.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.txtCrad)
}