package br.com.murilofarias.checkineventos.ui.eventlist


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.murilofarias.checkineventos.EventApplication
import br.com.murilofarias.checkineventos.R
import br.com.murilofarias.checkineventos.data.source.local.SharedPreferenceStorage
import br.com.murilofarias.checkineventos.data.source.remote.EventApi
import br.com.murilofarias.checkineventos.databinding.FragmentEventListBinding


enum class EventApiStatus { LOADING, ERROR, DONE }

class EventListFragment : Fragment() {


    private lateinit var viewModel: EventListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val binding = FragmentEventListBinding.inflate(inflater)


        binding.lifecycleOwner = this.viewLifecycleOwner

        val viewModelFactory = EventListViewModelFactory(
            (requireContext().applicationContext as EventApplication).remoteSource,
            (requireContext().applicationContext as EventApplication).localSource)
        viewModel = ViewModelProvider(
            this, viewModelFactory).get(EventListViewModel::class.java)

        binding.viewModel = viewModel


        binding.eventList.adapter = EventAdapter(EventAdapter.OnClickListener {
            viewModel.displayEventDetails(it)
        })



        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateToSelectedEvent.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                findNavController().navigate(EventListFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayEventDetailsComplete()
            }
        })

        viewModel.navigateToUserInfo.observe(viewLifecycleOwner, Observer {
            if (it) {
                // Must find the NavController from the Fragment
                findNavController().navigate(EventListFragmentDirections.actionEventListFragmentToCheckInInputFragment())
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayUserInfoComplete()
            }
        })

        viewModel.checkUserInfoSetup()
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