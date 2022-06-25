package br.com.murilofarias.checkineventos.ui.checkininput

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.murilofarias.checkineventos.R
import br.com.murilofarias.checkineventos.util.isUserInfoValid
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class CheckInInputFragment : Fragment() {

    private lateinit var userNameEd: TextInputEditText
    private lateinit var userEmailEd: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val layout = inflater.inflate(R.layout.fragment_check_in_input, container, false)

        userNameEd =
            layout.findViewById(R.id.user_name_tiet)
        userEmailEd =
            layout.findViewById(R.id.user_email_tiet)

        val saveUserInfoButton = layout.findViewById<MaterialButton>(R.id.save_user_info_button)

        saveUserInfoButton.setOnClickListener {
            onSaveUserInfo()
        }


        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(
            "MAIN_SHARED",
            Context.MODE_PRIVATE
        )

        //restores saved user values
        userNameEd.setText( sharedPreferences.getString("USER_NAME", ""))
        userEmailEd.setText(sharedPreferences.getString("USER_EMAIL", ""))


    }

    fun onSaveUserInfo(){
        val userName = userNameEd.text.toString()
        val userEmail = userEmailEd.text.toString()

        if (isUserInfoValid(userName, userEmail)) {
            val sharedPreferences = requireContext().getSharedPreferences(
                "MAIN_SHARED",
                Context.MODE_PRIVATE
            )

            val editor = sharedPreferences.edit()

            editor.putString("USER_NAME", userName)
            editor.putString("USER_EMAIL", userEmail)
            editor.apply()

            findNavController().navigate(CheckInInputFragmentDirections.actionCheckInInputFragmentToEventListFragment())
        }
        else {
            Toast.makeText(activity, "Há Campos Obrigatórios Pendentes!", Toast.LENGTH_LONG)
                .show()
        }
    }



}