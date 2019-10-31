package com.template.contracts

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.transactions.LedgerTransaction

class HighlyRegulatedContract : Contract {

    companion object {
        const val ID = "com.template.contracts.HighlyRegulatedContract"
    }

    override fun verify(tx: LedgerTransaction) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    interface Commands : CommandData {
        class Trade : Commands
    }
}