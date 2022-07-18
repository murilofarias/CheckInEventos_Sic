package br.com.murilofarias.checkineventos.ui.checkininput


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.murilofarias.checkineventos.EventApplication
import br.com.murilofarias.checkineventos.R
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.databinding.FragmentCheckInInputBinding
import br.com.murilofarias.checkineventos.util.isUserInfoValid
import com.google.android.material.textfield.TextInputEditText


class CheckInInputFragment : Fragment() {

    private lateinit var userNameEd: TextInputEditText
    private lateinit var userEmailEd: TextInputEditText
    private lateinit var viewModel: CheckInInputViewModel

    private lateinit var localSource : LocalSource


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        val binding = FragmentCheckInInputBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner

        localSource = (requireContext().applicationContext as EventApplication).localSource

        val viewModelFactory = CheckInInputViewModelFactory(
            (requireContext().applicationContext as EventApplication).localSource)

        viewModel = ViewModelProvider(
            this, viewModelFactory).get(CheckInInputViewModel::class.java)

        userNameEd = binding.userNameTiet
        userEmailEd = binding.userEmailTiet

        val saveUserInfoButton = binding.saveUserInfoButton

        saveUserInfoButton.setOnClickListener {
            onSaveUserInfo()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = viewModel.getUser()

        viewModel.user.observe(viewLifecycleOwner) { user ->
            //restores saved user values
            userNameEd.setText( user.name)
            userEmailEd.setText(user.email)
        }

    }

    fun onSaveUserInfo(){
        val userName = userNameEd.text.toString()
        val userEmail = userEmailEd.text.toString()

        if (isUserInfoValid(userName, userEmail)) {

            viewModel.onSaveUserInfo(userName, userEmail)
            findNavController().navigate(CheckInInputFragmentDirections.actionCheckInInputFragmentToEventListFragment())

        }
        else {
            Toast.makeText(activity, getString(R.string.invalid_user_data_message), Toast.LENGTH_LONG)
                .show()
        }
    }



}