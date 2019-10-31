package com.template.states

import com.template.contracts.HighlyRegulatedContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party

@BelongsToContract(HighlyRegulatedContract::class)
data class HighlyRegulatedState(val buyer: Party, val seller: Party): ContractState {
    override val participants: List<AbstractParty>
        //get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        get() = listOf(buyer, seller)
}