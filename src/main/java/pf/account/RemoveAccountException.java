package pf.account;

class RemoveAccountException extends Exception {
	private static final long serialVersionUID = 1L;
	int transCount = 0;
	
	public void setTransCount(int length) {
		transCount = length;
	}
	
	public int getTransCount() {
		return transCount;
	}

}
