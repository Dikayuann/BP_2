import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ewallet.R
import com.example.ewallet.TransaksiModel

// Ensure this import is correct

class TransactionAdapter(private val transactions: List<TransaksiModel>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNominal: TextView = itemView.findViewById(R.id.textViewNominal)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val viewMenu: ImageView = itemView.findViewById(R.id.ViewMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false) // Ensure the layout file name is correct
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.textViewNominal.text = "${transaction.amount}"
        holder.textViewDescription.text = transaction.description
        holder.viewMenu.setImageResource(R.drawable.baseline_monetization_on_50_b) // Update the image resource as needed
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}
