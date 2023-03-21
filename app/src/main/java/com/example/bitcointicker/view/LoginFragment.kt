package com.example.bitcointicker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.bitcointicker.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser!=null)
            navigationToCoinsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationToRegister()
        loginWithEmailAndPassword(binding.edtMail,binding.edtPassword)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigationToRegister() {
        binding.txtLoginToRegister.setOnClickListener {
            val actionToRegister = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(actionToRegister)
        }
    }

    fun loginWithEmailAndPassword(email: EditText, password: EditText) {
        binding.btnLogin.setOnClickListener {
            if (email.text.isEmpty() || password.text.isEmpty())
                Toast.makeText(requireContext(), "Text fields can not be empty!!", Toast.LENGTH_SHORT)
                    .show()
            else {
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnSuccessListener {
                        navigationToCoinsFragment()
                    }.addOnFailureListener { e ->
                        Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }



    fun navigationToCoinsFragment() {
        val actionToCoins = LoginFragmentDirections.actionLoginFragmentToCoinsFragment()
        findNavController().navigate(actionToCoins)
    }
}