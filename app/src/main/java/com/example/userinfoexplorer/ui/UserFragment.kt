package com.example.userinfoexplorer.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.userinfoexplorer.MainViewModel
import com.example.userinfoexplorer.R
import com.example.userinfoexplorer.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        val uuid = requireArguments().getString(UUID)!!
        val userForUuid = viewModel.userMap[uuid]

        userForUuid?.let { user ->
            binding.apply {
                userName.text = getString(R.string.user_name, user.name.first, user.name.last)

                loadImage(userForUuid.picture.large)

                email.text = user.email
                email.paint.isUnderlineText = true
                email.setOnClickListener {
                    onEmailClick(user.email)
                }

                birthday.text = user.dob.date.substringBefore('T')

                location.text = getString(
                    R.string.user_location,
                    user.location.street.number,
                    user.location.street.name
                )
                location.paint.isUnderlineText = true
                location.setOnClickListener {
                    onLocationClick("${user.location.street.number} ${user.location.street.name}")
                }

                phoneNumber.text = user.phone
                phoneNumber.paint.isUnderlineText = true
                phoneNumber.setOnClickListener {
                    onPhoneNumberClick(user.phone)
                }

                password.text = user.login.password
            }
        }

        return binding.root
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun onEmailClick(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Log.i(TAG, "activity not resolved")
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun onLocationClick(location: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("geo:0,0?q=$location")
        }

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Log.i(TAG, "activity not resolved")
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun onPhoneNumberClick(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Log.i(TAG, "activity not resolved")
        }
    }

    private fun loadImage(url: String) {
        viewModel.picasso
            .load(url)
            .placeholder(R.drawable.no_image)
            .error(R.drawable.no_image)
            .into(binding.userPicture)
    }

    companion object {
        private const val TAG = "UserFragment"
        private const val UUID = "USER_UUID"
        fun newInstance(uuid: String) = UserFragment().apply {
            arguments = Bundle().apply {
                putString(UUID, uuid)
            }
        }
    }
}