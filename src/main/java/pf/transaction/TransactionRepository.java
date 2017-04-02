package pf.transaction;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pf.account.Account;
import pf.user.User;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

	List<Transaction> findByUserOrderByDate(User user); //only one OrderBy Allowed
	
	List<Transaction> findByUserAndWithdrawAccountOrDepositAccountOrderByDate(
			User user,
			Account withdraw,
			Account deposit);

    List<Transaction> findByUserAndDateBetweenOrderByDate(User user,
                                                                String fromDate,
                                                                String toDate);

    List<Transaction> findByUser_IdAndDateBetweenOrderByDate(String userId, String fromDate, String toDate);

    void deleteByUser(User user);
    void deleteByUser_Id(String userId);

    List<Transaction> findByUser_Id(String userId);


    @Query("select t from Transaction t where t.id=?1")
    Transaction queryById(String id);

	@Query(value = "SELECT min(date(r.date)), max(date(r.date)) FROM Transaction r where r.user = ?1")
	String queryMinAndMaxDate(User user);


    @Query("SELECT t FROM Transaction t where t.user = ?1 AND (t.date BETWEEN ?2 AND ?3) AND (t.withdrawAccount = ?4 OR t.depositAccount=?4) ORDER BY t.date")
    List<Transaction> queryByUserAndDateBetweenAndAccountOrderByDate(
		    User user, String dateFrom, String dateTo,
		    Account account);

    @Query("SELECT t FROM Transaction t where t.user = ?1 AND (t.withdrawAccount = ?2 OR t.depositAccount=?2) ORDER BY t.date")
    List<Transaction> queryByUserAndAccountOrderByDate(User user, Account account);

  
}
