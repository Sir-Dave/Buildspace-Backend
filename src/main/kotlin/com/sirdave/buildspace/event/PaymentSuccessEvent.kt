package com.sirdave.buildspace.event

import com.sirdave.buildspace.payment.TransactionResponse
import com.sirdave.buildspace.transaction.Transaction

class PaymentSuccessEvent(val transaction: Transaction, val payload: TransactionResponse)