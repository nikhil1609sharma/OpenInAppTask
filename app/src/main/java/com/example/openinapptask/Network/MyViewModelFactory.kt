package amr_handheld.network



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.openinapptask.viewmodels.MainViewModel
import com.example.openinapptask.repo.Repository

class MyViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(this.repository) as T
        }
        else{
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}