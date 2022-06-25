package br.com.murilofarias.checkineventos.ui.eventlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.murilofarias.checkineventos.R
import br.com.murilofarias.checkineventos.databinding.FragmentEventListBinding
import br.com.murilofarias.checkineventos.util.isUserInfoValid
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

enum class EventApiStatus { LOADING, ERROR, DONE }

class EventListFragment : Fragment() {

    private val viewModel: EventListViewModel by lazy {
        ViewModelProvider(this).get(EventListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val binding = FragmentEventListBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this.viewLifecycleOwner

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the photosGrid RecyclerView with clickHandler lambda that
        // tells the viewModel when our property is clicked
        binding.eventList.adapter = EventAdapter(EventAdapter.OnClickListener {
            viewModel.displayEventDetails(it)
        })

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedEvent.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                findNavController().navigate(EventListFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayEventDetailsComplete()
            }
        })


        checkUserInfoSetup()
        setHasOptionsMenu(true)
        return binding.root
    }

    fun checkUserInfoSetup(){
            val sharedPreferences = requireContext().getSharedPreferences(
                "MAIN_SHARED",
                Context.MODE_PRIVATE
            )

            val userName = sharedPreferences.getString("USER_NAME", "") ?: ""
            val userEmail = sharedPreferences.getString("USER_EMAIL", "") ?: ""
            if (!isUserInfoValid(userName, userEmail))
                findNavController().navigate(EventListFragmentDirections.actionEventListFragmentToCheckInInputFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // We are using switch case because multiple icons can be kept
        when (item.getItemId()) {
            R.id.user_button -> {
                findNavController().navigate(EventListFragmentDirections.actionEventListFragmentToCheckInInputFragment())
            }

        }
        return super.onOptionsItemSelected(item)
    }

}