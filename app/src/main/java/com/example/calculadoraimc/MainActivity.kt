package com.example.calculadoraimc

import android.R.attr.text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.ui.theme.CalculadoraIMCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraIMCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IMCScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IMCScreen(modifier: Modifier = Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var alturaInput by remember {
        mutableStateOf("")
    }
    var pesoInput by remember {
        mutableStateOf("")
    }
    var isCardVisible by remember {
        mutableStateOf(false)
    }
    var imc: Double by remember {
        mutableStateOf(0.00)
    }
    var statusIMC by remember {
        mutableStateOf("")
    }

    val corDoCardID = when (statusIMC) {
        "ABAIXO DO PESO" -> R.color.cor_obesidade_e_abaixo_peso
        "PESO IDEAL" -> R.color.cor_peso_ideal
        "LEVEMENTE ACIMA DO PESO" -> R.color.cor_levemente_acima_peso
        "OBESIDADE GRAU I" -> R.color.cor_obesidade_e_abaixo_peso
        "OBESIDADE GRAU II" -> R.color.cor_obesidade_e_abaixo_peso
        "OBESIDADE GRAU III" -> R.color.cor_obesidade_e_abaixo_peso
        else -> R.color.white
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .focusTarget()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                })
            }
    ) {
//        ---- header ----
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(color = colorResource(id = R.color.cor_app)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.bmi),
                contentDescription = "Logo APP",
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .size(60.dp)
            )

            Text(
                text = "Calculadora IMC",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

//        ---- form ----
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .offset(y = (-30).dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF9F6F6)
                ),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 25.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )

                    Text(
                        text = "Seus dados",
                        color = colorResource(id = R.color.cor_app),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    OutlinedTextField(
                        value = alturaInput,
                        onValueChange = {novoValor ->
                            val filtrado = novoValor.filter { it.isDigit() }
                            if (filtrado.length <= 3)
                                alturaInput = novoValor
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(
                                text = "Altura",
                                color = colorResource(id = R.color.cor_app)
                            )
                        },
                        trailingIcon = {
                            Text(
                                text = "CM",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )
                        },
                        shape = RoundedCornerShape(
                            15.dp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.cor_app),
                            unfocusedBorderColor = colorResource(id = R.color.cor_app)
                        )
                    )

                    OutlinedTextField(
                        value = pesoInput,
                        onValueChange = { novoValor ->
                            val normalizado = novoValor.replace(",", ".")
                            if (normalizado.count { it == '.' } <= 1 && normalizado.all { it.isDigit() || it == '.' }) {
                                val partes = normalizado.split(".")
                                val casasDecimais = if (partes.size > 1) partes[1].length else 0
                                if (casasDecimais <= 2)
                                    pesoInput = novoValor
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(
                                text = "Peso",
                                color = colorResource(id = R.color.cor_app)
                            )
                        },
                        trailingIcon = {
                            Text(
                                text = "KG",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )
                        },
                        shape = RoundedCornerShape(
                            15.dp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.cor_app),
                            unfocusedBorderColor = colorResource(id = R.color.cor_app)
                        )
                    )

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus(force = true)

                            val alturaInfo = alturaInput.toDoubleOrNull() ?: 0.00
                            val pesoInfo = pesoInput.toDoubleOrNull() ?: 0.00

                            var alturaMetro = alturaInfo / 100

                            imc = pesoInfo / (alturaMetro * alturaMetro)

                            if (imc < 18.5) {
                                statusIMC = "ABAIXO DO PESO"
                            } else if (imc > 18.5 && imc < 25) {
                                statusIMC = "PESO IDEAL"
                            } else if (imc >= 25 && imc < 30) {
                                statusIMC = "LEVEMENTE ACIMA DO PESO"
                            } else if (imc >= 30 && imc < 35) {
                                statusIMC = "OBESIDADE GRAU I"
                            } else if (imc >= 35 && imc < 40) {
                                statusIMC = "OBESIDADE GRAU II"
                            } else {
                                statusIMC = "OBESIDADE GRAU III"
                            }

                            isCardVisible = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.cor_app)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(
                            size = 30.dp
                        )
                    ) {
                        Text(
                            text = "CALCULAR",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus(force = true)

                            alturaInput = ""
                            pesoInput = ""

                            isCardVisible = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.cor_btn_limpar)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(
                            size = 30.dp
                        )
                    ) {
                        Text(
                            text = "LIMPAR",
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

//        ---- card resultado ----
        AnimatedVisibility(
            visible = isCardVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = corDoCardID)
                ),
                elevation = CardDefaults.cardElevation(2.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = String.format("%.2f", imc),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                    Text(
                        text = "$statusIMC",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                }
            }
        }
    }
}