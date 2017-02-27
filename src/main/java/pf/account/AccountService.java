package pf.account;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pf.transaction.TransactionEntity;
import pf.transaction.TransactionRepository;
import pf.transaction.TransactionService;
import pf.user.UserEntity;
import pf.user.UserRepository;

import java.util.*;

@Service
public class AccountService {
	

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AccountRepository accountRepo;	
    
	@Autowired
	private TransactionRepository transRepo;	
	
	@Autowired
	private TransactionService transService;


    public AccountService() {
    }


    double usdRate, sarRate;
    public List<AccountEntity> getAccountsTree(String userId, String userEmail) throws Exception {
        String parent = "0";
        usdRate = getUsdRate(userEmail);
        sarRate = getSarRate(userEmail);
        return getAccountsTreeRecursive(userId, parent);
    }

    public double getUsdRate(String userEmail) throws Exception {
        return userRepo.findByEmail(userEmail).getUsd_rate();
    }

    public double getSarRate(String userEmail) throws Exception {
        return userRepo.findByEmail(userEmail).getSar_rate();
    }

    private List<AccountEntity> getAccountsTreeRecursive(String userId, String parent)
            throws Exception {
    	List<AccountEntity> accounts = accountRepo.findByUserAndParentOrderByText(userRepo.findById(userId), parent);
        if (accounts != null) {
            for (AccountEntity acc : accounts) {
                double bal = transService.getAccountBalance(userId, acc.getId());
                acc.setBalance(bal);
                List<AccountEntity> childes = getAccountsTreeRecursive(userId, String.valueOf(acc.getId()));
                acc.setBalance(acc.getBalance() + calcTotalBalance(childes));
                acc.setBalanceFormated(TransactionService.formatMoney(acc.getBalance()));
                acc.getChildren().addAll(childes);
            }
        } else {
//FIXME: Add logging as an aspect
            //LOGGER.warning("Account[] must not be null ever!");
        }
        return accounts;
    }

    /*
     * Inject root tree for the selection of new Account parent
     */
    public List<AccountEntity> getAccountsTreeWithOneRoot(String userId, String userEmail) throws Exception {
        List<AccountEntity> accounts = getAccountsTree(userId, userEmail);
        List<AccountEntity> root = new ArrayList<>(); // one element only
//    	public AccountEntity(String uuid, String parentId, UserEntity user, String name, String description, String type, String currency) {
        UserEntity userEntity = userRepo.findById(userId);
        AccountEntity accountEntity = new AccountEntity("1234", "0", userEntity, "Top Level Account", "", "", "");
//        root[0].setText("Top Level Account");
//        root[0].setParent("0");
//        root[0].getChildren().addAll(Arrays.asList(accounts));
        accountEntity.setChildren(accounts);
        root.add(accountEntity);
        return root;
    }

    public String getAccountPath(String userId, String accId, String accName,
                                 Map<String, AccountEntity> map) throws Exception {
        // Get one level parent
        AccountEntity accountRecord = map.get(accId);
        if (accountRecord == null) {
            accountRecord = accountRepo.findByUser_IdAndId(userId, String.valueOf(accId)); // get
            // parent
            // id
            if (accountRecord == null) {
                throw new Exception("Fatal: Null account for user:" + userId
                        + ",acc:" + accId);
            }
            map.put(accId, accountRecord);
        }

        String path = "";
        if (!accountRecord.getParent().equals("0")) { // It still has parent
            AccountEntity acc2 = map.get(accountRecord.getParent());
            if (acc2 == null) {
                acc2 = accountRepo.findByUserAndId(userRepo.findById(userId), 
                        String.valueOf(accountRecord.getParent()));
                map.put(acc2.getId(), acc2);
            }
            path = acc2.getText() + ":" + accName;
            if (!acc2.getParent().equals("0")) {
                throw new Exception(
                        "[getAccountPath]: Only two layers of Accounts are supported");
            }
        }
        return path;
    }

    public void removeAccount(String userId, String accountId) throws Exception {
    	UserEntity userEntity = userRepo.findById(userId);
    	List<AccountEntity> accountRecords = accountRepo.findByUserAndParentOrderByText(userEntity, accountId);
        if (accountRecords != null && accountRecords.size() > 0) {
            throw new Exception("Child Account Exist! Remove child first.");
        }
        ensureNoTransactionsLinked(userId, accountId);
        AccountEntity accountEntity = accountRepo.findByUserAndId(userEntity, accountId);
        accountRepo.delete(accountEntity);
    }

    // Parent unknown
    public String create(String userId, String name, String description,
                         String type, String currency) throws Exception {
    	UserEntity userEntity = userRepo.findById(userId);
    	List<AccountEntity> accountList = accountRepo.findByUserAndParentAndTypeOrderByText(userEntity, "0", type); //and parent is 0
    	AccountEntity accountRecord = accountList.get(0);
        String parent = accountRecord.getId();
        return createWithParent(userId, name, description, type, parent, currency);
    }

    // The parent is given
    public String createWithParent(String userId, String name, String description,
                                   String type, String parentId, String currency) throws Exception {
        // Ensure there is no brother with the same name
    	UserEntity userEntity = userRepo.findById(userId);
//    	findWithParentIdAndAccountName
    	List<AccountEntity> accs = accountRepo.findByUserAndParentAndTextOrderByText(userEntity, parentId, name);
        if (accs != null && accs.size() > 0) {
            throw new Exception("AccountAlreadyExist");
        }
        String uuid = UUID.randomUUID().toString();
        currency = currency.trim().toLowerCase();
        accountRepo.save(new AccountEntity(uuid, parentId, userEntity, name, description, type, currency));
        return uuid;
    }

    public void updateAccount(String userId, String accountId, String name,
                              String description, String type, String currency) throws Exception {
        // Note: parent can not changed directly, it will be changed per type,
        // this is because the two levels limit
    	UserEntity userEntity = userRepo.findById(userId);
//        //FIXME: you can cascade findByUserUserIdAndParentAndType
//    	List<AccountEntity> accs = accountRepo.findByUserAndParentAndType(userEntity, "0", type);
//        AccountEntity acc = accs.get(0); //I know because of parent = 0, it is only one element returned
//        String parent = acc.getId();
        AccountEntity updateAccount = accountRepo.findByUserAndId(userEntity, accountId);
        updateAccount.setText(name);
        updateAccount.setDescription(description);
        updateAccount.setType(type);
        updateAccount.setCurrency(currency);
//        		new AccountEntity(user, parent,accountId, name, description, type, currency)
        accountRepo.save(updateAccount);
    }

    public List<Map<String, String>> getLeafAccountList(String userId, String type) throws Exception {
        
//    	AccountStore[] accs = accountRepo.findWithAccountType(userId, type);
    	List<AccountEntity> accs = accountRepo.findByUser_IdAndTypeAndParentNotOrderByText(userId, type, "0");
        // Compose to be suitable for the jsp consumption
        List<Map<String, String>> out = new ArrayList<>();
        for (AccountEntity a : accs) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", a.getId());
            map.put("name", a.getText());
            out.add(map);
        }
        return out;
    }

    private void ensureNoTransactionsLinked(String userId, String accountId)
            throws Exception {
//        TransactionStore ta[] = transactionRepo.find(userId, accountId);
    	
    	List<TransactionEntity> ta = transRepo.queryByUserAndAccountOrderByDate(userRepo.findById(userId), accountRepo.findById(accountId));
        if (ta != null && ta.size() > 0) {
            RemoveAccountException exception = new RemoveAccountException();
            exception.setTransCount(ta.size());
            throw exception;
        }
    }

    public AccountEntity getAccount(String userId, String accountId) throws Exception {
        return accountRepo.findByUser_IdAndId(userId, accountId);
    }

    private double calcTotalBalance(List<AccountEntity> accounts) {
        double total = 0;
        for (AccountEntity acc : accounts) {
            double balance = acc.getBalance() * getCurrencyRate(acc.getCurrency(), usdRate, sarRate);
            total += balance;
        }
        return total;
    }

//    FIXME: Needs reimplementation 
    public double getBalanceEgp(AccountEntity accountEntity) {
//    	accountEntity.getBalance() * 
    	
    	return accountEntity.getBalance() * getCurrencyRate(accountEntity.getCurrency(), usdRate, sarRate);
    }

    public double getCurrencyRate(String currency, double usdRate, double sarRate) {
        switch (currency) {
            case AccountEntity.USD:
                return usdRate;
            case AccountEntity.SAR:
                return sarRate;
            default:
                return 1.0;
        }
    }


}
