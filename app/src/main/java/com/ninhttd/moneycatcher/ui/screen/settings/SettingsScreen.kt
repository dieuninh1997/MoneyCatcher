package com.ninhttd.moneycatcher.ui.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ninhttd.moneycatcher.R
import com.ninhttd.moneycatcher.ui.prewiewdata.SettingsUiStatePreviewProvider
import com.ninhttd.moneycatcher.ui.theme.AppTheme

@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onUpdateCurrency = {}
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  uiState: SettingsUiState,
  onNavigateUp: () -> Unit,
  onUpdateCurrency: (Currency) -> Unit,
  modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {

        },
        modifier = modifier.fillMaxSize()
    ) {scaffoldPadding ->

        when{
            uiState.isLoading -> {

            }
            uiState.errorMessage != null -> {

            }
            else -> {
                SettingContent(
                    currency = uiState.currency,
                    modifier = Modifier.padding(scaffoldPadding)
                )
            }
        }
    }

}

@Composable
fun SettingContent(
    currency: Currency,
    modifier: Modifier = Modifier,
){

    Column(
        modifier = modifier.padding(horizontal = 12.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ){
        Text(
            text = stringResource(R.string.settings_group),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )


    }

}

@Preview
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(SettingsUiStatePreviewProvider::class) uiState: SettingsUiState
) {
    AppTheme {
        SettingsScreen(
            uiState = uiState,
            onNavigateUp = {},
            onUpdateCurrency = {},
        )
    }
}