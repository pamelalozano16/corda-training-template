package net.corda.training.state;

import net.corda.core.contracts.Amount;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.Party;
import net.corda.core.identity.AbstractParty;

import java.util.*;
import com.google.common.collect.ImmutableList;
import net.corda.core.serialization.ConstructorForDeserialization;
import net.corda.finance.Currencies;
import org.checkerframework.common.aliasing.qual.Unique;
import org.jetbrains.annotations.NotNull;

/**
 * This is where you'll add the definition of your state object. Look at the unit tests in [IOUStateTests] for
 * instructions on how to complete the [IOUState] class.
 *
 */
public class IOUState implements LinearState {
    public Amount<Currency> amount;
    public Party lender;
    public Party borrower;
    public Amount<Currency> paid;
    public List<AbstractParty> participants;
    public UniqueIdentifier linearId;

    @ConstructorForDeserialization
    private IOUState(Amount<Currency> amount, Party lender, Party borrower, Amount<Currency> paid, UniqueIdentifier linearId){
        this.amount = amount;
        this.lender = lender;
        this.borrower = borrower;
        this.paid = paid;
        this.linearId = linearId;
    }

    public IOUState(Amount<Currency> amount, Party lender, Party borrower) {
        this(amount, lender, borrower, new Amount<>(0, amount.getToken()), new UniqueIdentifier());

    }

    public IOUState pay(Amount amount){
        Amount<Currency> newPaid = paid.plus(amount);
        return new IOUState(amount, lender, borrower, newPaid, linearId);
    }

    public IOUState withNewLender(Party newLender){
        return new IOUState(amount, newLender, borrower, paid, linearId);
    }

    public Amount<Currency> getPaid(){
        return paid;
    }

    public Party getLender(){
        return lender;
    }



    /**
     *  This method will return a list of the nodes which can "use" this state in a valid transaction. In this case, the
     *  lender or the borrower.
     */
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(lender, borrower);
    }

    @Override
    public UniqueIdentifier getLinearId() {
        return linearId;
    }
}