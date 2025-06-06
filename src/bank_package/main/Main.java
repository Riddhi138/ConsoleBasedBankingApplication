package bank_package.main;
import bank_package.entity.User;
import bank_package.service.UserService;

import java.sql.SQLOutput;
import java.util.*;
import java.lang.Thread;

public class Main {
   static Main main = new Main();
   static UserService userService= new UserService();
   private static Scanner sc = new Scanner(System.in);

   // throws expection because we used threa.sleep() to print msg after 5 sec in initCustomer method
    public static void main(String[] args ) throws InterruptedException {

        while (true) {
            System.out.println("Enter your Username: ");
            String username = sc.next();
            System.out.println("Enter your Password: ");
            String password = sc.next();

            //call the login() method from userService and create obj of User model class
            User user = userService.login(username, password);
            if (user != null && user.getRole().equals("admin")) {
                main.initAdmin();
            } else if (user != null && user.getRole().equals("user")) {
                main.initCustomer(user);

            } else {
                System.out.println("Login Failed.");
            }
        }

    }

    //to differentiate the user and admin

    private void initAdmin()                                    //if login admin call this method
    {
        boolean flag = true;                                    // for not letting admin automatically logged out
        String userId="";
        while(flag){
            System.out.println("----------------------------------------------");
            System.out.println("1.Exit / Logout.");                // if admin logged-in option for log out
            System.out.println("2. Create a Customer Account.");
            System.out.println("3. See ALl Transactions.");
            System.out.println("4. Check Bank Balance of user.");
            System.out.println("5. Approve Chequebook Request.");
            System.out.println("----------------------------------------------");

            int selected_option= sc.nextInt();

            switch(selected_option)                                //to select options
            {
                case 1:                                            //Logic for logged out
                    flag= false;
                    System.out.println("You have Successfully Logged-Out.");
                    break;

                case 2:
                   main.newCustomer(); ;   //Logic for adding customer
                    break;
                case 3:
                    System.out.println("Enter Customer Id: ");  // Logic for transaction view for admin
                    userId = sc.next();
                    printtransactions(userId );
                    break;

                case 4:
                    System.out.println("Enter Customer Id: ");  // Logic for bank balance view for admin of user
                     userId = sc.next();
                     Double accountBalance=checkBankBalance(userId);
                     System.out.println("Account Balance of "+userId+" is : "+accountBalance);
                     break;
                case 5:
                    //to get all the list of users who raised a chequebook request
                    List<String> userIds= getUserIdForCheckbookRequest();
                    System.out.println("User Ids of pending approval of chequebook: ");
                    System.out.println("Select the user id from below :");
                    System.out.println(userIds);
                    userId = sc.next();

                    //call method of approve cheque book
                    approveChequebookRequest(userId);
                    System.out.println("Chequebook Request is Approved");


                    break;
                default:
                    System.out.println("Wrong Option choice");      //wrong option choice
            }

        }

    }

    private void newCustomer()                                  //Credential for new user
    {
        System.out.println("Enter Username: ");
        String username= sc.next();
        System.out.println("Enter Password: ");
        String password=sc.next();
        System.out.println("Enter Contact Number: ");
        String contact= sc.next();

       boolean result=  userService.newCustomer(username, password, contact);

       // cheack if the account is new account store it in set and print following msg

       if(result)
       {
           System.out.println("Customer Account is Created.");
       }
       else {
           System.out.println("Fail to create Customer Account.");
       }

    }

    //if login as customer call this method
    //Take the User class obj user as parameter to fetch the username from Set in userRepo
    // throws exception because we used thread.sleep() to print message after 5 sec in case 5

    private void initCustomer(User user) throws InterruptedException {
        boolean flag= true;
        while(flag) {
            System.out.println("______________________________________");
            System.out.println("1. Exit / logout");
            System.out.println("2. Check Bank Account Balance.");
            System.out.println("3. Transfer Fund.");
            System.out.println("4. See all Transactions.");
            System.out.println("5. Raise Chequebook request.");
            System.out.println("_____________________________________");
            int selected_option= sc.nextInt();
            switch(selected_option)                                //to select options
            {
                case 1:                                            //Logic for logged out
                    flag= false;
                    System.out.println("You have Successfully Logged-Out.");
                    break;

                case 2:                                            //Logic for checking acc balance
                   Double balance= main.checkBankBalance(user.getUsername());
                   if(balance!=null)
                   {
                       System.out.println("Your Bank Balance : "+balance);
                   }
                   else {
                       System.out.println("Bank Balance Display Fail: Check Your Username or Password.");
                   }
                    break;

                case 3:                                            //Logic for fund transfer
                    main.fundTransfer(user);
                    break;

                case 4:
                    main.printtransactions(user.getUsername());     //logic for printing transaction
                    break;

                case 5:
                    //logic to raise chequebook

                    String userId= user.getUsername();

                    //once a request is raised it should not allow to raise again until approved or decline by user

                    Map<String, Boolean> map = getAllChequebookRequest();

                    // if the userId is present in Map and the key associated to value is true then
                    if(map.containsKey(userId) && map.get(userId))
                    {
                        System.out.println("You have already raised a request and it is approved.");
                    }  else if (map.containsKey(userId) && !map.get(userId)) {
                        System.out.println("You have already raised a request and is pending for approval.");
                    }
                    else
                    {
                        System.out.println("Raising a chequebook request....");
                        Thread.sleep(5000);
                        raiseChequebookRequest(userId);
                        System.out.println("Request Raised Successfully.");
                    }

                    break;

                default:
                    System.out.println("Wrong Option choice");      //wrong option choice
            }
        }
    }

    // get userId  of who raised chequebook request for chequebook request approval
    private List<String> getUserIdForCheckbookRequest()
    {
       return userService.getUserIdForCheckbookRequest();
    }

    //approve cheque book request
    private void approveChequebookRequest(String userId)
    {
        userService.approveChequebookRequest(userId);
    }

    //method for Fund Transfer
    private void fundTransfer(User userDetails)
    {
        System.out.println("Enter the payee user id: ");
        String payeeAccountId= sc.next();

        User user = getUser(payeeAccountId);    // checks account validity
        if(user!=null)
        {
            System.out.println("Enter amount to transfer: ");    // transfer amount?
            Double amount = sc.nextDouble();

            //amount to transfer must not exceed bank balance

            Double userAccountBalance= checkBankBalance(userDetails.getUsername());  //get amt of particular user
            if(userAccountBalance>=amount)
            {
                //Logic for money transaction
                boolean result= userService.transferAmount(userDetails.getUsername(),payeeAccountId,amount);

                if(result)
                {
                    System.out.println("Amount transferred Successfully");
                }
                else {
                    System.out.println("Amount Transfer Failed.");
                }

            }
            else
            {
                System.out.println("Insufficient Bank Balance."+userAccountBalance);
            }
        }
        else{
            System.out.println("Enter the Valid User ID.");
        }
    }

    //get all chequebook request
    private Map<String, Boolean> getAllChequebookRequest()
    {
        return userService.getAllChequebookRequest();
    }

    //method to raise chequebook request ; user
    private void raiseChequebookRequest(String userId)
    {
        userService.raiseChequebookRequest(userId);
    }

    // printing transaction history of user
    private void printtransactions(String userId)
    {
        userService.printTransactions(userId);
    }

    //method to verify the payee account is valid or not
    private User getUser(String userId)
    {
        return userService.getUser(userId);
    }

    //method to check bank balance
    private Double checkBankBalance( String userId)
    {
       return  userService.checkBankBalance(userId);
    }
}
