package io.github.monolithic.features.home.presentation.tabs.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import io.github.monolithic.features.home.presentation.tabs.welcome.components.WelcomeActions
import io.github.monolithic.features.home.presentation.tabs.welcome.components.WelcomeTopBar
import io.github.monolithic.invoicer.foundation.designSystem.tokens.Spacing
import io.github.monolithic.invoicer.foundation.navigation.InvoicerScreen
import io.github.monolithic.invoicer.foundation.navigation.args.SelectCompanyIntent

internal object WelcomeTab : Tab {

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 1u,
            title = "",
            icon = null
        )

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<WelcomeTabScreenModel>()
        val state = screenModel.state.collectAsState()
        val navigator = LocalNavigator.current?.parent

        val callbacks = rememberWelcomeCallbacks(
            onInvoiceClick = {
                navigator?.push(
                    ScreenRegistry.get(InvoicerScreen.Invoices.List)
                )
            },
            onCustomerClick = {
                navigator?.push(ScreenRegistry.get(InvoicerScreen.Customer.List))
            },
            onChangeCompanyClick = {
                navigator?.push(
                    ScreenRegistry.get(
                        InvoicerScreen.Company.SelectCompany(
                            SelectCompanyIntent.ChangeCompany
                        )
                    )
                )
            }
        )

        LaunchedEffect(Unit) {
            screenModel.loadData()
        }

        StateContent(
            callbacks = callbacks,
            state = state.value
        )
    }

    @Composable
    fun StateContent(
        state: WelcomeTabState,
        callbacks: WelcomeCallbacks
    ) {
        Scaffold(
            topBar = {
                WelcomeTopBar(
                    companyName = state.companyName,
                    modifier = Modifier.fillMaxWidth(),
                    onChangeClick = callbacks.onChangeCompanyClick,
                )
            }
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(Spacing.medium).padding(scaffoldPadding)
            ) {
                WelcomeActions(
                    onInvoiceClick = callbacks.onInvoiceClick,
                    onCustomerClick = callbacks.onCustomerClick
                )
            }
        }
    }
}
