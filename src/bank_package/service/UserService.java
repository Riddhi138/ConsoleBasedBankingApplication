package bank_package.service;

import bank_package.entity.User;
import bank_package.repository.UserRepository;

import java.util.List;
import java.util.Map;

public class UserService {

        //call method from UserRepo
        private UserRepository userRepository= new UserRepository();

    public void printUser(){
        userRepository.printUsers();
    }

    //return user from this method whatever UserRepo return similarly we will return user from UserService
    //call login method from userRepository
    public User login(String username, String password)
    {
        return userRepository.login(username,password);
    }

    //return userRepo.newCustomer() as a boolean value
    //call newCustomer method from userRepo

    public boolean newCustomer(String username, String password, String contact)
    {
        return userRepository.newCustomer(username, password, contact);
    }

    public Double checkBankBalance(String userId)
    {
        return userRepository.checkBankBalance(userId);
    }

    public User getUser(String userId)
    {
        return userRepository.getUser(userId);
    }

    //Fund transfer transaction
    public boolean transferAmount(String userId, String payeeAccountId, Double amount )
    {
       return userRepository.transferAmount(userId,payeeAccountId,amount);
    }

    public void printTransactions(String userId)
    {
        userRepository.printTransactions(userId);
    }

    public void raiseChequebookRequest(String userId)
    {
        userRepository.raiseChequebookRequest(userId);
    }

    //get all chequebook request
    public Map<String, Boolean> getAllChequebookRequest()
    {
        return userRepository.getAllChequebookRequest();
    }

    // get userId  of who raised chequebook request for chequebook request approval
    public List<String> getUserIdForCheckbookRequest()
    {
        return userRepository.getUserIdForCheckbookRequest();
    }

    //approve cheque book request
    public void approveChequebookRequest(String userId)
    {
        userRepository.approveChequebookRequest(userId);
    }
}
