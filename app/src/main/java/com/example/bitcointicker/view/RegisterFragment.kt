package com.example.bitcointicker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.bitcointicker.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationToLogin()
        registerWithEmailAndPassword(binding.edtMail, binding.edtPassword)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigationToLogin() {
        binding.txtRegisterToLogin.setOnClickListener {
            val actionToLogin = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            Navigation.findNavController(it).navigate(actionToLogin)
        }
    }

    fun registerWithEmailAndPassword(email: EditText, password: EditText) {
        binding.btnRegister.setOnClickListener {
            if (email.text.isEmpty() || password.text.isEmpty())
                Toast.makeText(requireContext(), "Text fields can not be empty", Toast.LENGTH_SHORT)
                    .show()
            else {
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "User has been registered successfully..",
                            Toast.LENGTH_SHORT
                        ).show()
                        val actionToLogin = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                        findNavController().navigate(actionToLogin)
                    }.addOnFailureListener { e ->
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
                    println(e.toString())
                }
            }
        }
    }
}