package com.erica.fintech.MicroMpesaplusAirtimePlatform.service;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.Transaction;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.User;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.Wallet;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.repository.TransactionRepository;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.repository.UserRepository;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class WalletTransactionService {

    private final WalletRepository walletRepo;
    private final TransactionRepository txRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    public WalletTransactionService( WalletRepository walletRepo, TransactionRepository txRepo, UserRepository userRepo, NotificationService notificationService) {
        this.walletRepo = walletRepo;
        this.txRepo = txRepo;
        this.userRepo = userRepo;
        this.notificationService = notificationService;
    }

    private User getUser(String phone) {
        return userRepo.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Wallet getWallet(User user) {
        return walletRepo.findByUserId(user.getId())
                .orElseGet(() -> walletRepo.save(new Wallet(user.getId(), 0)));
    }


    @Transactional
    public void deposit(String phone, double amount) {

        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");

        User user = getUser(phone);
        Wallet wallet = getWallet(user);

        wallet.setBalance(wallet.getBalance() + amount);
        walletRepo.save(wallet);

        Transaction tx = new Transaction();
        tx.setOwnerId(user.getId());
        tx.setSenderId(null);
        tx.setReceiverId(user.getId());
        tx.setAmount(amount);
        tx.setNote("Deposit");
        tx.setBalanceAfter(wallet.getBalance());
        txRepo.save(tx);

        String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/M/yy 'at' h:mma"));
        notificationService.notify(
                user,
                String.format(
                        "Confirmed Ksh %.2f deposit on %s",
                        amount,
                        formattedTime
                ),
                wallet.getBalance()
        );


    }


@Transactional
public void sendMoney(String senderPhone, String receiverPhone, double amount) {

    if (amount <= 0)
        throw new IllegalArgumentException("Invalid amount");

    User sender = getUser(senderPhone);
    User receiver = getUser(receiverPhone);

    Wallet senderWallet = getWallet(sender);
    Wallet receiverWallet = getWallet(receiver);

    if (senderWallet.getBalance() < amount)
        throw new IllegalArgumentException("Insufficient balance");

    senderWallet.setBalance(senderWallet.getBalance() - amount);
    receiverWallet.setBalance(receiverWallet.getBalance() + amount);

    walletRepo.save(senderWallet);
    walletRepo.save(receiverWallet);

    String senderNote =
            "You have sent KES " + amount + " to " + receiver.getFullName();

    String receiverNote =
            "You have received KES " + amount + " from " + sender.getFullName();


    Transaction sendTx = new Transaction();
    sendTx.setOwnerId(sender.getId());
    sendTx.setSenderId(sender.getId());
    sendTx.setReceiverId(receiver.getId());
    sendTx.setAmount(amount);
    sendTx.setNote(senderNote);
    sendTx.setBalanceAfter(senderWallet.getBalance());
    txRepo.save(sendTx);


    Transaction receiveTx = new Transaction();
    receiveTx.setOwnerId(receiver.getId());
    receiveTx.setSenderId(sender.getId());
    receiveTx.setReceiverId(receiver.getId());
    receiveTx.setAmount(amount);
    receiveTx.setNote(receiverNote);
    receiveTx.setBalanceAfter(receiverWallet.getBalance());
    txRepo.save(receiveTx);


    notificationService.notify(
            sender,
            String.format(
                    "Confirmed Ksh %.2f sent to %s %s on %s",
                    amount,
                    receiver.getFullName(),
                    receiver.getPhone(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/M/yy 'at' h:mma"))
            ),
            senderWallet.getBalance()
    );

    notificationService.notify(
            receiver,
            String.format(
                    "Confirmed Ksh %.2f received from %s %s on %s",
                    amount,
                    sender.getFullName(),
                    sender.getPhone(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/M/yy 'at' h:mma"))
            ),
            receiverWallet.getBalance()
    );


}


    @Transactional
    public void buyAirtime(String phone, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");

        User user = getUser(phone);
        Wallet wallet = getWallet(user);

        if (wallet.getBalance() < amount)
            throw new IllegalArgumentException("Insufficient balance");

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepo.save(wallet);

        Transaction tx = new Transaction();
        tx.setOwnerId(user.getId());
        tx.setSenderId(user.getId());
        tx.setReceiverId(null);
        tx.setAmount(amount);
        tx.setNote("Airtime purchase");
        tx.setBalanceAfter(wallet.getBalance());
        txRepo.save(tx);

        notificationService.notify(
                user,
                String.format(
                        "Confirmed Ksh %.2f airtime purchase on %s",
                        amount,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/M/yy 'at' h:mma"))
                ),
                wallet.getBalance()
        );


    }

    public double getAirtimeBalance(String phone) {
        User user = getUser(phone);
        return txRepo.sumAirtimeByUserId(user.getId());
    }

}

