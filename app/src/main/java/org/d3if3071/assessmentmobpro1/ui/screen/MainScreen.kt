package org.d3if3071.assessmentmobpro1.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3071.assessmentmobpro1.R
import org.d3if3071.assessmentmobpro1.navigation.Screen
import org.d3if3071.assessmentmobpro1.navigation.SetupNavGraph
import org.d3if3071.assessmentmobpro1.ui.theme.AssessmentMobpro1Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {navController.navigate(Screen.About.route)}) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.about_app),
                            tint = MaterialTheme.colorScheme.primary
                        )

                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}
@Composable
fun ScreenContent(modifier: Modifier) {
    val selectedType = rememberSaveable { mutableStateOf("") }
    val berat = rememberSaveable { mutableStateOf("") }
    val hargaPerKg = 5000 // Harga per kg

    var totalHarga by remember { mutableStateOf<Float?>(null) }
    var isInputValid by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.enter_data),
            modifier = Modifier.fillMaxWidth()
        )

        // Radio button for selecting type of waste
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically // Penyesuaian vertical alignment
        ) {
            RadioButton(
                selected = selectedType.value == "Organik",
                onClick = { selectedType.value = "Organik" }
            )
            Text(text = stringResource(id = R.string.organic))
            RadioButton(
                selected = selectedType.value == "Anorganik",
                onClick = { selectedType.value = "Anorganik" }
            )
            Text(text = stringResource(id = R.string.inorganic))
        }

        // OutlinedTextField for berat
        OutlinedTextField(
            value = berat.value,
            onValueChange = { newValue ->
                berat.value = newValue
            },
            label = { Text(text = stringResource(id = R.string.weight_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        // Text for harga per kg (5000)
        Text(
            text = "Harga Per Kg: $hargaPerKg",
            modifier = Modifier.padding(top = 8.dp)
        )

        // Button to submit data
        Button(
            onClick = {
                // Reset input validation
                isInputValid = true

                // Validate input
                if (selectedType.value.isEmpty() || berat.value.isEmpty()) {
                    isInputValid = false
                } else {
                    // Calculate total harga
                    val beratValue = berat.value.toFloatOrNull()
                    if (beratValue != null) {
                        val total = beratValue * hargaPerKg
                        totalHarga = total
                    }
                }
            },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = "Submit")
        }

        // Show total harga if input is valid
        if (isInputValid && totalHarga != null) {
            Text(
                text = "Jenis Sampah: ${selectedType.value}\nTotal Harga: ${totalHarga!!}",
                modifier = Modifier.padding(top = 8.dp)
            )
            Button(
                onClick = {
                    shareData(context, "Jenis Sampah: ${selectedType.value}\nTotal Harga: ${totalHarga!!}")
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.share))
            }
        }

        // Show input validation message if input is not valid
        if (!isInputValid) {
            Text(
                text = "Input tidak valid, mohon isi semua field.",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    val navController = rememberNavController()
    AssessmentMobpro1Theme {
        MainScreen(navController)
    }
}
