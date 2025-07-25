package io.github.monolithic.invoicer.features.company.data.repository

import io.github.monolithic.invoicer.features.company.data.datasource.PayAccountRemoteDataSource
import io.github.monolithic.invoicer.features.company.data.model.UpdatePayAccountRequest
import io.github.monolithic.invoicer.features.company.domain.model.UpdatePayAccountModel
import io.github.monolithic.invoicer.features.company.domain.repository.PayAccountRepository

internal class PayAccountRepositoryImpl(
    private val dataSource: PayAccountRemoteDataSource
) : PayAccountRepository {

    override suspend fun updatePayAccount(
        request: UpdatePayAccountModel,
        companyId: String,
        payAccountId: String
    ) {
        dataSource.updatePayAccount(
            request = UpdatePayAccountRequest(
                bankName = request.bankName,
                bankAddress = request.bankAddress,
                swift = request.swift,
                iban = request.iban
            ),
            companyId = companyId,
            payAccountId = payAccountId
        )
    }

    override suspend fun deletePayAccount(companyId: String, payAccountId: String) {
        dataSource.deletePayAccount(
            companyId = companyId,
            payAccountId = payAccountId
        )
    }
}
