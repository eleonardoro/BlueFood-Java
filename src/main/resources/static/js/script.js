function isNumberKey(evt){
	
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	
	if((parseInt(charCode) >= 48 && parseInt(charCode) <= 57) || parseInt(charCode) <= 31){
		return true;
	}
		
	return false;
}