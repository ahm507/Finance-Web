package pf.account;


import org.springframework.data.repository.CrudRepository;
import pf.user.UserEntity;
import java.util.List;

public interface AccountRepository  extends CrudRepository<AccountEntity, Long> {

    AccountEntity findById(String id);
	
    List<AccountEntity> findByUserOrderByText(UserEntity user);

    List<AccountEntity> findByUserAndParentOrderByText(UserEntity user, String parent);

    AccountEntity findByUserAndId(UserEntity user, String accountId);

    List<AccountEntity> findByUserAndTextAndParentOrderByText(UserEntity user,
                                                              String text,
                                                              String parent);

    //TODO: Making account type ENUM
    List<AccountEntity> findByUserAndTypeOrderByText(UserEntity user, String type);


    List<AccountEntity> findByUserAndParentAndTextOrderByText(UserEntity user,
                                                              String parent,
                                                              String text);
    
    List<AccountEntity> findByUserAndParentAndTypeOrderByText(UserEntity user, String parent, String type);
    
    //Used to get all leaf accounts without the parent root
    List<AccountEntity> findByUser_IdAndTypeAndParentNotOrderByText(String userId, String type, String parent);
    
    AccountEntity findByUser_IdAndId(String userId, String accountId);
    
    List<AccountEntity> findByUser_IdAndTypeOrderByText(String userId, String type);
   
    AccountEntity findByUser_IdAndIdAndParent(String userId, String accountId, String parent);

	AccountEntity findByUser_EmailAndId(String string, String accountId);
    
	void deleteByUser_Id(String userId);


    
}
