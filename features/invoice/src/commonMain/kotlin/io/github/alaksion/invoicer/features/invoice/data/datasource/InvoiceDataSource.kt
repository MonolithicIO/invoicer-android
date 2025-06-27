package io.github.alaksion.invoicer.features.invoice.data.datasource

import io.github.alaksion.invoicer.foundation.network.client.HttpWrapper
import io.github.alaksion.invoicer.features.invoice.data.model.CreateInvoiceRequest
import io.github.alaksion.invoicer.features.invoice.data.model.InvoiceDetailsResponse
import io.github.alaksion.invoicer.features.invoice.data.model.InvoiceListResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.parameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal interface InvoiceDataSource {
    suspend fun getInvoices(
        page: Long,
        limit: Int,
        minIssueDate: String?,
        maxIssueDate: String?,
        minDueDate: String?,
        maxDueDate: String?,
        customerId: String?
    ): InvoiceListResponse

    suspend fun createInvoice(
        payload: CreateInvoiceRequest
    )

    suspend fun getInvoiceDetails(
        invoiceId: String
    ): InvoiceDetailsResponse
}

internal class InvoiceDataSourceImpl(
    private val httpWrapper: HttpWrapper,
    private val dispatcher: CoroutineDispatcher
) : InvoiceDataSource {

    override suspend fun getInvoices(
        page: Long,
        limit: Int,
        minIssueDate: String?,
        maxIssueDate: String?,
        minDueDate: String?,
        maxDueDate: String?,
        customerId: String?
    ): InvoiceListResponse {
        return withContext(dispatcher) {
            httpWrapper.client.get(urlString = "/v1/invoice") {
                parameters {
                    append("page", page.toString())
                    append("limit", limit.toString())
                    minIssueDate?.let { append("minIssueDate", it) }
                    maxIssueDate?.let { append("maxIssueDate", it) }
                    minDueDate?.let { append("minDueDate", it) }
                    maxDueDate?.let { append("maxDueDate", it) }
                    customerId?.let { append("customerId", it) }
                }
            }.body()
        }
    }

    override suspend fun createInvoice(
        payload: CreateInvoiceRequest
    ) {
        withContext(dispatcher) {
            httpWrapper.client.post(
                urlString = "/v1/invoice"
            ) {
                setBody(payload)
            }
        }
    }

    override suspend fun getInvoiceDetails(invoiceId: String): InvoiceDetailsResponse {
        return withContext(dispatcher) {
            httpWrapper.client.get(urlString = "/v1/invoice/$invoiceId").body()
        }
    }
}
