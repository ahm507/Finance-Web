package pf.transaction;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pf.account.AccountEntity;
import pf.user.UserEntity;

import java.util.List;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {

	List<TransactionEntity> findByUserOrderByDate(UserEntity user); //only one OrderBy Allowed
	
	List<TransactionEntity> findByUserAndWithdrawAccountOrDepositAccountOrderByDate(
			UserEntity user,
			AccountEntity withdraw,
			AccountEntity deposit);

    List<TransactionEntity> findByUserAndDateBetweenOrderByDate(UserEntity user,
                                                                String fromDate,
                                                                String toDate);

    List<TransactionEntity> findByUser_IdAndDateBetweenOrderByDate(String userId, String fromDate, String toDate);

    void deleteByUser(UserEntity user);
    void deleteByUser_Id(String userId);

    List<TransactionEntity> findByUser_Id(String userId);


    @Query("select t from TransactionEntity t where t.id=?1")
    TransactionEntity queryById(String id);

	@Query(value = "SELECT min(date(r.date)), max(date(r.date)) FROM TransactionEntity r where r.user = ?1")
	String queryMinAndMaxDate(UserEntity user);


    @Query("SELECT t FROM TransactionEntity t where t.user = ?1 AND (t.date BETWEEN ?2 AND ?3) AND (t.withdrawAccount = ?4 OR t.depositAccount=?4) ORDER BY t.date")
    List<TransactionEntity> queryByUserAndDateBetweenAndAccountOrderByDate(
		    UserEntity user, String dateFrom, String dateTo,
		    AccountEntity account);

    //FIXME: not tested
    @Query("SELECT t FROM TransactionEntity t where t.user = ?1 AND (t.withdrawAccount = ?2 OR t.depositAccount=?2) ORDER BY t.date")
    List<TransactionEntity> queryByUserAndAccountOrderByDate(UserEntity user, AccountEntity account);

  
}
