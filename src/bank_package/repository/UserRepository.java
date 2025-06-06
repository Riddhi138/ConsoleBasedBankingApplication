package bank_package.repository;
import bank_package.entity.Transaction;
import bank_package.entity.User;
import bank_package.main.Main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.*;

public class UserRepository {
    private static Set<User> users = new HashSet<>();
    //Create a collection of transaction using arraylist bcz we need duplicate entry of transactions here
    private static List<Transaction> transactions= new ArrayList<>();
    Map<String,Boolean> chequeBookRequest = new HashMap<>();        //collection for chequebook request


    // add users previous based as we need them to start the application
    static {
        User user1= new User("admin","admin","123456","admin",0.0);
        User user2= new User( "user2","user2","654321","user",1000.0);
        User user3= new User( "user3","user3","654721","user",2000.0);
        User user4 = new User( "user4","user4","654720","user",2000.0);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        
    }

    // Raise chequebook for user
    public void raiseChequebookRequest(String userId)
    {
        chequeBookRequest.put(userId,false);                    //request raised but pending for approval
    }

    //get all chequebook request
    public Map<String, Boolean> getAllChequebookRequest()
    {
        return chequeBookRequest;
    }

    // get userId  of who raised chequebook request for chequebook request approval
    public List<String> getUserIdForCheckbookRequest()
    {
        //filter out all the request that are approval pending from the map and return those userId from the map
        //Declare a list of string
        List<String> userIds = new ArrayList<>();

        // for loop whereentry of type Map stores the request returned ny .entrySet()
        for (Map.Entry<String,Boolean> entry : chequeBookRequest.entrySet())
        {
            //choose that entry value whose value in Map is false means approval pending
            if(!entry.getValue())
            {
                userIds.add(entry.getKey());        //add the entry in list userIds (those whose req pending)
            }
        }
        return userIds;
    }

    //approve cheque book request
    public void approveChequebookRequest(String userId)
    {
        // check if request contains key
        if(chequeBookRequest.containsKey(userId))
        {
            chequeBookRequest.put(userId,true);   // if map has the userid with pending req approve its chequebook
        }
    }

    //Fund transfer transaction
    public boolean transferAmount(String userId, String payeeAccountId, Double amount )
    {
        boolean isDebit= debit(userId,amount,payeeAccountId);
        boolean isCredit= credit(payeeAccountId,amount,userId);
        return isDebit && isCredit;

    }

    // method to Debit money from payer account
    private boolean debit(String userId, Double amount, String payeeAccountId)
    {
        User user = getUser(userId);
        Double accountBalance= user.getAccountBalance();

        users.remove(user);

        Double finalBalance= accountBalance - amount;
        user.setAccountBalance(finalBalance);         //setAccountbalance is a setter in User class of entity

        //create transaction for debit operations
        Transaction transaction = new Transaction(
                LocalDate.now(),
                payeeAccountId,
                amount,
                "Debit",
                accountBalance,
                finalBalance,
                userId              //to fetch particular account transaction only
        );

        System.out.println(transaction);
        transactions.add(transaction);
        return  users.add(user);
    }

    // method to Credit money from payee account
    private boolean credit(String payeeAccountId, Double amount,String userId)
    {
        User user = getUser(payeeAccountId);
        Double accountBalance= user.getAccountBalance();

        users.remove(user);
        Double finalBalance= accountBalance + amount;
        user.setAccountBalance(finalBalance);         //setAccountbalance is a setter in User class of entity

        //create transaction for credit operations
        Transaction transaction = new Transaction(
                LocalDate.now(),
                userId,                             //to get this (third party user id ) basically payeeUserId
                amount,
                "Credit",
                accountBalance,
                finalBalance,
                payeeAccountId                        //to fetch particular account transaction only
        );

        System.out.println(transaction);
        transactions.add(transaction);
         return users.add(user);
    }

    //printing transactions of user
    public void printTransactions(String userId)
    {
        //logic to print transaction filter out transaction

        List<Transaction> filteredTransactions = transactions.stream().filter(transaction -> transaction.getTransactionPerformedBy().equals(userId)).collect(Collectors.toList());

        // use for each loop to print transaction
        for (Transaction t: filteredTransactions)
        {
            System.out.println("____________________________________________________________________________________________________________");
            System.out.println("Date\t     User Id\t    Transaction Amount\t    Transaction Type\t    Initial Balance\t    Final Balance");
            System.out.println("_______________________________________________________________________________________________________________");
            System.out.println(t.getTransactionDate()
                    +"\t    "+t.getTransactionUserId()
                    +"\t        "+t.getTransactionAmount()
                    +"\t              "+t.getTransactionType()
                    +"\t                   "+t.getInitalBalance()
                    +"\t                "+t.getFinalBalance()
            );
        }
            System.out.println("____________________________________________________________________________________________________________");

    }




    //payee account method
    //match the userId and the username from set if present it is valid
    public User getUser(String userId)
    {
        // Logic for verification of account

        List<User> result=users.stream().filter(user -> user.getUsername().equals(userId))
                          .collect(Collectors.toList());

        if(!result.isEmpty())
        {
            return result.get(0);
        }
        else {
            return null;
        }

    }

    //check bank balance ; user side
    //use stream api to iterate over the bank balance of users in Set & filter out the username

    public Double checkBankBalance( String userId)
    {
       List<User> result= users.stream().filter(user -> user.getUsername().equals(userId))
                          .collect(Collectors.toList());

       if(!result.isEmpty())
       {
           return result.get(0).getAccountBalance();
       }
       else{
           return null;
       }
    }

    public void printUsers() {
        System.out.println(users);
    }

    //to check user name and password is valid or not

    public User login(String username, String password)
    {
        List<User> finallist= users.stream()
            .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
            .collect(Collectors.toList());

        if(!finallist.isEmpty())
        {
            return finallist.get(0);

        }
        else {
            return null;
        }
    }

    //for new user account

    public boolean newCustomer(String username, String password, String contact)
    {
        User user= new User(username, password, contact, "user", 5000.0);

        //add user to the Set<> users
        return users.add(user);
    }

}
