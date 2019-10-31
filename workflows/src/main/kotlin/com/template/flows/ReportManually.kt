package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.FlowSession
import net.corda.core.flows.InitiatedBy
import net.corda.core.flows.InitiatingFlow
import net.corda.core.identity.Party
import net.corda.core.node.StatesToRecord
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker
import net.corda.core.utilities.unwrap

@InitiatingFlow
class ReportManually(val signedTransaction : SignedTransaction,
                     val regulator: Party) : FlowLogic<Unit>() {
    override  val progressTracker = ProgressTracker()

    @Suspendable
    override fun call() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val session = initiateFlow(regulator)
        session.send(signedTransaction)
    }
}

@InitiatedBy(ReportManually::class)
class ReportManuallyResponder(val counterPartySession : FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val signedTransaction = counterPartySession.receive<SignedTransaction>().unwrap { it}

        //use all_visible flag
        serviceHub.recordTransactions(StatesToRecord.ALL_VISIBLE, listOf(signedTransaction))
    }


}