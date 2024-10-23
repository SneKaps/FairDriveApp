package com.example.fairdriveapp.components

import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hintTitle: String,
    onPlaceSelected: (String) -> Unit
){
    val textColor = if(isSystemInDarkTheme()) Color.Black else Color.White

    AndroidView(
        factory = { context ->
            AutoCompleteTextView(context).apply {
                hint = "$hintTitle"

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                val autocompleteAdapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line)
                val placesClient = Places.createClient(context)
                val autocompleteSessionToken = AutocompleteSessionToken.newInstance()

                addTextChangedListener { editable ->
                    val query = editable?.toString() ?: " "
                    if(query.isNotEmpty()){
                        val request = FindAutocompletePredictionsRequest.builder()
                            .setSessionToken(autocompleteSessionToken)
                            .setQuery(query)
                            .build()

                        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                            autocompleteAdapter.clear()
                            response.autocompletePredictions.forEach { prediction ->
                                autocompleteAdapter.add(prediction.getFullText(null).toString())
                            }
                            autocompleteAdapter.notifyDataSetChanged()
                        }
                    }
                }
                setAdapter(autocompleteAdapter)

                setOnItemClickListener{ _, _, position, _ ->
                    val selectedPlace = autocompleteAdapter.getItem(position) ?: return@setOnItemClickListener

                    onPlaceSelected(selectedPlace)
                }
            }
        },
        modifier = modifier.fillMaxWidth().padding(16.dp)
    )
}