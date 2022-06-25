package br.com.murilofarias.checkineventos.ui.eventdetail

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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

    private var isCheckIn = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentEventDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner

        event = EventDetailFragmentArgs.fromBundle(requireArguments()).selectedEvent
        val viewModelFactory = EventDetailViewModelFactory(event, application)
        viewModel = ViewModelProvider(
            this, viewModelFactory).get(EventDetailViewModel::class.java)

        binding.viewModel = viewModel

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.checkinButton.setOnClickListener {
            if(isCheckIn)
                onCheckIn()
            else
                onCheckOut()
        }

        checkButton = binding.checkinButton
        progressBar = binding.spinnerCheckinLoading

        viewModel.checkInSuccess.observe(viewLifecycleOwner, Observer {
            onCheckInResponseReceived(it)
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    fun onCheckInResponseReceived(checkInSuccess : Boolean){
        checkInSuccess?.let {
            if (it) {
                Snackbar.make(checkButton, "Check-In realizado com Sucesso", Snackbar.LENGTH_LONG)
                    .show()

                val sharedPreferences = requireContext().getSharedPreferences(
                    "MAIN_SHARED",
                    Context.MODE_PRIVATE
                )

                var userChecks = sharedPreferences.getString("USER_CHECKS", "") ?: ""
                userChecks = "$userChecks;${event.id}"

                val editor = sharedPreferences.edit()
                editor.putString("USER_CHECKS", userChecks)
                editor.apply()

                checkButton.text = "Fazer Check-out"
                isCheckIn = false
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(
            "MAIN_SHARED",
            Context.MODE_PRIVATE
        )

        var userChecks = sharedPreferences.getString("USER_CHECKS", "") ?: ""

        isCheckIn = !userChecks.split(";").contains(event.id)
        checkButton.text = if(isCheckIn) "Fazer Check-in" else "Fazer Check-out"

    }

    fun onCheckOut(){
        val sharedPreferences = requireContext().getSharedPreferences(
            "MAIN_SHARED",
            Context.MODE_PRIVATE
        )

        val userChecks = sharedPreferences.getString("USER_CHECKS", "") ?: ""
        val userChecksMutable = userChecks.split(";").toMutableList()
        if(userChecksMutable.remove(event.id)) {
            checkButton.text = "Fazer Check-in"
            isCheckIn = true
            val editor = sharedPreferences.edit()
            editor.putString("USER_CHECKS", userChecksMutable.joinToString(";"))
            editor.apply()
        }
        else {
            Snackbar.make(checkButton, "Falha no Check-out", Snackbar.LENGTH_LONG)
                .show()
        }

    }

    fun onCheckIn(){
        progressBar.visibility = View.VISIBLE
        checkButton.visibility = View.GONE

        val sharedPreferences = requireContext().getSharedPreferences(
            "MAIN_SHARED",
            Context.MODE_PRIVATE
        )

        val userName = sharedPreferences.getString("USER_NAME", "") ?: ""
        val userEmail = sharedPreferences.getString("USER_EMAIL", "") ?: ""
        viewModel.onCheckIn(userName, userEmail, event.id)

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