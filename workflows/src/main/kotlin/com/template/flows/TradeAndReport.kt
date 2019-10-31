package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.template.contracts.HighlyRegulatedContract
import com.template.states.HighlyRegulatedState
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.node.StatesToRecord
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker

@StartableByRPC
@InitiatingFlow
class TradeAndReport(
        val buyer: Party,
        val stateRegulator : Party,
        val nationalRegulatory : Party
) : FlowLogic<Unit>() {
    override  val progressTracker = ProgressTracker()

    @Suspendable
    override fun call() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        //Get notary
        val notary = serviceHub.networkMapCache.notaryIdentities[0]

        //transaction builder
        val transactionBuilder = TransactionBuilder(notary).addOutputState(HighlyRegulatedState(buyer, ourIdentity), HighlyRegulatedContract.ID)
                .addCommand(HighlyRegulatedContract.Commands.Trade(), ourIdentity.owningKey)

        //Sign the transaction
        val signedTransaction = serviceHub.signInitialTransaction(transactionBuilder)

        val sessions = listOf(initiateFlow(buyer), initiateFlow(stateRegulator))

        subFlow(FinalityFlow(signedTransaction, sessions))

        //Distribute the transaction to the regulator manually.
        subFlow(ReportManually(signedTransaction, nationalRegulatory))
    }
}

@InitiatedBy(TradeAndReport::class)
class TradeAndReportResponder(val counterpartySession: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        // Both the buyer and the state regulator record all of the transaction's states using
        // `ReceiveFinalityFlow` with the `ALL_VISIBLE` flag.
        subFlow(ReceiveFinalityFlow(counterpartySession, statesToRecord = StatesToRecord.ALL_VISIBLE))
    }
}