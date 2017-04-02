package pf.account;


import org.springframework.data.repository.CrudRepository;
import pf.user.User;
import java.util.List;

public interface AccountRepository  extends CrudRepository<Account, Long> {

    Account findById(String id);
	
    List<Account> findByUserOrderByText(User user);

    List<Account> findByUserAndParentOrderByText(User user, String parent);

    Account findByUserAndId(User user, String accountId);

    List<Account> findByUserAndTextAndParentOrderByText(User user,
                                                              String text,
                                                              String parent);

    //TODO: Making account type ENUM
    List<Account> findByUserAndTypeOrderByText(User user, String type);


    List<Account> findByUserAndParentAndTextOrderByText(User user,
                                                              String parent,
                                                              String text);
    
    List<Account> findByUserAndParentAndTypeOrderByText(User user, String parent, String type);
    
    //Used to get all leaf accounts without the parent root
    List<Account> findByUser_IdAndTypeAndParentNotOrderByText(String userId, String type, String parent);
    
    Account findByUser_IdAndId(String userId, String accountId);
    
    List<Account> findByUser_IdAndTypeOrderByText(String userId, String type);
   
    Account findByUser_IdAndIdAndParent(String userId, String accountId, String parent);

	Account findByUser_EmailAndId(String string, String accountId);
    
	void deleteByUser_Id(String userId);


    
}
