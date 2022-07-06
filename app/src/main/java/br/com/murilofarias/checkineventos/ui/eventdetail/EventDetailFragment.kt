package br.com.murilofarias.checkineventos.ui.eventdetail


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.murilofarias.checkineventos.EventApplication
import br.com.murilofarias.checkineventos.R
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.databinding.FragmentEventDetailBinding
import br.com.murilofarias.checkineventos.util.convertLongToDateString
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar


class EventDetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var event: Event
    private lateinit var viewModel: EventDetailViewModel
    private lateinit var checkButton : MaterialButton
    private lateinit var progressBar : ProgressBar



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentEventDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner


        event = EventDetailFragmentArgs.fromBundle(requireArguments()).selectedEvent
        val viewModelFactory = EventDetailViewModelFactory(
            event,
            (requireContext().applicationContext as EventApplication).remoteSource,
            (requireContext().applicationContext as EventApplication).localSource)
        viewModel = ViewModelProvider(
            this, viewModelFactory).get(EventDetailViewModel::class.java)

        binding.viewModel = viewModel

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.checkinButton.setOnClickListener {
                onCheckIn()
        }


        checkButton = binding.checkinButton
        progressBar = binding.spinnerCheckinLoading



        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkButton.text = if(viewModel.isCheckedIn.value!!) "Check-in Realizado" else "Fazer Check-in"

        viewModel.checkInSuccess.observe(viewLifecycleOwner, Observer {
            onCheckInResponseReceived(it)
        })

        viewModel.isCheckedIn.observe(viewLifecycleOwner, Observer {
            it.let{
                checkButton.text = if(it) "Check-in Realizado" else "Fazer Check-in"
            }
        })

    }

    fun onCheckInResponseReceived(checkInSuccess : Boolean){
        checkInSuccess?.let {
            if (it) {
                Snackbar.make(checkButton, "Check-In realizado com Sucesso", Snackbar.LENGTH_LONG)
                    .show()

            } else {
                Snackbar.make(checkButton, "Falha no Check-In", Snackbar.LENGTH_LONG)
                    .setAction("Tentar Novamente") {
                        onCheckIn()
                    }
                    .show()

            }

            progressBar.visibility = View.GONE
            checkButton.visibility = View.VISIBLE
        }
    }


    fun onCheckIn(){

        if(viewModel.isCheckedIn.value!!)
        {
            Snackbar.make(checkButton, "Check-in já foi realizado!", Snackbar.LENGTH_LONG)
                .show()
        }
        else
            doCheckInRequest()

    }

    fun doCheckInRequest(){
        progressBar.visibility = View.VISIBLE
        checkButton.visibility = View.GONE

        viewModel.onCheckIn(event.id)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // We are using switch case because multiple icons can be kept
        when (item.getItemId()) {
            R.id.share_button -> {
                val sharingIntent = Intent(Intent.ACTION_SEND)

                // type of the content to be shared
                sharingIntent.type = "text/plain"

                val uri =
                    "https://maps.google.com/maps?saddr=${event.latitude},${event.longitude}"

                val shareBody = "Titulo: ${event.title}\n\nLocalização: $uri\n\nData: ${convertLongToDateString(event.date)}" +
                        "\n\nPreço: ${event.price}\n\nDescrição: ${event.description}"
                // subject of the content. you can share anything
                val shareSubject = event.title

                // passing body of the content
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

                // passing subject of the content
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
                startActivity(Intent.createChooser(sharingIntent, "Share using"))
            }
            R.id.user_button -> {
                findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToCheckInInputFragment())
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        val eventLocation = LatLng(event.latitude, event.longitude)
        mMap.addMarker(MarkerOptions().position(eventLocation).title(event.title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 12.0f))

    }

}