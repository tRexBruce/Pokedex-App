package app.pokedex.pokemondetailscreen.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.pokedex.common.domain.model.PokemonType
import app.pokedex.common.utils.toFeetAndInches
import app.pokedex.common.utils.toLb
import app.pokedex.pokemondetailscreen.domain.model.PokemonDetails
import app.pokedex.pokemonlistscreen.domain.model.Pokemon
import app.pokedex.pokemonlistscreen.presentation.components.PokemonPortrait
import java.util.*

@Composable
fun PokemonDetailCard(pokemonDetails: PokemonDetails) {
    val initialColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(initialColor)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(dominantColor.copy(alpha = 0.35f), dominantColor)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pokemon = Pokemon(
            name = pokemonDetails.name,
            imageUrl = pokemonDetails.sprites!!.front_default,
            number = pokemonDetails.id
        )

        PokemonPortrait(
            pokemon = pokemon,
            modifier = Modifier.size(180.dp),
            imageScale = 1.4f,
            onMainColorExtracted = {
                dominantColor = it
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = pokemonDetails.name.replaceFirstChar {
                it.titlecase(Locale.getDefault())
            },
            style = MaterialTheme.typography.h2
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(16.dp))
            pokemonDetails.types.forEach {
                Surface(shape = RoundedCornerShape(12.dp)) {

                    // App crashes if using .first() sometimes
                    val types = PokemonType.values().filter { pokemonType ->
                        pokemonType.name == it.type.name.uppercase()
                    }

                    if (types.isNotEmpty()) {
                        val type = types[0]
                        Row(
                            modifier = Modifier
                                .defaultMinSize(minWidth = 96.dp)
                                .background(PokemonType.getCorrespondingColor(type))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Icon(
                                painter = painterResource(id = PokemonType.getCorrespondingIcon(type)),
                                contentDescription = type.name
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = type.name)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        Row {
            Text(
                text = "WEIGHT: ",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${pokemonDetails.weight.toLb()} lbs",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
        }

        Row {
            Text(
                text = "HEIGHT: ",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = pokemonDetails.height.toFeetAndInches(),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
        }

        // TODO: 1/18/22 Update Pokemon stats UI
        pokemonDetails.stats.forEach {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    text = "${it.stat.name.uppercase()}: ",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${it.base_stat}",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}